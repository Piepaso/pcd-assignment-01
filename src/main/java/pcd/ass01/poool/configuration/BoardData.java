package pcd.ass01.poool.configuration;

import pcd.ass01.poool.model.BallData;
import pcd.ass01.poool.model.Boundary;

import java.util.List;

public record BoardData(Boundary bounds, List<BallData> balls, BallData playerBall) {

}
