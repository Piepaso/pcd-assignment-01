package pcd.ass01.poool.view;


import pcd.ass01.poool.controller.ActiveCmdController;
import pcd.ass01.poool.controller.CmdController;

public class View {

	private final ViewFrame frame;
	
	public View(ViewModel model, CmdController controller) {
		frame = new ViewFrame(model, controller, 1000, 1000);
		frame.setVisible(true);
	}
		
	public void render() {
		frame.render();
	}
}
