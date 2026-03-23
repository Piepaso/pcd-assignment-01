package pcd.ass01.poool.model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoardMonitor {
	private final Board board;
	private BoardData boardData;

	private final int ballsThreadsNum;
	private int threadsFinished;
	private final Lock lock;
	private final Condition canRead;
	private volatile boolean updated;


	/*
	* Solitamente l'update viene chiamato molto più frequentemente della get
	* Se fossero 2 metodi syncronized il get farebbe fatica a prendere la lock
	* Quando una get viene chiamata, non appena l'update corrente finisce il prossimo si blocca per lasciargli la lock
	* Con updated si assicura che la get attenda almeno un update prima di essere rieseguita,
	* nel caso il rendere dovesse essere molto veloce
	*/
	public BoardMonitor(Board board, int ballsThreadsNum) {
		this.board = board;
		this.boardData = board.getData();
		this.ballsThreadsNum = ballsThreadsNum;

		this.lock = new ReentrantLock();
		this.canRead = lock.newCondition();
		this.updated = false;
		this.threadsFinished = 0;
	}

	public void notifyCollisionResolved() {
		lock.lock();
		try {
			threadsFinished++;
			if (threadsFinished == ballsThreadsNum) {
				threadsFinished = 0;
				boardData = board.getData();
				updated = false;
				canRead.signal();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			lock.unlock();
		}
	}

	public BoardData getUpdatedBoardData() {
		lock.lock();
		try {
			while (updated) {
				canRead.await();
			}
			updated = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			lock.unlock();
		}
		return boardData;
	}
}
