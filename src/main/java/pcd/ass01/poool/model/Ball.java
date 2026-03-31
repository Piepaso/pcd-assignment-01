package pcd.ass01.poool.model;

import java.util.*;

public class Ball {

    private final static double FRICTION_FACTOR = 0.02; // 0.01 min 0.02 buono
    private final static double RESTITUTION_FACTOR = 1.0;
    private final static double MIN_SPEED = 0.0001;

    private final double radius;
    private final double mass;
    private final int playerId;

    private P2d pos;
    private V2d vel;

    private int lastCollisionPlayerId;
    private volatile boolean inHole;

    private Boundary bounds;
    private List<Hole> holes;
    private V2d posIncrease;
    private V2d velIncrease;
    private int numCollisions;
    private final double[] impContributions;
    
    public Ball(P2d pos, double radius, double mass, V2d vel, int playerId) {
       this.playerId = playerId;
       this.pos = pos;
       this.radius = radius;
       this.mass = mass;
       this.vel = vel;

       posIncrease = new V2d(0,0);
       velIncrease = new V2d(0,0);
       lastCollisionPlayerId = playerId;
       numCollisions = 0;
       inHole = false;
       impContributions = new double[10];
    }

    public void setBounds(Boundary bounds) {
    	this.bounds = bounds;
    }

    public void setHoles(List<Hole> holes) {
    	this.holes = holes;
    }

    public void updateState(double dt) {
        double speed = vel.abs();
    	if (speed > MIN_SPEED) {
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
                var otherPlayerId = other.lastCollisionPlayerId();
                if (!isPlayer() && otherPlayerId >= 0) {
                    impContributions[otherPlayerId] += imp;
                }
                numCollisions++;
            }
        }
    }

    public void updateAfterCollisions() {
    	pos = pos.sum(posIncrease);
        if (numCollisions > 0) {
            vel = vel.sum(velIncrease.mul(1.0 / numCollisions));
        }
    	posIncrease = new V2d(0,0);
        velIncrease = new V2d(0,0);

        if (!isPlayer()) {
            int maxIdx = lastCollisionPlayerId;
            double max = 0;

            for (int i = 0; i < impContributions.length; i++) {
                if (impContributions[i] > max) {
                    maxIdx = i;
                    max = impContributions[i];
                }
                impContributions[i] = 0;
            }
            lastCollisionPlayerId = maxIdx;
        }

        numCollisions = 0;

        for (Hole hole : holes) {
        	if (hole.isIn(pos)) {
        		inHole = true;
        		break;
        	}
        }
    }

    
    public P2d getPos(){        
    	return pos;
    }
    
    public double getMass() {
    	return mass;
    }
    
    public V2d getVel() {
    	return vel;
    }
    
    public double getRadius() {
    	return radius;
    }

    public boolean isPlayer() {
    	return playerId >= 0;
    }

    public boolean isInHole() {
    	return inHole;
    }

    public int getPlayerId() {
        if (isPlayer()) {
            return playerId;
        } else {
            throw new IllegalStateException("Only player ball has a player id");
        }
    }

    public int getLastCollisionPlayerId() {
        return lastCollisionPlayerId;
    }

    public void applyKick(Kick kick) {
        if (isPlayer()) {
            if (vel.abs() == 0)
                vel = new V2d(kick.position(), pos).getNormalized().mul(Math.min(kick.strength(), 3.0));
        } else {
            throw new IllegalStateException("Only player ball can be kicked");
        }
    }

}
