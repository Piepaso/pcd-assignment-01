package pcd.ass01.poool;

import pcd.ass01.poool.model.BallsMonitor;

public class EngineAgent extends Thread {
	private final BallsMonitor ballsMonitor;
	private volatile int fps = 0;

	public EngineAgent(BallsMonitor ballsMonitor) {
		this.ballsMonitor = ballsMonitor;
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

			if (totalTime > 1e9) {
				this.fps = frameCounter;
				totalTime = 0;
				frameCounter = 0;
			}

			//ballsMonitor.notifyNextFrame(elapsed_nano / 1_000_000_000.0);
		}
	}

	public int getFPS() {
		return fps;
	}
}
