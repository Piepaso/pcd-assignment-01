package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.board.Kick;

import java.util.List;

public class UpdateBallsTask implements Task<Void> {
    private final Ball ball;
    private final double dt;
    private final Kick kick;

    public UpdateBallsTask(Ball ball, double dt, Kick kick) {
        this.ball = ball;
        this.dt = dt;
        this.kick = kick;
    }

    @Override
    public Void call() {
        ball.updateState(dt);
        if (kick != null) {
            ball.applyKick(kick);
        }
        return null;
    }
}

