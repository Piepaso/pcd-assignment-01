package pcd.ass01.poool.controller;

import pcd.ass01.poool.configuration.StaticConf;
import pcd.ass01.poool.model.board.P2d;

//import java.util.Random;

public class BotAgent extends Thread {

	private final CmdMonitor cmdMonitor;
	private final int id;

	public BotAgent(CmdMonitor cmdMonitor, int botId) {
		this.cmdMonitor = cmdMonitor;
		this.id = botId;
	}

	@Override
	public void run() {
		//Random random = new Random();
		int delay = 1000;

		//cmdMonitor.waitPlayerFirstMove();
		for (int i = 0; i < StaticConf.AGENTS_ITERATIONS; i++) {
			cmdMonitor.botKick(id,
					new P2d(-1 + 0.5 * 2, -1 + 0.5 * 2),
					delay / 1000.0);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//delay = 500 + random.nextInt(2500);
		}
	}
}
