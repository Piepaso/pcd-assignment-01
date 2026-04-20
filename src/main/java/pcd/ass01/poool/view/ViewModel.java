package pcd.ass01.poool.view;

import pcd.ass01.poool.model.board.Hole;
import pcd.ass01.poool.model.dto.BallData;
import pcd.ass01.poool.model.dto.BoardData;
import pcd.ass01.poool.model.dto.PlayerData;

import java.util.List;

public class ViewModel {

	private final List<Hole> holes;
	private final int humanPlayerId;
	private BoardData boardData;

	private int engineFPS = 0;
	private int viewFPS = 0;
	
	public ViewModel(BoardData board, List<Hole> holes, int humanPlayerId) {
		this.holes = holes;
		this.humanPlayerId = humanPlayerId;
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
	
	public List<PlayerData> getPlayers() {
		return boardData.players();
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

	public void setEngineFPS(int frameCounter) {
		engineFPS = frameCounter;
	}

	public void setViewFPS(int frameCounter) {
		viewFPS = frameCounter;
	}

	public int getHumanPlayerId() {
		return humanPlayerId;
	}

}
