package pcd.ass01.poool.model.balls;

import pcd.ass01.poool.configuration.PlayerType;
import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;

import java.util.ArrayList;
import java.util.List;

public class BallFactory {

    private int id = 0;
    private int mouseId = 0;
    private List<Integer> bots = new ArrayList<>();

    public Ball getSmallBall(P2d pos, double mass, double radius, V2d vel) {
        return new SmallBall(pos, mass, radius, vel);
    }

    public Ball getPlayerBall(P2d pos, double mass, double radius, V2d vel, PlayerType type) {
        switch (type) {
            case BOT -> bots.add(id);
            case MOUSE -> this.mouseId = id;
        }
        return new PlayerBall(pos, mass, radius, vel, id++);
    }

    public List<Integer> getBots() {
        return List.copyOf(bots);
    }

    public int getMousePlayerId() {
        return mouseId;
    }
}
