package pcd.ass01.poool;

import pcd.ass01.poool.configuration.BoardData;
import pcd.ass01.poool.view.*;

import java.util.function.Supplier;

public class ViewAgent extends Thread {

	private final View view;
	private final ViewModel viewModel;
	private final Supplier<BoardData> boardSupplier;
	private final Supplier<Integer> engineFPSgetter;

	public ViewAgent(View view, ViewModel viewModel, Supplier<BoardData> boardSupplier, Supplier<Integer> fps) {
		this.view = view;
		this.viewModel = viewModel;
		this.boardSupplier = boardSupplier;
		this.engineFPSgetter = fps;
	}

	public void run() {
		long previousFPSUpdate = System.nanoTime();
		int frameCounter = 0;

		while (true) {
			BoardData boardData = boardSupplier.get();
			viewModel.update(boardData);
			view.render();

			frameCounter++;
			if (System.nanoTime() - previousFPSUpdate >= 1_000_000_000L) {
				viewModel.updateEngineFPS(engineFPSgetter.get());
				viewModel.updateViewFPS(frameCounter);
				previousFPSUpdate = System.nanoTime();
				frameCounter = 0;
			}

		}
	}
}
