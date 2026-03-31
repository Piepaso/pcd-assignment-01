package pcd.ass01.poool.model;

public record PlayerData(int id, BallData ball, int score) {
	public PlayerData(Player player) {
		this(player.id(), new BallData(player.ball()), player.score());
	}
}
