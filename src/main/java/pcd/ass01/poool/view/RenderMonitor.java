package pcd.ass01.poool.view;

public class RenderMonitor {
	public synchronized void await() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			notifyAll();
		}
	}

	public synchronized void signal() {
		notifyAll();
	}
}
