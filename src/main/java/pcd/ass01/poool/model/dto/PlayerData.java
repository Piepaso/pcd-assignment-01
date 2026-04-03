package pcd.ass01.poool.model.dto;

import pcd.ass01.poool.model.Player;

public record PlayerData(int id, BallData ball, int score, boolean isAlive) {
	public PlayerData(Player player) {
		this(player.id(), new BallData(player.ball()), player.score(), !player.ball().isInHole());
	}
}
