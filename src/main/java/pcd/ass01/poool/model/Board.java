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
	private int score = 0;
	private BallsMonitor ballsMonitor;
	private final Ball playerBall;

    public Board(BoardConf conf) {
	    bounds = conf.getBoardBoundary();
		holes = conf.getHoles();
		playerBall = conf.getPlayerBall();
	    balls = new ArrayList<>(conf.getSmallBalls());
	    balls.add(conf.getPlayerBall());
		for (Ball b : balls) {
			b.setBounds(bounds);
			b.setHoles(holes);
		}
    }

	public void startBalls(BallsMonitor ballsMonitor, CmdMonitor playerMonitor, int ballsThreadNum) {
		this.ballsMonitor = ballsMonitor;
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

		for (Ball b : balls) {
			if (b.isInHole()) {
				ballInHole(b);
			}
		}

		boolean gameOver = playerBall.isInHole();

		return new BoardData(
			balls.stream().map(BallData::new).toList(),
			new BallData(playerBall),
			score,
			gameOver
		);
	}

	private void ballInHole(Ball ball) {
		if (!ball.isPlayer()) {
			score++;
		}
		balls = balls.stream().filter(b -> b != ball).toList();
	}

	public List<Hole> getHoles() {
		return holes;
	}
}
