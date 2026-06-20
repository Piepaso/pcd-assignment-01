package pcd.ass01.poool.view;

public class RenderMonitor {

	private volatile int renderDone = 0;

	public synchronized void await() {
		try {
			while (renderDone == 0) {
				wait();
			}
			renderDone--;
		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
	}

	public synchronized void signal() {
		renderDone++;
		notify();
	}
}
