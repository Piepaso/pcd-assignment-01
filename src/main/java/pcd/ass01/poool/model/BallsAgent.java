package pcd.ass01.poool.model;

import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.dto.BallData;

import java.util.List;

public class BallsAgent extends Thread {
	private final List<Ball> balls;
	private final BoardMonitor boardMonitor;
	private final CmdMonitor cmdMonitor;

	public BallsAgent(List<Ball> balls, BoardMonitor monitor, CmdMonitor cmdMonitor) {
		this.balls = balls;
		this.boardMonitor = monitor;
		this.cmdMonitor = cmdMonitor;
	}

	public void run() {
		while (true) {

			double dt = boardMonitor.waitForNextFrame();

			for (Ball b : balls) {
				b.updateState(dt);
				if (cmdMonitor.isKickAvailable(b.getPlayerId())) {
					b.applyKick(cmdMonitor.consumeKick(b.getPlayerId()));
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
