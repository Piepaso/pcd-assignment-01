package pcd.ass01.poool.model.balls;

import pcd.ass01.poool.model.board.Kick;
import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.board.V2d;

import static pcd.ass01.poool.configuration.StaticConf.MAX_KICK_STRENGTH;

public class PlayerBall extends AbstractBall {

    private final int id;

    protected PlayerBall(P2d pos, double radius, double mass, V2d vel, int id) {
        super(pos, radius, mass, vel);
        this.id = id;
    }

    @Override
    protected void updateLastCollisionPlayerId() {}

    @Override
    void updateImpContributions(int otherId, double imp) {}

    @Override
    public int getPlayerId() {
        return id;
    }

    @Override
    public int getLastCollisionPlayerId() {
        return id;
    }

    @Override
    protected V2d getKickVector(Kick kick) {
        if (getVel().abs() == 0)
            return new V2d(kick.position(), getPos()).getNormalized().mul(Math.min(kick.strength(), MAX_KICK_STRENGTH));
        else
            return new V2d(0, 0);
    }
}
