package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.view.View;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class RenderTask implements Task {
    private final View view;

    public RenderTask(View view) {
        this.view = view;
    }

    @Override
    public Void call() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(view::render);
        return null;
    }
}

