package pcd.ass01.poool.view;

import pcd.ass01.poool.model.BallData;
import pcd.ass01.poool.model.BoardData;

public class ViewModel {

	private BoardData boardData;
	private long startTime;
	private int engineFPS = 0;
	private int viewFPS = 0;
	
	public ViewModel() {
	}
	
	public void init(BoardData board) {
		boardData = board;
		startTime = System.currentTimeMillis();
	}
	
	public void update(BoardData board) {
		boardData = board;
	}
	
	public BallData[] getBalls(){
		return boardData.balls();
	}
	
	public BallData getPlayerBall() {
		return boardData.playerBall();
	}

	public int getEngineFPS() {
		return engineFPS;
	}

	public int getViewFPS() {
		return viewFPS;
	}

	public long getStartTime() {
		return startTime;
	}

	public void updateEngineFPS(Integer integer) {
		engineFPS = integer;
	}

	public void updateViewFPS(int frameCounter) {
		viewFPS = frameCounter;
	}
}
