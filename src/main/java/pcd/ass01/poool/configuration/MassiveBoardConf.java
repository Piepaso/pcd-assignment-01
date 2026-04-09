package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.balls.BallFactory;
import pcd.ass01.poool.model.board.Boundary;
import pcd.ass01.poool.model.board.Hole;
import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MassiveBoardConf implements BoardConf {

	private final static int N_ROWS = 30;
	private final static int N_COLS = 150;
	private final BallFactory factory = new BallFactory();

	@Override
	public List<Ball> getPlayerBall() {
		return Collections.emptyList();
	}

	@Override
	public List<Ball> getSmallBalls() {
			double ballRadius = 1.0 / (N_COLS * 1.5);
				List<Ball> balls = new ArrayList<Ball>();

    	for (int row = 0; row < N_ROWS; row++) {
    		for (int col = 0; col < N_COLS; col++) {
					double px = -1.0 + ballRadius + col * ballRadius * 3;
					double py = row * ballRadius * 3;
					Ball b = factory.getSmallBall(new P2d(px, py), ballRadius, 0.25, new V2d(0, 0));
            	balls.add(b);    			
    		}
    	}		
    	return balls;
	}


	@Override
	public List<Hole> getHoles() {
		return Arrays.asList(
				new Hole(new P2d(-0.9, 0.9), 0.1),
				new Hole(new P2d(0.9, 0.9), 0.1)
		);
	}

	@Override
	public int getWinScore() {
		return 500;
	}

	public Boundary getBoardBoundary() {
        return new Boundary(-1.0,-1.0,1.0,1.0);
	}
}
