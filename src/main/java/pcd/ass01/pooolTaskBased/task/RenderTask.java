package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.view.View;

public record RenderTask(View view) implements Task<Void> {

    @Override
    public Void call() {
        view.render();
        return null;
    }
}

