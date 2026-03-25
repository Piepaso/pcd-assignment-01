package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.*;

import java.util.ArrayList;
import java.util.List;

public class LargeBoardConf implements BoardConf {

	@Override
	public Ball getPlayerBall() {
		return  new  Ball(new P2d(0, -0.75), 0.05, 1.5, new V2d(0,1), true);
	}

	@Override
	public List<Ball> getSmallBalls() {
		var ballRadius = 0.01;
        var balls = new ArrayList< Ball>();

    	for (int row = 0; row < 20; row++) {
    		for (int col = 0; col < 20; col++) {
        		var px = -0.25 + col*0.025;
        		var py =  row*0.025;
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
