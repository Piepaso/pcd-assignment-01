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
				if (b.isPlayer() && playerMonitor.isKickAvailable(b.getPlayerId())) {
					b.applyKick(playerMonitor.consumeKick(b.getPlayerId()));
				}
			}

			List<BallData> allBallsData = ballsMonitor.waitForUpdatedBalls();

			for (Ball ball : balls) {
				for (BallData other : allBallsData) {
					ball.resolveCollisionWith(other);
				}
				ball.updateAfterCollisions();
			}
			balls.removeIf(Ball::isInHole);
		}
	}
}
