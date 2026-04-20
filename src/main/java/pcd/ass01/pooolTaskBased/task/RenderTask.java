package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.view.View;

public class RenderTask implements Task<Void> {
    private final View view;

    public RenderTask(View view) {
        this.view = view;
    }

    @Override
    public Void call() {
        view.render();
        return null;
    }
}

