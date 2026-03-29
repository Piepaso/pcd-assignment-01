package pcd.ass01.poool.view;

public class RenderMonitor {
	public synchronized void await() {
		try {
			wait();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public synchronized void signal() {
		notifyAll();
	}
}
