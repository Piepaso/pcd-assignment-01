package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.board.Kick;
import pcd.ass01.poool.model.board.P2d;

import java.util.HashMap;
import java.util.Map;

public class CmdMonitor {

	private long pressedTime = -1;
	private final int mousePlayerId;
	private final Map<Integer, Kick> kicks = new HashMap<>();

	public CmdMonitor(int humanPlayerId) {
		this.mousePlayerId = humanPlayerId;
	}

	public synchronized void mousePressed(long time) {
		this.pressedTime = time;
	}

	public synchronized void mouseReleased(P2d position, long time) {
		if (pressedTime != -1) {
			kicks.put(mousePlayerId, new Kick(position, pressedTime, time));
			pressedTime = -1;
		}
	}

	public synchronized void botKick(int playerId, P2d position, double strength) {
		kicks.put(playerId, new Kick(position, strength));
	}

	public synchronized boolean isKickAvailable(int playerId) {
		return playerId >= 0 && kicks.containsKey(playerId);
	}

	public synchronized Kick consumeKick(int playerId) {
		Kick kick = kicks.remove(playerId);
		if (kick != null) {
			return kick;
		}
		throw new IllegalStateException("Kick not available for player: " + playerId);
	}
}