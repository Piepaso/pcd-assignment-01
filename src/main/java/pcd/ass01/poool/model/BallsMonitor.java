package pcd.ass01.poool.model;

import pcd.ass01.poool.configuration.BoardData;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BallsMonitor {

	private final Board board;
	private final int threadsNum;
	private final Lock lock;
	private final Condition newFrameStarted;
	private final Condition allUpdated;

	private int threadsUpdated;
	private int threadsFinished;

	private long lastUpdateTime;
	private double elapsed_sec;
	private int frameCounter;
	private volatile BoardData boardData;
	private volatile List<BallData> uncollisionedBallsData;
	private volatile boolean updated;

	public BallsMonitor(Board board, int threadsNum) {
		this.threadsNum = threadsNum;
		this.board = board;

		lock = new ReentrantLock();
		newFrameStarted = lock.newCondition();
		allUpdated = lock.newCondition();
		threadsUpdated = 0;
		lastUpdateTime = System.nanoTime();
		frameCounter = 0;
		updated = false;
	}

	public double waitForNextFrame() {
		lock.lock();
		try {
			if (threadsFinished == threadsNum) { // first thread on next frame resets counter
				threadsFinished = 0;
			}

			threadsFinished++;
			boolean last = threadsFinished == threadsNum;
			while (threadsFinished < threadsNum) {
				newFrameStarted.await();
			}
			if (last) {
				boardData = board.getData();
				updated = false;
				frameCounter++;

				elapsed_sec = (System.nanoTime() - lastUpdateTime) * 1e-9;
				lastUpdateTime = System.nanoTime();
				newFrameStarted.signalAll();
			}
			return elapsed_sec;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	public List<BallData> waitForUpdatedBalls() {
		lock.lock();
		try {
			if (threadsUpdated == threadsNum) { // first thread on next frame resets counter
				threadsUpdated = 0;
			}

			threadsUpdated++;
			boolean last = threadsUpdated == threadsNum;
			while (threadsUpdated < threadsNum) {
				allUpdated.await();
			}
			if (last) {
				uncollisionedBallsData = board.getData().balls();
				allUpdated.signalAll();
			}
			return uncollisionedBallsData;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	public BoardData getUpdatedBoardData() {
		lock.lock();
		try {
			while (updated) {
				newFrameStarted.await();
			}
			updated = true;
			return boardData;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			lock.unlock();
		}
	}

	public int getFrames() {
		lock.lock();
		try {
			return frameCounter;
		} finally {
			frameCounter = 0;
			lock.unlock();
		}
	}
}
