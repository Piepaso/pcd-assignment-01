package pcd.ass01.poool.model;

import java.util.List;

public interface BoardConf {

	Boundary getBoardBoundary();
	
	Ball getPlayerBall();
	
	List<Ball> getSmallBalls();
}
