package pcd.ass01.poool.model.dto;

import java.util.List;
import java.util.Objects;

public final class BoardData {

	private final List<BallData> balls;
	private final List<PlayerData> players;
	private final boolean gameOver;

	public BoardData(List<BallData> balls, List<PlayerData> players, boolean gameOver) {
		this.balls = balls;
		this.players = players;
		this.gameOver = gameOver;
	}

	public List<BallData> balls() {
		return balls;
	}

	public List<PlayerData> players() {
		return players;
	}

	public boolean gameOver() {
		return gameOver;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BoardData boardData = (BoardData) o;
		return gameOver == boardData.gameOver
				&& Objects.equals(balls, boardData.balls)
				&& Objects.equals(players, boardData.players);
	}

	@Override
	public int hashCode() {
		return Objects.hash(balls, players, gameOver);
	}

	@Override
	public String toString() {
		return "BoardData[balls=" + balls + ", players=" + players + ", gameOver=" + gameOver + "]";
	}
}
