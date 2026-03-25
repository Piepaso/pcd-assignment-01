package pcd.ass01.poool.controller;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
	private final int threadsNum;
	private int countArrived;
	private int generation;
	private final Runnable lastThreadActions;

	private final Lock lock;
	private final Condition allArrived;

	public Barrier(int threadsNum, Runnable lastThreadActions) {
		this.threadsNum = threadsNum;
		this.countArrived = 0;
		this.generation = 0;
		this.lastThreadActions = lastThreadActions;

		this.lock = new ReentrantLock();
		this.allArrived = lock.newCondition();
	}

	public void await() {
		lock.lock();
		try {
			int currentGen = generation;
			countArrived++;

			if (countArrived == threadsNum) {
				lastThreadActions.run();
				countArrived = 0;
				generation++;
				allArrived.signalAll();
			} else {
				while (currentGen == generation) {
					allArrived.await();
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	public int getGeneration() {
		lock.lock();
		try {
			return generation;
		} finally {
			generation = 0;
			lock.unlock();
		}
	}
}