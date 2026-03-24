package pcd.ass01.poool.model;

import java.util.List;

public class BallsAgent extends Thread {
	private final List<Ball> balls;
	private final Boundary bounds;
	private final BallsMonitor ballsMonitor;
	private final CmdMonitor playerMonitor;

	public BallsAgent(List<Ball> balls, Boundary bounds, BallsMonitor monitor, CmdMonitor playerMonitor) {
		this.balls = balls;
		this.bounds = bounds;
		this.ballsMonitor = monitor;
		this.playerMonitor = playerMonitor;
	}

	public void run() {
		while (true) {

			double dt = ballsMonitor.waitForNextFrame();

			for (Ball b : balls) {
				b.updateState(dt, bounds);
				if (b.isPlayer() && b.getVel().abs() == 0 && playerMonitor.isKickAvailable()) {
					b.kick(playerMonitor.consumeKick());
				}
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
