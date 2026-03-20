package pcd.ass01.sketch02.controller;

import pcd.ass01.sketch02.model.Counter;

public class ResetCmd implements Cmd {

	@Override
	public void execute(Counter c) {
		c.reset();
	}

}
