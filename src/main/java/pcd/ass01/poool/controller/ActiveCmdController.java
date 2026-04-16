package pcd.ass01.poool.controller;

import pcd.ass01.poool.util.BoundedBuffer;
import pcd.ass01.poool.util.BoundedBufferImpl;

public class ActiveCmdController extends Thread implements CmdController {

	private BoundedBuffer<Cmd> cmdBuffer;
	private CmdMonitor cmdMonitor;
	
	public ActiveCmdController(CmdMonitor cmdMonitor) {
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

	@Override
	public void notifyNewCmd(Cmd cmd) {
		try {
			cmdBuffer.put(cmd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
