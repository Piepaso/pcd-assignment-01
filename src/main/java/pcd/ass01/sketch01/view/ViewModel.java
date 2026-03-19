package pcd.ass01.sketch01.view;

import pcd.ass01.sketch01.model.Board;
import pcd.ass01.sketch01.model.BoardData;
import pcd.ass01.sketch01.model.P2d;
import pcd.ass01.sketch01.model.V2d;

import java.util.ArrayList;

record BallViewInfo(P2d pos, double radius, V2d vel) {}

public class ViewModel {

	private ArrayList<BallViewInfo> balls;
	private BallViewInfo player;
	private long startTime;
	
	public ViewModel() {
		balls = new ArrayList<BallViewInfo>();
	}
	
	public void init() {
		startTime = System.currentTimeMillis();
	}
	
	public void update(BoardData board) {
		balls.clear();
		for (var b: board.balls()) {
			balls.add(new BallViewInfo(b.pos(), b.radius(), b.vel()));
		}
		var p = board.playerBall();
		player = new BallViewInfo(p.pos(), p.radius(), p.vel());
	}
	
	public ArrayList<BallViewInfo> getBalls(){
		return balls;
	}
	
	public BallViewInfo getPlayerBall() {
		return player;
	}
	
	public long getStartTime() {
		return startTime;
	}
}
