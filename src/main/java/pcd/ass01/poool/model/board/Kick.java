package pcd.ass01.poool.model.board;

public record Kick(P2d position, double strength) {
	public Kick(P2d position, long startTime, long endTime) {
		this(position, (endTime - startTime) / 1000.0);
	}
}
