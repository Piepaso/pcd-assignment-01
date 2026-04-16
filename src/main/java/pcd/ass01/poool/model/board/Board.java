package pcd.ass01.poool.model.board;


import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.BallsAgent;
import pcd.ass01.poool.model.BoardMonitor;
import pcd.ass01.poool.model.Player;
import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.dto.BallData;
import pcd.ass01.poool.model.dto.BoardData;
import pcd.ass01.poool.model.dto.PlayerData;

import java.util.*;

public class Board {

	private final Boundary bounds;
    private final List<Ball> balls;
	private final List<Hole> holes;
	private final List<BallsAgent> agents = new ArrayList<>();
	private final List<Player> players = new ArrayList<>();
	private final Map<Integer, Player> playersById = new HashMap<>();
	private final int win_score;

    public Board(BoardConf conf) {
	    bounds = conf.getBoardBoundary();
		holes = conf.getHoles();
	    balls = new ArrayList<>(conf.getSmallBalls());
		win_score = conf.getWinScore();

        for (Ball pb : conf.getPlayerBall()) {
            Player player = new Player(pb);
            if (playersById.putIfAbsent(player.id(), player) != null) {
                throw new IllegalArgumentException("Duplicate player id: " + player.id());
            }
            players.add(player);
            balls.add(pb);
        }

		for (Ball b : balls) {
			b.setBounds(bounds);
			b.setHoles(holes);
		}
    }

	public void startEngine(BoardMonitor ballsMonitor, CmdMonitor playerMonitor, int ballsThreadNum) {
        for (int i = 0; i < ballsThreadNum; i++) {
			int fromIndex = i * balls.size() / ballsThreadNum;
			int toIndex = (i + 1) * balls.size() / ballsThreadNum;
			List<Ball> threadBalls = new ArrayList<>();
			for (int j = fromIndex; j < toIndex; j++) {
				threadBalls.add(balls.get(j));
			}
			agents.add(new BallsAgent(threadBalls, ballsMonitor, playerMonitor));
		}
		agents.forEach(Thread::start);
	}

	public BoardData getImmutableData() {

		balls.stream().filter(b -> b.isInHole() && b.getLastCollisionPlayerId() >= 0)
				.forEach(b -> playersById.get(b.getLastCollisionPlayerId()).incrementScore(1));
		balls.removeIf(Ball::isInHole);

		boolean gameOver = players.stream().anyMatch(p -> p.score() >= win_score) ||
                players.stream().filter(p -> !p.ball().isInHole()).count() <= 1;

		return new BoardData(
			balls.stream().map(BallData::new).toList(),
			players.stream().map(PlayerData::new).toList(),
			gameOver
		);
	}

	public List<Hole> getHoles() {
		return holes;
	}
}
