package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.model.board.Kick;
import pcd.ass01.pooolTaskBased.controller.TaskBasedController;

import java.util.Map;

public class ExecuteCommandsTask implements Task<Map<Integer, Kick>> {
    private final TaskBasedController controller;

    public ExecuteCommandsTask(TaskBasedController controller) {
        this.controller = controller;
    }

    @Override
    public Map<Integer, Kick> call() {
        return null;
    }
}

