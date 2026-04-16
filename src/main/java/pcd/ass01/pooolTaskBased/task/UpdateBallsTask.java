package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.model.balls.Ball;

import java.util.List;

public class UpdateBallsTask implements Task {
    private final Ball ball;
    private final double dt;

    public UpdateBallsTask(Ball ball, double dt) {
        this.ball = ball;
        this.dt = dt;
    }

    @Override
    public Void call() {
        ball.updateState(dt);
        return null;
    }
}

