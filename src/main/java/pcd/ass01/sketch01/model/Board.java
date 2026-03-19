package pcd.ass01.sketch01.model;

import java.util.*;

public class Board {

	private final static int NUM_BALLS = 10;
	private final static double BALL_RADIUS = 0.02;
	private final static double BALL_MASS = 1.0;

    private Boundary bounds;
    private ArrayList<Ball> balls;
    private Ball playerBall;
    
    public Board(){
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        balls = new ArrayList<Ball>();
    } 
    
    public void init() {

		var v = new V2d(0.0, 0.0);
		var points = generateTriangle(NUM_BALLS, BALL_RADIUS, new P2d(0, 0.5));
		for (var p: points) {
			balls.add(new Ball(p, BALL_RADIUS, BALL_MASS, v));
		}
    	
    	playerBall = new Ball(new P2d(0, -0.6), 0.06, 10.0, new V2d(0,1));
    }
    
    public void updateState(double dt) {

    	playerBall.updateState(dt, this);
    	
    	for (var b: balls) {
    		b.updateState(dt, this);
    	}       	
    	
    	for (int i = 0; i < balls.size() - 1; i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                Ball.resolveCollision(balls.get(i), balls.get(j));
            }
        }
    	for (var b: balls) {
    		Ball.resolveCollision(playerBall, b);
    	} 
    	   	    	
    }

	public BoardData getData() {
		return new BoardData(
			bounds,
			balls.stream().map(b -> new BallData(b.getPos(), b.getVel(), b.getRadius(), b.getMass())).toArray(BallData[]::new),
			new BallData(playerBall.getPos(), playerBall.getVel(), playerBall.getRadius(), playerBall.getMass()));
	}

	private static List<P2d> generateTriangle(int rows, double radius, P2d startVertex) {
		List<P2d> points = new ArrayList<>();
		double centerDistance = 2 * radius;
		double rowHeight = radius * Math.sqrt(3.0);

		for (int row = 0; row < rows; row++) {
			double yOffset = row * rowHeight;
			double xStartOffset = -((row * centerDistance) / 2.0);

			for (int col = 0; col <= row; col++) {
				double xOffset = xStartOffset + (col * centerDistance);
				points.add(startVertex.sum(new V2d(xOffset, yOffset)));
			}
		}

		return points;
	}

	protected Boundary getBounds() {
		return bounds;
	}
}
