package pcd.ass01.pooolTaskBased.controller;
import pcd.ass01.poool.controller.CmdController;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.controller.Cmd;
import pcd.ass01.poool.util.BoundedBuffer;
import pcd.ass01.poool.util.BoundedBufferImpl;

public class TaskBasedController implements CmdController<Cmd> {
    private final BoundedBuffer<Cmd> cmdBuffer;
    private final CmdMonitor cmdMonitor;
    private int currentSize;

    public TaskBasedController(CmdMonitor cmdMonitor) {
        cmdBuffer = new BoundedBufferImpl<>(20);
        this.cmdMonitor = cmdMonitor;
        this.currentSize = 0;
    }

    public void notifyNewCmd(Cmd cmd) {
        try {
            cmdBuffer.put(cmd);
            currentSize++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void executeCommand() {
        try {
            if (currentSize > 0) {
                var cmd = cmdBuffer.get();
                currentSize--;
                cmd.execute(cmdMonitor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

