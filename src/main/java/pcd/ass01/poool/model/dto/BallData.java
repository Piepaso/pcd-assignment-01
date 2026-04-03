package pcd.ass01.poool.model.dto;

import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;
import pcd.ass01.poool.model.balls.Ball;

public record BallData(P2d pos, V2d vel, double radius, double mass, int lastCollisionPlayerId) {
	public BallData(Ball b) {
		this(b.getPos(), b.getVel(), b.getRadius(), b.getMass(), b.getLastCollisionPlayerId());
	}
}
