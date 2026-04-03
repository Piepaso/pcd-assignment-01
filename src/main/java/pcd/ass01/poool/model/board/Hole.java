package pcd.ass01.poool.model.board;

public record Hole(P2d position, double radius) {

	public boolean isIn(P2d pos) {
		return position.sub(pos).abs() <= radius;
	}
}
