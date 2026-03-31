package pcd.ass01.poool.model;

import java.util.List;

public record BoardData(List<BallData> balls, List<PlayerData> players, boolean gameOver) {
}
