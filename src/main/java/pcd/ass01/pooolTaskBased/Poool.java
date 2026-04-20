package pcd.ass01.pooolTaskBased;

import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.configuration.PoolBoardConf;
import pcd.ass01.poool.controller.BotAgent;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.balls.BallFactory;
import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.board.Kick;
import pcd.ass01.poool.model.dto.BallData;
import pcd.ass01.poool.model.dto.BoardData;
import pcd.ass01.poool.view.View;
import pcd.ass01.poool.view.ViewModel;
import pcd.ass01.pooolTaskBased.controller.TaskBasedController;
import pcd.ass01.pooolTaskBased.task.ExecuteCommandsTask;
import pcd.ass01.pooolTaskBased.task.RenderTask;
import pcd.ass01.pooolTaskBased.task.ResolveBallsCollisionTask;
import pcd.ass01.pooolTaskBased.task.Task;
import pcd.ass01.pooolTaskBased.task.UpdateBallsTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Poool {

	private static final double MIN_FRAME_DT = 1e-4;

	private static <T> void waitFutures(List<Future<T>> futures) {
		try {
			for (Future<T> f : futures) {
				f.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] argv) {

		final BallFactory ballFactory = new BallFactory();
		final BoardConf CONFIGURATION = new PoolBoardConf(ballFactory);
		final int THREADS = CONFIGURATION.getSmallBalls().size() > 150 ? Runtime.getRuntime().availableProcessors(): 1;

		Board board = new Board(CONFIGURATION);

		CmdMonitor cmdMonitor = new CmdMonitor(ballFactory.getMousePlayerId());
		TaskBasedController controller = new TaskBasedController();

		ViewModel viewModel = new ViewModel(board.getImmutableData(), board.getHoles(), ballFactory.getMousePlayerId());
		View view = new View(viewModel, controller);
		RenderTask renderTask = new RenderTask(view);
		ExecuteCommandsTask executeCommandsTask = new ExecuteCommandsTask(controller);

		ExecutorService workerPool = Executors.newFixedThreadPool(THREADS);
		Runtime.getRuntime().addShutdownHook(new Thread(workerPool::shutdownNow));

		ballFactory.getBotIds().forEach(id -> new BotAgent(cmdMonitor, id).start());

		long lastFrame = System.nanoTime();
		while (true) {
			try {
				var ballFutures = workerPool.invokeAll(board.getResolveCollisionsTasks());

				renderTask.call();
				Future<Map<Integer, Kick>> kicksFuture = workerPool.submit(executeCommandsTask);

				waitFutures(ballFutures);
				viewModel.update(board.getImmutableData());


				long now = System.nanoTime();
				double dt = (now - lastFrame) * 1e-9;
				lastFrame = now;

				ballFutures = workerPool.invokeAll(board.getUpdateBallsTasks(dt, kicksFuture.get()));
				waitFutures(ballFutures);



			} catch (Exception ex) {
				ex.printStackTrace();
				workerPool.shutdownNow();
				return;
			}
		}
	}
}
