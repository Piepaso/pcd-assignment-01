package pcd.ass01.pooolTaskBased.task;

import pcd.ass01.poool.model.balls.Ball;
import pcd.ass01.poool.model.dto.BallData;

import java.util.List;

public record ResolveBallsCollisionTask(Ball ball, List<BallData> snapshot) implements Task<Void> {

    @Override
    public Void call() {
        for (BallData other : snapshot) {
            ball.resolveCollisionWith(other);
        }
        ball.updateAfterCollisions();
        return null;
    }
}
