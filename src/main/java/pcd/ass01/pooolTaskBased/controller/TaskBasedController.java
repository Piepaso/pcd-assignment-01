package pcd.ass01.pooolTaskBased.controller;
import pcd.ass01.poool.controller.CmdController;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.controller.Cmd;
import pcd.ass01.poool.util.BoundedBuffer;

public class TaskBasedController implements CmdController<Cmd> {
    private final BoundedBuffer<Cmd> cmdBuffer;
    private final CmdMonitor cmdMonitor;

    public TaskBasedController(CmdMonitor cmdMonitor) {
        cmdBuffer = new NonBlockingBoundedBuffer<>(20);
        this.cmdMonitor = cmdMonitor;
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
            var cmd = cmdBuffer.get();
            if (cmd != null) {
                cmd.execute(cmdMonitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

