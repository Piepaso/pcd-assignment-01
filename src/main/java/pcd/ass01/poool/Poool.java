package pcd.ass01.poool;

import pcd.ass01.poool.model.*;
import pcd.ass01.poool.view.View;
import pcd.ass01.poool.view.ViewModel;

public class Poool {

	
	public static void main(String[] argv) {

		final int THREADS = 20;

		BoardConf conf = new MassiveBoardConf();
		Board board = new Board(conf, THREADS);

		BallsMonitor ballsMonitor = new BallsMonitor(board, THREADS);

		board.init(ballsMonitor);

		//EngineAgent engineAgent = new EngineAgent(ballsMonitor);

		ViewModel viewModel = new ViewModel();
		viewModel.init(ballsMonitor.getUpdatedBoardData());

		View view = new View(viewModel);

		ViewAgent viewAgent = new ViewAgent(view, viewModel, ballsMonitor::getUpdatedBoardData, ballsMonitor::getFrames);

		//engineAgent.start();
		viewAgent.start();

		/*long totalTime = 0;
		int frameCount = 0;
		long lastUpdateTime = System.nanoTime();
		long stateUpdateTime;
		long renderTime;

				
		while (true){

			
			long elapsed_nano = System.nanoTime() - lastUpdateTime;
			lastUpdateTime = System.nanoTime();
			totalTime += elapsed_nano;
			frameCount++;

			stateUpdateTime = System.nanoTime();

			board.updateState(elapsed_nano / 1_000_000_000.0);

			System.out.println("State update time: " + (System.nanoTime() - stateUpdateTime) + " ns");

			renderTime = System.nanoTime();

			viewModel.update(board);

			view.render();

			System.out.println("Render time: " + (System.nanoTime() - renderTime) + " ns");

			if (totalTime > 1_000_000_000) {
				System.out.println("FPS: " + frameCount);
				frameCount = 0;
				totalTime = 0;
			}

			
		}
	}*/
	}
}
