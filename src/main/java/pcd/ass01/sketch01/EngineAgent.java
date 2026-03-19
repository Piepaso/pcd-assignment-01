package pcd.ass01.sketch01;

import pcd.ass01.sketch01.model.BoardMonitor;

public class EngineAgent extends Thread {
	private final BoardMonitor boardMonitor;

	public EngineAgent(BoardMonitor boardMonitor) {
		this.boardMonitor = boardMonitor;
	}

	public void run() {
		long lastUpdateTime = System.nanoTime();
		long totalTime = 0;
		int frameCounter = 0;

		while (true) {
			long elapsed_nano = System.nanoTime() - lastUpdateTime;
			lastUpdateTime = System.nanoTime();

			totalTime+= elapsed_nano;
			frameCounter ++;

			if (totalTime > 10e8) {
				System.out.println("Engine FPS: " + frameCounter);
				totalTime = 0;
				frameCounter = 0;
			}

			boardMonitor.updateBoard(elapsed_nano / 1_000_000_000.0);
		}
	}
}
