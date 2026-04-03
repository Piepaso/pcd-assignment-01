package pcd.ass01.poool.model.balls;

import pcd.ass01.poool.model.board.*;
import pcd.ass01.poool.model.dto.BallData;

import java.util.*;

import static pcd.ass01.poool.configuration.StaticConf.*;

abstract public class AbstractBall implements Ball {

    private final double radius;
    private final double mass;

    private P2d pos;
    private V2d vel;

    private volatile boolean inHole;

    private Boundary bounds;
    private List<Hole> holes;
    private V2d posIncrease;
    private V2d velIncrease;
    private int numCollisions;

    public AbstractBall(P2d pos, double radius, double mass, V2d vel) {
        this.pos = pos;
        this.radius = radius;
        this.mass = mass;
        this.vel = vel;

        posIncrease = new V2d(0,0);
        velIncrease = new V2d(0,0);
        numCollisions = 0;
        inHole = false;
    }

    @Override
    public void setBounds(Boundary bounds) {
        this.bounds = bounds;
    }

    @Override
    public void setHoles(List<Hole> holes) {
        this.holes = holes;
    }

    @Override
    public void updateState(double dt) {
        double speed = vel.abs();
        if (speed > MIN_SPEED_NOT_ZERO) {
            double dec    = FRICTION_FACTOR * dt;
            double factor = Math.max(0, speed - dec) / speed;
            vel = vel.mul(factor);
        } else {
            vel = new V2d(0,0);
        }
        pos = pos.sum(vel.mul(dt));
        applyBoundaryConstraints();
    }
    private void applyBoundaryConstraints(){
        if (pos.x() + radius > bounds.x1()){
            pos = new P2d(bounds.x1() - radius, pos.y());
            vel = vel.getSwappedX();
        } else if (pos.x() - radius < bounds.x0()){
            pos = new P2d(bounds.x0() + radius, pos.y());
            vel = vel.getSwappedX();
        } else if (pos.y() + radius > bounds.y1()){
            pos = new P2d(pos.x(), bounds.y1() - radius);
            vel = vel.getSwappedY();
        } else if (pos.y() - radius < bounds.y0()){
            pos = new P2d(pos.x(), bounds.y0() + radius);
            vel = vel.getSwappedY();
        }
    }

    @Override
    public void resolveCollisionWith(BallData other) {
        /* check if there is a collision */

        double dx   = other.pos().x() - pos.x();
        double dy   = other.pos().y() - pos.y();
        double dist = Math.hypot(dx, dy);
        double minD = radius + other.radius();


        if (dist < minD && dist > 1e-6)  {

            double nx = dx / dist;
            double ny = dy / dist;

            double overlap = minD - dist;
            double totalM  = mass + other.mass();

            double a_factor = overlap * (other.mass() / totalM);
            posIncrease = posIncrease.sum(new V2d(- nx * a_factor, - ny * a_factor));

            /* Update velocities  */
            /* relative speed along the normal vector*/

            double dvx = other.vel().x() - vel.x();
            double dvy = other.vel().y() - vel.y();
            double dvn = dvx * nx + dvy * ny;

            if (dvn <= 0) { /* if not already separating, update velocities */
                double imp = -(1 + RESTITUTION_FACTOR) * dvn / (1.0/mass + 1.0/other.mass());
                velIncrease = new V2d(- (imp / mass) * nx, - (imp / mass) * ny);
                updateImpContributions(other.lastCollisionPlayerId(), imp);
                numCollisions++;
            }
        }
    }

    @Override
    public void updateAfterCollisions() {
        pos = pos.sum(posIncrease);
        if (numCollisions > 0) {
            vel = vel.sum(velIncrease.mul(1.0 / numCollisions));
        }
        updateLastCollisionPlayerId();
        posIncrease = new V2d(0,0);
        velIncrease = new V2d(0,0);
        numCollisions = 0;


        for (Hole hole : holes) {
            if (hole.isIn(pos)) {
                inHole = true;
                break;
            }
        }
    }

    abstract protected void updateLastCollisionPlayerId();

    abstract void updateImpContributions(int otherId, double imp);

    @Override
    public P2d getPos(){
        return pos;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public V2d getVel() {
        return vel;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isInHole() {
        return inHole;
    }

    public abstract int getPlayerId();

    public abstract int getLastCollisionPlayerId();

    protected abstract V2d getKickVector(Kick kick);

    @Override
    public void applyKick(Kick kick) {
        vel = vel.sum(getKickVector(kick));
    }
}
