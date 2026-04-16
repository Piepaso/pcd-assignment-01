package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.pooolTaskBased.controller.TaskBasedController;

public class ExecuteCommandsTask implements Task {
    private final TaskBasedController controller;

    public ExecuteCommandsTask(TaskBasedController controller) {
        this.controller = controller;
    }

    @Override
    public Void call() {
        controller.executePendingCommands();
        return null;
    }
}

