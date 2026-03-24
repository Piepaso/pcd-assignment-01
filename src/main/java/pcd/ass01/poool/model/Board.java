package pcd.ass01.poool.model;


import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.configuration.BoardData;

import java.util.*;

public class Board {

	private final int ballsThreadNum;
    private final Boundary bounds;
    private final List<Ball> balls;

    public Board(BoardConf conf, int ballsThreadNum) {
		this.ballsThreadNum = ballsThreadNum;
	    balls = new ArrayList<>(conf.getSmallBalls());
	    balls.add(conf.getPlayerBall());
	    bounds = conf.getBoardBoundary();
    }

	public void startBalls(BallsMonitor ballsMonitor, CmdMonitor playerMonitor) {
		for (int i = 0; i < ballsThreadNum; i++) {
			int fromIndex = i * balls.size() / ballsThreadNum;
			int toIndex = (i + 1) * balls.size() / ballsThreadNum;
			new BallsAgent(balls.subList(fromIndex, toIndex), bounds, ballsMonitor, playerMonitor).start();
		}
	}

	public BoardData getData() {
		Ball playerBall = balls.get(balls.size() - 1);
		return new BoardData(
			bounds,
			balls.stream().map(BallData::new).toList(),
			new BallData(playerBall)
		);
	}
}
