package pcd.ass01.sketch02.controller;

import pcd.ass01.sketch02.model.Counter;

public interface Cmd {
	
	void execute(Counter c);
}
