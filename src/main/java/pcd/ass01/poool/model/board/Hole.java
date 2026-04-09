package pcd.ass01.poool.model.board;

import java.util.Objects;

public final class Hole {

	private final P2d position;
	private final double radius;

	public Hole(P2d position, double radius) {
		this.position = position;
		this.radius = radius;
	}

	public boolean isIn(P2d pos) {
		return position.sub(pos).abs() <= radius;
	}

	public P2d position() {
		return position;
	}

	public double radius() {
		return radius;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Hole hole = (Hole) o;
		return Double.compare(hole.radius, radius) == 0 && Objects.equals(position, hole.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, radius);
	}

	@Override
	public String toString() {
		return "Hole[position=" + position + ", radius=" + radius + "]";
	}
}
