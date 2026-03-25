package pcd.ass01.poool.view;

import pcd.ass01.poool.model.BallData;
import pcd.ass01.poool.model.BoardData;
import pcd.ass01.poool.model.Boundary;
import pcd.ass01.poool.model.Hole;

import java.util.List;

public class ViewModel {

	private List<Hole> holes;
	private BoardData boardData;

	private int engineFPS = 0;
	private int viewFPS = 0;
	
	public ViewModel() {
	}
	
	public void init(BoardData board, List<Hole> holes) {
		this.holes = holes;
		boardData = board;
	}
	
	public void update(BoardData board) {
		boardData = board;
	}

	public List<Hole> getHoles() {
		return holes;
	}
	
	public List<BallData> getBalls(){
		return boardData.balls();
	}
	
	public BallData getPlayerBall() {
		return boardData.playerBall();
	}

	public int getScore() {
		return boardData.score();
	}

	public boolean isGameOver() {
		return boardData.gameOver();
	}

	public int getEngineFPS() {
		return engineFPS;
	}

	public int getViewFPS() {
		return viewFPS;
	}

	public void updateEngineFPS(int frameCounter) {
		engineFPS = frameCounter;
	}

	public void updateViewFPS(int frameCounter) {
		viewFPS = frameCounter;
	}
}
