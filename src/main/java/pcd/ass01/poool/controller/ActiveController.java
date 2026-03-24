package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.CmdMonitor;
import pcd.ass01.poool.util.BoundedBuffer;
import pcd.ass01.poool.util.BoundedBufferImpl;

public class ActiveController extends Thread {

	private BoundedBuffer<Cmd> cmdBuffer;
	private CmdMonitor cmdMonitor;
	
	public ActiveController(CmdMonitor cmdMonitor) {
		this.cmdBuffer = new BoundedBufferImpl<>(10);
		this.cmdMonitor = cmdMonitor;
	}
	
	public void run() {
		while (true) {
			try {
				var cmd = cmdBuffer.get();
				cmd.execute(cmdMonitor);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void notifyNewCmd(Cmd cmd) {
		try {
			log("new cmd notified: " + cmd);
			cmdBuffer.put(cmd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void log(String msg) {
		System.out.println("[ " + System.currentTimeMillis() + "][ Controller ] " + msg);
	}
}
