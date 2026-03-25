package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.Kick;
import pcd.ass01.poool.model.P2d;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CmdMonitor {

	private final Lock lock = new ReentrantLock();

	private long pressedTime = -1;
	private Kick kick;
	private volatile boolean kickAvailable;

	public void mousePressed(long time) {
		lock.lock();
		try {
			pressedTime = time;
		} finally {
			lock.unlock();
		}
	}

	public void mouseReleased(P2d position, long time) {
		lock.lock();

		try {
			if (pressedTime != -1) {
				kick = new Kick(position, pressedTime, time);
				kickAvailable = true;
				pressedTime = -1;
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean isKickAvailable() {  // can ask without locking for better performance
		return kickAvailable;
	}

	public Kick consumeKick() {
		lock.lock();
		try {
			if (kickAvailable) {
				kickAvailable = false;
				return kick;
			} else {
				throw new IllegalStateException("Kick not available");
			}
		} finally {
			lock.unlock();
		}
	}

}
