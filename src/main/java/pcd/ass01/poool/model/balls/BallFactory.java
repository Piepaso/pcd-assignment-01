package pcd.ass01.poool.model.balls;

import pcd.ass01.poool.configuration.PlayerType;
import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BallFactory {

    private int id = 0;
    private int mouseId = 0;
    private final List<Integer> botIds = new ArrayList<>();

    public Ball getSmallBall(P2d pos, double mass, double radius, V2d vel) {
        return new SmallBall(pos, mass, radius, vel);
    }

    public Ball getPlayerBall(P2d pos, double mass, double radius, V2d vel, PlayerType type) {
        switch (type) {
            case BOT:
                botIds.add(id);
                break;
            case MOUSE:
                this.mouseId = id;
                break;
            default:
                break;
        }
        return new PlayerBall(pos, mass, radius, vel, id++);
    }

    public List<Integer> getBotIds() {
        return Collections.unmodifiableList(new ArrayList<Integer>(botIds));
    }

    public int getMousePlayerId() {
        return mouseId;
    }
}
