package pcd.ass01.sketch02;

import pcd.ass01.sketch02.controller.*;
import pcd.ass01.sketch02.model.AutonomousUpdater;
import pcd.ass01.sketch02.model.Counter;
import pcd.ass01.sketch02.util.*;
import pcd.ass01.sketch02.view.View;
import pcd.ass01.sketch02.view.ViewModel;

public class Sketch02 {

	
	public static void main(String[] argv) {

		var model = new Counter(0);
		
		var controller = new ActiveController(model);				
		
		var viewModel = new ViewModel(model.getCount());
		var view = new View(viewModel, controller);
		model.addObserver(view);
		
		controller.start();
		
		var updater = new AutonomousUpdater(model);
		updater.start();		
		
		view.display();

	}
	
}
