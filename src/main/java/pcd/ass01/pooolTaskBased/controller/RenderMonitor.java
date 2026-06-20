package pcd.ass01.pooolTaskBased.controller;

public class RenderMonitor {

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
