package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.Ball;
import pcd.ass01.poool.model.Boundary;

import java.util.List;

public interface BoardConf {

	Boundary getBoardBoundary();
	
	Ball getPlayerBall();
	
	List<Ball> getSmallBalls();
}
