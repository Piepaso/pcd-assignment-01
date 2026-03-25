package pcd.ass01.poool.controller;


public class PressedCmd implements Cmd {

	private final long time;

	public PressedCmd(long time) {
		this.time = time;
	}

	@Override
	public void execute(CmdMonitor cmdMonitor) {
		cmdMonitor.mousePressed(time);
	}

	@Override
	public String toString() {
		return "PressedCmd{" +
				"time=" + time +
				'}';
	}
}
