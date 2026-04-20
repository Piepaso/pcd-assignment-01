package pcd.ass01.poool.controller;

public interface CmdController<C> {
    void notifyNewCmd(C cmd);
}
