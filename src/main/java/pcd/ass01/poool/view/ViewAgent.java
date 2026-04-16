package pcd.ass01.poool.view;

import pcd.ass01.poool.model.BoardMonitor;
import pcd.ass01.poool.model.dto.BoardData;

import javax.swing.*;

public class ViewAgent extends Thread {

	private final View view;
	private final ViewModel viewModel;
	private final BoardMonitor boardMonitor;
	private final RenderMonitor renderMonitor;

	private int frameCounter = 0;

	public ViewAgent(View view, ViewModel viewModel, BoardMonitor boardMonitor, RenderMonitor renderMonitor) {
		this.view = view;
		this.viewModel = viewModel;
		this.boardMonitor = boardMonitor;
		this.renderMonitor = renderMonitor;
	}

	public void run() {
		long previousFPSUpdate = System.currentTimeMillis();
		BoardData boardData = boardMonitor.getUpdatedBoardData();

		while (true) {
			viewModel.update(boardData);

			if (System.currentTimeMillis() - previousFPSUpdate >= 1000) {
				viewModel.updateEngineFPS(boardMonitor.getFrames());
				viewModel.updateViewFPS(frameCounter);
				previousFPSUpdate = System.currentTimeMillis();
				frameCounter = 0;
			}

			view.render();
			SwingUtilities.invokeLater(renderMonitor::signal);
			boardData = boardMonitor.getUpdatedBoardData();
			renderMonitor.await();

			frameCounter++;
		}
	}
}
