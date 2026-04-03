package pcd.ass01.poool;

import pcd.ass01.poool.configuration.BoardConf;
import pcd.ass01.poool.configuration.MassiveBoardConf;
import pcd.ass01.poool.configuration.PoolBoardConf;
import pcd.ass01.poool.controller.ActiveController;
import pcd.ass01.poool.controller.BallsMonitor;
import pcd.ass01.poool.controller.BotAgent;
import pcd.ass01.poool.controller.CmdMonitor;
import pcd.ass01.poool.model.*;
import pcd.ass01.poool.view.RenderMonitor;
import pcd.ass01.poool.view.View;
import pcd.ass01.poool.view.ViewAgent;
import pcd.ass01.poool.view.ViewModel;

public class Poool {

	
	public static void main(String[] argv) {

		final BallFactory ballFactory = new BallFactory();
		final BoardConf CONFIGURATION = new PoolBoardConf(ballFactory);
		final int THREADS = CONFIGURATION.getSmallBalls().size() > 150 ? Runtime.getRuntime().availableProcessors(): 1;

		Board board = new Board(CONFIGURATION);
		BallsMonitor ballsMonitor = new BallsMonitor(board, THREADS);

		CmdMonitor cmdMonitor = new CmdMonitor(ballFactory.getMousePlayerId());
		ActiveController controller = new ActiveController(cmdMonitor);

		ViewModel viewModel = new ViewModel(ballsMonitor.getUpdatedBoardData(), board.getHoles());
		RenderMonitor renderMonitor = new RenderMonitor();
		View view = new View(viewModel, controller, renderMonitor);

		ViewAgent viewAgent = new ViewAgent(view, viewModel, ballsMonitor, renderMonitor);

		board.startEngine(ballsMonitor, cmdMonitor, THREADS);
		controller.start();
		viewAgent.start();
		ballFactory.getBots().forEach(id -> new BotAgent(cmdMonitor, 1000, id).start());
	}
}
