package pcd.ass01.poool.model;

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


	/*
	* Solitamente l'update viene chiamato molto più frequentemente della get
	* Se fossero 2 metodi syncronized il get farebbe fatica a prendere la lock
	* Quando una get viene chiamata, non appena l'update corrente finisce il prossimo si blocca per lasciargli la lock
	* Con updated si assicura che la get attenda almeno un update prima di essere rieseguita,
	* nel caso il rendere dovesse essere molto veloce
	*/
	public BoardMonitor(Board board) {
		this.board = board;
		this.boardData = board.getData();

		this.lock = new ReentrantLock();
		this.canUpdate = lock.newCondition();
		this.canRead = lock.newCondition();
		this.updated = false;
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

	public BoardData getUpdatedBoardData() {
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
