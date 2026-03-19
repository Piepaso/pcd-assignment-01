package pcd.ass01.sketch01;

import pcd.ass01.sketch01.model.Board;
import pcd.ass01.sketch01.model.BoardMonitor;
import pcd.ass01.sketch01.view.View;
import pcd.ass01.sketch01.view.ViewModel;

public class Sketch01 {

	
	public static void main(String[] argv) {

		Board board = new Board();
		board.init();

		BoardMonitor boardMonitor = new BoardMonitor(board);
		EngineAgent engineAgent = new EngineAgent(boardMonitor);

		ViewModel viewModel = new ViewModel();
		View view = new View(viewModel);
		viewModel.init();

		ViewAgent viewAgent = new ViewAgent(view, viewModel, boardMonitor::getBoard);

		engineAgent.start();
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
