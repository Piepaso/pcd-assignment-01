package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.balls.BallFactory;
import pcd.ass01.poool.model.board.Boundary;
import pcd.ass01.poool.model.board.Hole;
import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;

import java.util.ArrayList;
import java.util.List;

public class MassiveBoardConf implements BoardConf {

	private final static int N_ROWS = 30;
	private final static int N_COLS = 150;
	private final BallFactory factory;

    public MassiveBoardConf(BallFactory ballFactory) {
        this.factory = ballFactory;
    }

    @Override
    public List<Ball> getPlayerBall() {
        return List.of(
                factory.getPlayerBall(new P2d(-0.2, -0.75), 0.03, 2.0, new V2d(0, 0), PlayerType.MOUSE),
                factory.getPlayerBall(new P2d(0.2, -0.75), 0.03, 2.0, new V2d(0, 0), PlayerType.BOT),
                factory.getPlayerBall(new P2d(0.4, -0.75), 0.03, 2.0, new V2d(0, 0), PlayerType.BOT)

        );
    }

	@Override
	public List<Ball> getSmallBalls() {
		var ballRadius = 1.0 / (N_COLS*1.5);
        var balls = new ArrayList<Ball>();

    	for (int row = 0; row < N_ROWS; row++) {
    		for (int col = 0; col < N_COLS; col++) {
        		var px = -1.0 + ballRadius + col * ballRadius*3;
        		var py =  row * ballRadius*3;
        		var b = factory.getSmallBall(new P2d(px, py), ballRadius, 0.25, new V2d(0,0));
            	balls.add(b);    			
    		}
    	}		
    	return balls;
	}


    @Override
    public List<Hole> getHoles() {
        return List.of(
                new Hole(new P2d(-1, -1), 0.1),
                new Hole(new P2d(1, -1), 0.1),
                new Hole(new P2d(-1, 1), 0.1),
                new Hole(new P2d(1, 1), 0.1)
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
