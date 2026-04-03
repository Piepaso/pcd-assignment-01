package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.board.Boundary;
import pcd.ass01.poool.model.board.Hole;

import java.util.List;

public interface BoardConf {

	Boundary getBoardBoundary();
	
	List<Ball> getPlayerBall();
	
	List<Ball> getSmallBalls();

	List<Hole> getHoles();

	int getWinScore();
}
