package pcd.ass01.poool.model;

import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.dto.BallData;

import java.util.List;

public class BallsAgent extends Thread {
	private final List<Ball> balls;
	private final BoardMonitor boardMonitor;
	private final CmdMonitor playerMonitor;

	public BallsAgent(List<Ball> balls, BoardMonitor monitor, CmdMonitor playerMonitor) {
		this.balls = balls;
		this.boardMonitor = monitor;
		this.playerMonitor = playerMonitor;
	}

	public void run() {
		while (true) {

			double dt = boardMonitor.waitForNextFrame();

			for (Ball b : balls) {
				b.updateState(dt);
				if (playerMonitor.isKickAvailable(b.getPlayerId())) {
					b.applyKick(playerMonitor.consumeKick(b.getPlayerId()));
				}
			}

			List<BallData> allBallsData = boardMonitor.waitForUpdatedBalls();

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
