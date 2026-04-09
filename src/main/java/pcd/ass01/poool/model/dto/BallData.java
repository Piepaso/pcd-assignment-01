package pcd.ass01.poool.model.dto;

import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;
import pcd.ass01.poool.model.balls.Ball;

import java.util.Objects;

public final class BallData {

	private final P2d pos;
	private final V2d vel;
	private final double radius;
	private final double mass;
	private final int lastCollisionPlayerId;
	private final boolean isPlayer;

	public BallData(P2d pos, V2d vel, double radius, double mass, int lastCollisionPlayerId, boolean isPlayer) {
		this.pos = pos;
		this.vel = vel;
		this.radius = radius;
		this.mass = mass;
		this.lastCollisionPlayerId = lastCollisionPlayerId;
		this.isPlayer = isPlayer;
	}

	public BallData(Ball b) {
		this(b.getPos(), b.getVel(), b.getRadius(), b.getMass(), b.getLastCollisionPlayerId(), b.getPlayerId() >= 0);
	}

	public P2d pos() {
		return pos;
	}

	public V2d vel() {
		return vel;
	}

	public double radius() {
		return radius;
	}

	public double mass() {
		return mass;
	}

	public int lastCollisionPlayerId() {
		return lastCollisionPlayerId;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BallData ballData = (BallData) o;
		return Double.compare(ballData.radius, radius) == 0
				&& Double.compare(ballData.mass, mass) == 0
				&& lastCollisionPlayerId == ballData.lastCollisionPlayerId
				&& isPlayer == ballData.isPlayer
				&& Objects.equals(pos, ballData.pos)
				&& Objects.equals(vel, ballData.vel);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pos, vel, radius, mass, lastCollisionPlayerId, isPlayer);
	}

	@Override
	public String toString() {
		return "BallData[pos=" + pos + ", vel=" + vel + ", radius=" + radius + ", mass=" + mass
				+ ", lastCollisionPlayerId=" + lastCollisionPlayerId + ", isPlayer=" + isPlayer + "]";
	}
}
