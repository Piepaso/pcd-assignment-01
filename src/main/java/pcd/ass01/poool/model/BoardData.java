package pcd.ass01.poool.model;

import java.util.List;

public record BoardData(List<BallData> balls, BallData playerBall, int score, boolean gameOver) {
	@Override
	public String toString() {
		return "BoardData{" +
				"balls=" + balls +
				", playerBall=" + playerBall +
				", score=" + score +
				", gameOver=" + gameOver +
				'}';
	}
}
