package pcd.ass01.sketch01.model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoardMonitor {
	private final Board board;
	private BoardData boardData;

	private final Lock lock;
	private final Condition canUpdate;
	private final Condition canRead;
	private volatile boolean updated;
	private volatile boolean needToRead;

	public BoardMonitor(Board board) {
		this.board = board;
		this.boardData = board.getData();

		this.lock = new ReentrantLock();
		this.canUpdate = lock.newCondition();
		this.canRead = lock.newCondition();
		this.updated = true;
		this.needToRead = false;
	}

	public void updateBoard(double dt) {
		lock.lock();
		try {
			while(needToRead && !updated) {
				canUpdate.await();
			}
			board.updateState(dt);
			updated = false;
			canRead.signal();
		} catch (Exception ignored) {}
		finally {
			lock.unlock();
		}
	}

	public BoardData getBoard() {
		needToRead = true;
		lock.lock();
		try {
			while (updated) {
				canRead.await();
			}
			boardData = board.getData();
			needToRead = false;
			updated = true;
			canUpdate.signalAll();
		} catch (Exception ignored) {}
		finally {
			lock.unlock();
		}

		return boardData;
	}
}
