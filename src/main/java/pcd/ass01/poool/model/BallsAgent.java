package pcd.ass01.poool.model;

import pcd.ass01.poool.controller.BallsMonitor;
import pcd.ass01.poool.controller.CmdMonitor;

import java.util.List;

public class BallsAgent extends Thread {
	private final List<Ball> balls;
	private final BallsMonitor ballsMonitor;
	private final CmdMonitor playerMonitor;

	public BallsAgent(List<Ball> balls, BallsMonitor monitor, CmdMonitor playerMonitor) {
		this.balls = balls;
		this.ballsMonitor = monitor;
		this.playerMonitor = playerMonitor;
	}

	public void run() {
		while (true) {

			double dt = ballsMonitor.waitForNextFrame();

			for (Ball b : balls) {
				b.updateState(dt);
				if (b.isPlayer() && b.getVel().abs() == 0 && playerMonitor.isKickAvailable()) {
					b.applyKick(playerMonitor.consumeKick());
				}
			}

			List<BallData> allBallsData = ballsMonitor.waitForUpdatedBalls();

			for (Ball b : balls) {
				for (BallData other : allBallsData) {
					b.resolveCollisionWith(other);
				}
				b.updateAfterCollisions();
				if (b.isInHole()) {
					balls.remove(b);
				}
			}
		}
	}
}
