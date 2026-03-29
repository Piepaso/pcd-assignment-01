package pcd.ass01.poool.view;


import pcd.ass01.poool.controller.ActiveController;

public class View {

	private ViewFrame frame;
	
	public View(ViewModel model, ActiveController controller, RenderMonitor renderMonitor) {
		frame = new ViewFrame(model, controller, renderMonitor, 1000, 1000);
		frame.setVisible(true);
	}
		
	public void render() {
		frame.render();
	}
}
