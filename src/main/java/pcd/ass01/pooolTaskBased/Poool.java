package pcd.ass01.pooolTaskBased;

import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.configuration.PoolBoardConf;
import pcd.ass01.poool.controller.BotAgent;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.balls.BallFactory;
import pcd.ass01.poool.view.View;
import pcd.ass01.poool.view.ViewModel;

import pcd.ass01.pooolTaskBased.controller.*;
import pcd.ass01.pooolTaskBased.task.ExecuteCommandsTask;
import pcd.ass01.pooolTaskBased.task.RenderTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Poool {
	public static void main(String[] argv) {

		final BallFactory ballFactory = new BallFactory();
		final BoardConf CONFIGURATION = new PoolBoardConf(ballFactory);
		final int THREADS = CONFIGURATION.getSmallBalls().size() > 150 ? Runtime.getRuntime().availableProcessors(): 1;

		Board board = new Board(CONFIGURATION);

		CmdMonitor cmdMonitor = new CmdMonitor(ballFactory.getMousePlayerId());
		TaskBasedController controller = new TaskBasedController(cmdMonitor);

		ViewModel viewModel = new ViewModel(board.getImmutableData(), board.getHoles(), ballFactory.getMousePlayerId());
		View view = new View(viewModel, controller);

		RenderTask renderTask = new RenderTask(view, new RenderSemaphore());
		ExecuteCommandsTask executeCommandsTask = new ExecuteCommandsTask(controller);

		ExecutorService workerPool = Executors.newFixedThreadPool(THREADS);

		ballFactory.getBotIds().forEach(id -> new BotAgent(cmdMonitor, id).start());

		long lastFrame = System.nanoTime();
        double totalTime = 0;
        int frameCounter = 0;

		while (true) {
			try {
                long now = System.nanoTime();
                double dt = (now - lastFrame) * 1e-9;
                lastFrame = now;
                frameCounter++;
                totalTime += dt;
                if (totalTime >= 1) {
                    viewModel.setEngineFPS(frameCounter);
                    frameCounter = 0;
                    totalTime = 0;
                }

                workerPool.invokeAll(board.getUpdateBallsTasks(dt, cmdMonitor)); // implicit barrier

				workerPool.invokeAll(board.getResolveCollisionsTasks());

                viewModel.update(board.getImmutableData());

				workerPool.submit(renderTask);      // one execution by first free worker

				workerPool.submit(executeCommandsTask);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
