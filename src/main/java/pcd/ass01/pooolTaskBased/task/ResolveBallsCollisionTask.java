package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.dto.BallData;

import java.util.List;

public class ResolveBallsCollisionTask implements Task {
    private final Ball ball;
    private final List<BallData> snapshot;

    public ResolveBallsCollisionTask(Ball ball, List<BallData> snapshot) {
        this.ball = ball;
        this.snapshot = snapshot;
    }

    @Override
    public Void call() {
        for (BallData other : snapshot) {
            ball.resolveCollisionWith(other);
        }
        ball.updateAfterCollisions();
        return null;
    }
}
