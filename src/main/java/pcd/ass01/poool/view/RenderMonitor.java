package pcd.ass01.poool.view;

public class RenderMonitor {

	private volatile boolean renderDone = false;

	public synchronized void await() {
		try {
			while (!renderDone) {
				wait();
			}
			renderDone = false;
		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
			notifyAll();
		}
	}

	public synchronized void signal() {
		renderDone = true;
		notifyAll();
	}
}
