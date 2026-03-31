package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.Kick;
import pcd.ass01.poool.model.P2d;

public class CmdMonitor {

	private long pressedTime = -1;
	private final Kick[] kicks = new Kick[10];

	public synchronized void mousePressed(long time) {
		this.pressedTime = time;
	}

	public synchronized void mouseReleased(P2d position, long time) {
		if (pressedTime != -1) {
			kicks[0] = new Kick(position, pressedTime, time);
			pressedTime = -1;
		}
	}

	public synchronized void botKick(int playerId, P2d position, double strength) {
		kicks[playerId] = new Kick(position, strength);
	}

	public synchronized boolean isKickAvailable(int playerId) {
		return kicks[playerId] != null;
	}

	public synchronized Kick consumeKick(int playerId) {
		if (kicks[playerId] != null) {
			Kick k = kicks[playerId];
			kicks[playerId] = null;
			return k;
		} else {
			throw new IllegalStateException("Kick not available for player: " + playerId);
		}
	}
}