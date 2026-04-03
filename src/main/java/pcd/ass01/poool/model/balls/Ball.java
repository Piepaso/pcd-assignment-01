package pcd.ass01.poool.model.balls;

import pcd.ass01.poool.model.board.*;
import pcd.ass01.poool.model.dto.BallData;

import java.util.List;

public interface Ball {

    void setBounds(Boundary bounds);

    void setHoles(List<Hole> holes);

    void updateState(double dt);

    void resolveCollisionWith(BallData other);

    void updateAfterCollisions();

    P2d getPos();

    double getMass();

    V2d getVel();

    double getRadius();

    boolean isInHole();

    int getPlayerId();

    int getLastCollisionPlayerId();

    void applyKick(Kick kick);
}
