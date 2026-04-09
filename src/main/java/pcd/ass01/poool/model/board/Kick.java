package pcd.ass01.poool.model.board;

import java.util.Objects;

public final class Kick {

	private final P2d position;
	private final double strength;

	public Kick(P2d position, double strength) {
		this.position = position;
		this.strength = strength;
	}

	public Kick(P2d position, long startTime, long endTime) {
		this(position, (endTime - startTime) / 1000.0);
	}

	public P2d position() {
		return position;
	}

	public double strength() {
		return strength;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Kick kick = (Kick) o;
		return Double.compare(kick.strength, strength) == 0 && Objects.equals(position, kick.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, strength);
	}

	@Override
	public String toString() {
		return "Kick[position=" + position + ", strength=" + strength + "]";
	}
}
