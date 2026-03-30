package pcd.ass01.poool.model;

public record BallData(P2d pos, V2d vel, double radius, double mass, int lastCollisionPlayerId) {
	public BallData(Ball b) {
		this(b.getPos(), b.getVel(), b.getRadius(), b.getMass(), b.getLastCollisionPlayerId());
	}
}
