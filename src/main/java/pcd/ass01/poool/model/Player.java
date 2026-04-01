package pcd.ass01.poool.model;

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
        return score;
    }

    public Ball ball() {
        return ball;
    }

    public int id() {
        return ball.getPlayerId();
    }

}
