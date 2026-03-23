package pcd.ass01.poool.model;

import java.util.List;

public record BoardData(Boundary bounds, List<BallData> balls, BallData playerBall) {

}
