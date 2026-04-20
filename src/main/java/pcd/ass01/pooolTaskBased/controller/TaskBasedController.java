package pcd.ass01.pooolTaskBased.controller;

import pcd.ass01.poool.controller.Cmd;
import pcd.ass01.poool.controller.CmdController;
import pcd.ass01.poool.util.BoundedBuffer;
import pcd.ass01.poool.util.BoundedBufferImpl;
import pcd.ass01.pooolTaskBased.task.Task;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class TaskBasedController implements CmdController {
    private final BoundedBuffer<Cmd> cmdBuffer;

    public TaskBasedController() {
        cmdBuffer = new BoundedBufferImpl<>(20);
    }

    public void notifyNewCmd(Cmd cmd) {
        try {
            cmdBuffer.put(cmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void executeCommand() {
        try {
            cmdBuffer.get().;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

