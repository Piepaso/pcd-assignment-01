package pcd.ass01.poool.configuration;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StaticConf {

	public final static boolean KICK_ONLY_WHEN_BALL_IS_STOPPED = false;
	public final static boolean SCORE_ONLY_DIRECT_COLLISION = true;

	public final static double FRICTION_FACTOR = 0.02;
	public final static double RESTITUTION_FACTOR = 1.0;
	public final static double MIN_SPEED_NOT_ZERO = 0.0001;
	public final static double MAX_KICK_STRENGTH = 3.0;

	public final static double AGENTS_ITERATIONS = 5;

	public static final Map<Integer, Color> PLAYER_COLORS;

	static {
		Map<Integer, Color> playerColors = new HashMap<Integer, Color>();
		playerColors.put(0, Color.YELLOW);
		playerColors.put(1, Color.RED);
		playerColors.put(2, Color.CYAN);
		PLAYER_COLORS = Collections.unmodifiableMap(playerColors);
	}
}
