package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.pooolTaskBased.controller.TaskBasedController;

public record ExecuteCommandsTask(TaskBasedController controller) implements Task<Void> {

    @Override
    public Void call() {
        controller.executeCommand();
        return null;
    }
}

