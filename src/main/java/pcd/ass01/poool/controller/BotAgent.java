package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.board.P2d;

import java.util.Random;

public class BotAgent extends Thread {

	private final CmdMonitor cmdMonitor;
	private final long delay;
	private final int id;

	public BotAgent(CmdMonitor cmdMonitor, long delay, int botId) {
		this.cmdMonitor = cmdMonitor;
		this.delay = delay;
		this.id = botId;
	}

	@Override
	public void run() {
		var random = new Random();

		while (true) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cmdMonitor.botKick(id, new P2d(random.nextDouble(-1, 1), random.nextDouble(-1, 1)), random.nextDouble(0, 3));
		}
	}
}
