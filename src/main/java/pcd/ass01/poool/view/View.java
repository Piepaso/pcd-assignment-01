package pcd.ass01.poool.view;


import pcd.ass01.poool.controller.ActiveController;

public class View {

	private ViewFrame frame;
	
	public View(ViewModel model, ActiveController controller) {
		frame = new ViewFrame(model, controller, 1200, 1200);
		frame.setVisible(true);
	}
		
	public void render() {
		frame.render();
	}
}
