package pcd.ass01.poool.model.dto;

import pcd.ass01.poool.model.Player;

import java.util.Objects;

public final class PlayerData {

	private final int id;
	private final BallData ball;
	private final int score;
	private final boolean isAlive;

	public PlayerData(int id, BallData ball, int score, boolean isAlive) {
		this.id = id;
		this.ball = ball;
		this.score = score;
		this.isAlive = isAlive;
	}

	public PlayerData(Player player) {
		this(player.id(), new BallData(player.ball()), player.score(), !player.ball().isInHole());
	}

	public int id() {
		return id;
	}

	public BallData ball() {
		return ball;
	}

	public int score() {
		return score;
	}

	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PlayerData that = (PlayerData) o;
		return id == that.id && score == that.score && isAlive == that.isAlive && Objects.equals(ball, that.ball);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, ball, score, isAlive);
	}

	@Override
	public String toString() {
		return "PlayerData[id=" + id + ", ball=" + ball + ", score=" + score + ", isAlive=" + isAlive + "]";
	}
}
