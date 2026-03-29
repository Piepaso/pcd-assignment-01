package pcd.ass01.poool;

import pcd.ass01.poool.controller.BallsMonitor;
import pcd.ass01.poool.model.BoardData;
import pcd.ass01.poool.view.*;

import java.util.function.Supplier;

public class ViewAgent extends Thread {

	private final View view;
	private final ViewModel viewModel;
	private final BallsMonitor ballsMonitor;
	private final RenderMonitor renderMonitor;

	private int frameCounter = 0;

	public ViewAgent(View view, ViewModel viewModel, BallsMonitor ballsMonitor, RenderMonitor renderMonitor) {
		this.view = view;
		this.viewModel = viewModel;
		this.ballsMonitor = ballsMonitor;
		this.renderMonitor = renderMonitor;
	}

	public void run() {
		long previousFPSUpdate = System.currentTimeMillis();

		while (true) {
			BoardData boardData = ballsMonitor.getUpdatedBoardData();
			viewModel.update(boardData);


			if (System.currentTimeMillis() - previousFPSUpdate >= 1000) {
				viewModel.updateEngineFPS(ballsMonitor.getFrames());
				viewModel.updateViewFPS(frameCounter);
				previousFPSUpdate = System.currentTimeMillis();
				frameCounter = 0;
			}

			view.render();
			renderMonitor.await();

			frameCounter++;
		}
	}
}
