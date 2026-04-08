package pcd.ass01.poool.model;

import pcd.ass01.poool.model.balls.Ball;

public class Player {

    private final Ball ball;
    private int score;

    public Player(Ball ball) {
        this.ball = ball;
    }

    public void incrementScore(int score) {
        this.score += score;
    }

    public int score() {
        if (!ball.isInHole()) {
            return score;
        }
        return 0;
    }

    public Ball ball() {
        return ball;
    }

    public int id() {
        return ball.getPlayerId();
    }

}
