package pcd.ass01.sketch01;

import pcd.ass01.sketch01.model.BoardData;
import pcd.ass01.sketch01.view.*;

import java.util.function.Supplier;

public class ViewAgent extends Thread {

	private final View view;
	private final ViewModel viewModel;
	private final Supplier<BoardData> boardSupplier;

	public ViewAgent(View view, ViewModel viewModel, Supplier<BoardData> boardSupplier) {
		this.view = view;
		this.viewModel = viewModel;
		this.boardSupplier = boardSupplier;
	}

	public void run() {
		long previousFPSUpdate = System.nanoTime();
		int frameCounter = 0;

		while (true) {
			BoardData boardData = boardSupplier.get();
			viewModel.update(boardData);
			view.render();

			frameCounter++;
			if (System.nanoTime() - previousFPSUpdate >= 10e8) {
				System.out.println("Render FPS: " + frameCounter);
				previousFPSUpdate = System.nanoTime();
				frameCounter = 0;
			}

		}
	}
}
