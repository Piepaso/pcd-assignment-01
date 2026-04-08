package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.board.P2d;

import java.util.Random;

public class BotAgent extends Thread {

	private final CmdMonitor cmdMonitor;
	private final int id;

	public BotAgent(CmdMonitor cmdMonitor, int botId) {
		this.cmdMonitor = cmdMonitor;
		this.id = botId;
	}

	@Override
	public void run() {
		var random = new Random();
		int delay = random.nextInt(500, 3000);

		cmdMonitor.waitPlayerFirstMove();
		while (true) {
			cmdMonitor.botKick(id, new P2d(random.nextDouble(-1, 1), random.nextDouble(-1, 1)), delay/1000.0);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			delay = random.nextInt(500, 3000);
		}
	}
}
