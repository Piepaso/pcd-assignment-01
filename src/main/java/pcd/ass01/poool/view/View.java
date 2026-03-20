package pcd.ass01.poool.view;


public class View {

	private ViewFrame frame;
	
	public View(ViewModel model) {
		frame = new ViewFrame(model, 1200, 1200);
		frame.setVisible(true);
	}
		
	public void render() {
		frame.render();
	}
}
