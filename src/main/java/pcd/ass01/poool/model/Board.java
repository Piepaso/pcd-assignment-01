package pcd.ass01.poool.model;


import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.controller.BallsMonitor;
import pcd.ass01.poool.controller.CmdMonitor;

import java.util.*;

public class Board {

    private final Boundary bounds;
    private final List<Ball> balls;
	private final List<Hole> holes;
	private final List<BallsAgent> agents = new ArrayList<>();
	private int score = 0;
	private BallsMonitor ballsMonitor;

    public Board(BoardConf conf) {
	    bounds = conf.getBoardBoundary();
		holes = conf.getHoles();
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
			agents.add(new BallsAgent(balls.subList(fromIndex, toIndex), ballsMonitor, playerMonitor));
		}
		agents.forEach(Thread::start);
	}

	public BoardData getData() {

		for (Ball b : balls) {
			if (b.isInHole()) {
				ballInHole(b);
			}
		}
		Ball playerBall = balls.get(balls.size() - 1);
		boolean gameOver = playerBall.isInHole();

		if (gameOver) {
			agents.forEach(Thread::interrupt);
			ballsMonitor.notifyGameOver();
		}

		return new BoardData(
			balls.stream().map(BallData::new).toList(),
			new BallData(playerBall),
			score,
			gameOver
		);
	}

	private void ballInHole(Ball b) {
		if (!b.isPlayer()) {
			score++;
		}
		balls.remove(b);
	}

	public List<Hole> getHoles() {
		return holes;
	}
}
