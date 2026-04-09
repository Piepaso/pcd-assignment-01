package pcd.ass01.poool.view;

import pcd.ass01.poool.configuration.StaticConf;
import pcd.ass01.poool.model.BallsMonitor;
import pcd.ass01.poool.model.dto.BoardData;

import javax.swing.*;

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
		//long previousFPSUpdate = System.currentTimeMillis();
		BoardData boardData = ballsMonitor.getUpdatedBoardData();

		for (int i = 0; i < StaticConf.AGENTS_ITERATIONS; i++)  {
			viewModel.update(boardData);

			if (i % 3 == 0) {
				viewModel.updateEngineFPS(ballsMonitor.getFrames());
				viewModel.updateViewFPS(frameCounter);
				frameCounter = 0;
			}

			view.render();
			SwingUtilities.invokeLater(renderMonitor::signal);
			boardData = ballsMonitor.getUpdatedBoardData();
			renderMonitor.await();

			frameCounter++;
		}
	}
}
