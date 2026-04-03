package pcd.ass01.poool.controller;

import pcd.ass01.poool.model.board.P2d;


public class ReleasedCmd implements Cmd {

	private final long time;
	private final P2d position;

	public ReleasedCmd(long time, P2d position) {
		this.time = time;
		this.position = position;
	}

	@Override
	public void execute(CmdMonitor cmdMonitor) {
		cmdMonitor.mouseReleased(position, time);
	}

	@Override
	public String toString() {
		return "MouseCmd{" +
				"time=" + time +
				", position=" + position +
				'}';
	}
}
