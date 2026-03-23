package pcd.ass01.poool.model;

import java.util.List;

public class BallsAgent extends Thread {
	private final List<Ball> balls;
	private final Boundary bounds;
	private final BallsMonitor ballsMonitor;

	public BallsAgent(List<Ball> balls, Boundary bounds, BallsMonitor monitor) {
		this.balls = balls;
		this.bounds = bounds;
		this.ballsMonitor = monitor;
	}

	public void run() {
		while (true) {

			double dt = ballsMonitor.waitForNextFrame();

			for (Ball b : balls) {
				b.updateState(dt, bounds);
			}

			List<BallData> allBallsData = ballsMonitor.waitForUpdatedBalls();
			for (Ball b : balls) {
				for (BallData other : allBallsData) {
					b.resolveCollision(other);
				}
				b.applyIncreases();
			}
		}
	}
}
