package pcd.ass01.poool.model;


import java.util.*;

public class Board {
    private Boundary bounds;
    private List<Ball> balls;
    private Ball playerBall;

    public Board(){
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        balls = new ArrayList<Ball>();
    }

	public void init(BoardConf conf) {
		balls = conf.getSmallBalls();
		playerBall = conf.getPlayerBall();
		bounds = conf.getBoardBoundary();
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
