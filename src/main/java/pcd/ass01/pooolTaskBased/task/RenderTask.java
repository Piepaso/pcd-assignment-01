package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.pooolTaskBased.controller.RenderMonitor;
import pcd.ass01.poool.view.View;

import javax.swing.*;

public record RenderTask(View view, RenderMonitor renderSemafore) implements Task<Void> {

    @Override
    public Void call() {
        if (renderSemafore.isRendering()) {
            return null;
        }
        renderSemafore.signalRenderStart();
        view.render();
        SwingUtilities.invokeLater(renderSemafore::signalRenderDone);
        return null;
    }
}

