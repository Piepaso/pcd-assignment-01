package pcd.ass01.pooolTaskBased.controller;

public class RenderSemaphore {

	private volatile int renderDone = 0;

	public synchronized boolean isRendering() {
		return renderDone > 0;
	}

	public synchronized void signalRenderDone() {
		renderDone--;
	}

	public synchronized void signalRenderStart() {
		renderDone++;
	}
}
