package pcd.ass01.poool;

import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.configuration.PoolConf;
import pcd.ass01.poool.controller.ActiveController;
import pcd.ass01.poool.controller.BallsMonitor;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.*;
import pcd.ass01.poool.view.View;
import pcd.ass01.poool.view.ViewModel;

public class Poool {

	
	public static void main(String[] argv) {

		final BoardConf CONFIGURATION = new PoolConf();
		final int THREADS = 1; //CONFIGURATION.getSmallBalls().size() > 100 ? Runtime.getRuntime().availableProcessors(): 1; // +1 view = n + 1 threads

		Board board = new Board(CONFIGURATION);

		CmdMonitor cmdMonitor = new CmdMonitor();
		ActiveController controller = new ActiveController(cmdMonitor);
		BallsMonitor ballsMonitor = new BallsMonitor(board, THREADS);
		ViewModel viewModel = new ViewModel();
		viewModel.init(ballsMonitor.getUpdatedBoardData(), board.getHoles());

		View view = new View(viewModel, controller);

		ViewAgent viewAgent = new ViewAgent(view, viewModel, ballsMonitor::getUpdatedBoardData, ballsMonitor::getFrames);

		board.startBalls(ballsMonitor, cmdMonitor, THREADS);
		controller.start();
		viewAgent.start();
	}
}
