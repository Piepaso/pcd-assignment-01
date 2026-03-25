package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.BoardData;
import pcd.ass01.poool.model.BallData;
import pcd.ass01.poool.model.Board;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoardMonitor {

	private final Barrier frameBarrier;
	private final Barrier updateBarrier;
	private final Lock viewLock;
	private final Condition newFrameReady;

	private long lastUpdateTime;
	private double elapsed_sec;
	private BoardData boardData;
	private List<BallData> uncollisionedBallsData;

	private boolean newFrameToRender;

	public BoardMonitor(Board board, int threadsNum) {

		viewLock = new ReentrantLock();
		newFrameReady = viewLock.newCondition();
		this.lastUpdateTime = System.nanoTime();
		this.newFrameToRender = false;

		this.frameBarrier = new Barrier(threadsNum, () -> {
			boardData = board.getData();
			elapsed_sec = (System.nanoTime() - lastUpdateTime) * 1e-9;
			lastUpdateTime = System.nanoTime();

			viewLock.lock();
			try {
				newFrameToRender = true;
				newFrameReady.signalAll();
			} finally {
				viewLock.unlock();
			}
		});

		this.updateBarrier = new Barrier(threadsNum, () -> uncollisionedBallsData = board.getData().balls());
	}

	public double waitForNextFrame() {
		frameBarrier.await();
		return elapsed_sec;
	}

	public List<BallData> waitForUpdatedBalls() {
		updateBarrier.await();
		return uncollisionedBallsData;
	}

	public BoardData getUpdatedBoardData() {
		viewLock.lock();
		try {
			while (!newFrameToRender) {
				newFrameReady.await();
			}
			newFrameToRender = false;
			return boardData;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} finally {
			viewLock.unlock();
		}
	}

	public int getFrames() {
		return frameBarrier.getGeneration();
	}
}