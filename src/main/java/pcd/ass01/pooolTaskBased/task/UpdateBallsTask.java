package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.board.Kick;

public record UpdateBallsTask(Ball ball, double dt, Kick kick) implements Task<Void> {

    @Override
    public Void call() {
        ball.updateState(dt);
        if (kick != null) {
            ball.applyKick(kick);
        }
        return null;
    }
}

