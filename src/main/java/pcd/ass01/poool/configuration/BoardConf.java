package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.Ball;
import pcd.ass01.poool.model.Boundary;
import pcd.ass01.poool.model.Hole;

import java.util.List;

public interface BoardConf {

	Boundary getBoardBoundary();
	
	List<Ball> getPlayerBall();
	
	List<Ball> getSmallBalls();

	List<Hole> getHoles();
}
