package pcd.ass01.poool.view;

public class viewFpsCounter {
	private long lastTime;
	private int frames;
	private int currentFPS;

	public viewFpsCounter() {
		this.lastTime = System.currentTimeMillis();
		this.frames = 0;
		this.currentFPS = 0;
	}

	public void tick() {
		frames++;
		long currentTime = System.currentTimeMillis();

		if (currentTime - lastTime >= 1000) {
			currentFPS = frames;
			frames = 0;
			lastTime = currentTime;
		}
	}

	public int getFPS() {
		return currentFPS;
	}
}
