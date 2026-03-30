package pcd.ass01.poool.model;


import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.controller.BallsMonitor;
import pcd.ass01.poool.controller.CmdMonitor;

import java.util.*;

public class Board {

    private final Boundary bounds;
    private List<Ball> balls;
	private final List<Hole> holes;
	private final List<BallsAgent> agents = new ArrayList<>();
	private final List<Player> players = new  ArrayList<>();

    public Board(BoardConf conf) {
	    bounds = conf.getBoardBoundary();
		holes = conf.getHoles();
	    balls = new ArrayList<>(conf.getSmallBalls());
        for (Ball pb : conf.getPlayerBall()) {
            players.add(new Player(pb));
            balls.add(pb);
        }

		for (Ball b : balls) {
			b.setBounds(bounds);
			b.setHoles(holes);
		}
    }

	public void startEngine(BallsMonitor ballsMonitor, CmdMonitor playerMonitor, int ballsThreadNum) {
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

	public BoardData getData() {

		balls.stream().filter(b -> b.isInHole() && !b.isPlayer()).forEach(b -> )
		balls.removeIf(Ball::isInHole);
		boolean gameOver = playerBall.isInHole();

		return new BoardData(
			balls.stream().map(BallData::new).toList(),
			new BallData(playerBall),
			score,
			gameOver
		);
	}

	public List<Hole> getHoles() {
		return holes;
	}
}
