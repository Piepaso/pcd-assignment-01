package pcd.ass01.poool.model.dto;

import java.util.List;

public record BoardData(List<BallData> balls, List<PlayerData> players, boolean gameOver) {
}
