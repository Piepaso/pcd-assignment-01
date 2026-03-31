package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PoolConf implements BoardConf {

	private final static int NUM_LAYERS = 15;
	private final static double BALL_RADIUS = 0.02;
	private final static double BALL_MASS = 0.5;
	private final static P2d START_VERTEX = new P2d(0, 0);

	private final static Boundary BOUNDARY = new Boundary(-1.0, -1.0, 1.0, 1.0);

	@Override
	public Boundary getBoardBoundary() {
		return BOUNDARY;
	}

	@Override
	public List<Ball> getPlayerBall() {
		return List.of(
				new Ball(new P2d(-0.2, -0.75), 0.03, 2.0, new V2d(0, 0), 0),
				new Ball(new P2d(0.2, -0.75), 0.03, 2.0, new V2d(0, 0), 1)
		);
	}

	@Override
	public List<Ball> getSmallBalls() {
		return generateTriangle().stream()
			.map(p -> new Ball(p, BALL_RADIUS, BALL_MASS, new V2d(0, 0), -1))
			.toList();
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

	private static List<P2d> generateTriangle() {
		List<P2d> points = new ArrayList<>();
		Random rand = new Random(42);
		double centerDistance = 2 * BALL_RADIUS*1.1;
		double rowHeight = BALL_RADIUS * Math.sqrt(3.0)*1.1;

		for (int row = 0; row < NUM_LAYERS; row++) {
			double yOffset = row * rowHeight + rand.nextDouble() * 0.004 - 0.002; // Small random vertical offset
			double xStartOffset = -((row * centerDistance) / 2.0);

			for (int col = 0; col <= row; col++) {
				double xOffset = xStartOffset + (col * centerDistance) + rand.nextDouble() * 0.004 - 0.002;
				points.add(START_VERTEX.sum(new V2d(xOffset, yOffset)));
			}
		}

		return points;
	}
}
