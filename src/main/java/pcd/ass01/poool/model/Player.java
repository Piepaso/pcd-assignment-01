package pcd.ass01.poool.model;

public class Player {

    private final int id;
    private final Ball ball;
    private int score;

    public Player(int id, Ball ball) {
        this.id = id;
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
        return id;
    }

}
