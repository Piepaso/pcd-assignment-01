package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.*;

import java.util.ArrayList;
import java.util.List;

public class MassiveBoardConf implements BoardConf {

	private final static int N_ROWS = 30;
	private final static int N_COLS = 150;

	@Override
	public Ball getPlayerBall() {
		return  new Ball(new P2d(0, -0.75), 0.05, 20.0, new V2d(0,0.5), true);
	}

	@Override
	public List<Ball> getSmallBalls() {
		var ballRadius = 1.0 / (N_COLS*1.5);
        var balls = new ArrayList<Ball>();

    	for (int row = 0; row < N_ROWS; row++) {
    		for (int col = 0; col < N_COLS; col++) {
        		var px = -1.0 + ballRadius + col * ballRadius*3;
        		var py =  row * ballRadius*3;
        		var b = new Ball(new P2d(px, py), ballRadius, 0.25, new V2d(0,0), false);
            	balls.add(b);    			
    		}
    	}		
    	return balls;
	}


	@Override
	public List<Hole> getHoles() {
		return List.of(
				new Hole(new P2d(-0.9, 0.9), 0.1),
				new Hole(new P2d(0.9, 0.9), 0.1)
		);
	}

	public Boundary getBoardBoundary() {
        return new Boundary(-1.0,-1.0,1.0,1.0);
	}
}
