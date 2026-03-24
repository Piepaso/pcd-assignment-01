package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.Ball;
import pcd.ass01.poool.model.Boundary;
import pcd.ass01.poool.model.P2d;
import pcd.ass01.poool.model.V2d;

import java.util.ArrayList;
import java.util.List;

public class PoolConf implements BoardConf {

	private final static int NUM_LAYERS = 15;
	private final static double BALL_RADIUS = 0.02;
	private final static double BALL_MASS = 0.5;
	private final static P2d START_VERTEX = new P2d(0, 0);

	private final static Boundary BOUNDARY = new Boundary(-1.0, -1.0, 1.0, 1.0);
	private final static Ball PLAYER_BALL = new Ball(new P2d(0, -0.75), 0.05, 2.0, new V2d(0, 0), true);

	@Override
	public Boundary getBoardBoundary() {
		return BOUNDARY;
	}

	@Override
	public Ball getPlayerBall() {
		return PLAYER_BALL;
	}

	@Override
	public List<Ball> getSmallBalls() {
		return generateTriangle().stream()
			.map(p -> new Ball(p, BALL_RADIUS, BALL_MASS, new V2d(0, 0), false))
			.toList();
	}

	private static List<P2d> generateTriangle() {
		List<P2d> points = new ArrayList<>();
		double centerDistance = 2 * BALL_RADIUS;
		double rowHeight = BALL_RADIUS * Math.sqrt(3.0);

		for (int row = 0; row < NUM_LAYERS; row++) {
			double yOffset = row * rowHeight;
			double xStartOffset = -((row * centerDistance) / 2.0);

			for (int col = 0; col <= row; col++) {
				double xOffset = xStartOffset + (col * centerDistance);
				points.add(START_VERTEX.sum(new V2d(xOffset, yOffset)));
			}
		}

		return points;
	}
}
