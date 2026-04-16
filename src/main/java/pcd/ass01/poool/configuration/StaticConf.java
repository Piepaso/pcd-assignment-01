package pcd.ass01.poool.configuration;

import java.awt.*;
import java.util.Map;

public class StaticConf {

	public final static boolean KICK_ONLY_WHEN_BALL_IS_STOPPED = true;
	public final static boolean SCORE_ONLY_DIRECT_COLLISION = false;

	public final static double FRICTION_FACTOR = 0.02;
	public final static double RESTITUTION_FACTOR = 1.0;
	public final static double MIN_SPEED_NOT_ZERO = 0.0001;
	public final static double MAX_KICK_STRENGTH = 3.0;

	public final static Map<Integer, Color> PLAYER_COLORS = Map.of(
			0, Color.YELLOW,
			1, Color.RED,
			2, Color.CYAN
	);
}
