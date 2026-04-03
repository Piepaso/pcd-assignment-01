package pcd.ass01.poool.model;

import java.util.*;

public class SmallBall extends AbstractBall {

    private final Map<Integer, Double> impContributions;
    private int lastCollisionPlayerId;

    public SmallBall(P2d pos, double radius, double mass, V2d vel) {
        super(pos, radius, mass, vel);
        impContributions = new HashMap<>();
        lastCollisionPlayerId = -1;
    }

    @Override
    protected void updateLastCollisionPlayerId() {
        int maxIdx = getVel().abs() > 0 ? lastCollisionPlayerId : -1;
        double max = 0;

        for (var entry : impContributions.entrySet()) {
            if (entry.getValue() > max && entry.getKey() >= 0) {
                maxIdx = entry.getKey();
                max = entry.getValue();
            }
        }
        impContributions.clear();
        lastCollisionPlayerId = maxIdx;
    }

    @Override
    void updateImpContributions(int otherId, double imp) {
        impContributions.merge(otherId, imp, Double::sum);
    }


    @Override
    public int getPlayerId() {
        return -1;
    }

    @Override
    public int getLastCollisionPlayerId() {
        return lastCollisionPlayerId;
    }

    @Override
    protected V2d getKickVector(Kick kick) {
        return new V2d(0, 0);
    }

    @Override
    public void applyKick(Kick kick) {}

}
