package pcd.ass01.poool.view;

import pcd.ass01.poool.controller.ActiveController;
import pcd.ass01.poool.controller.PressedCmd;
import pcd.ass01.poool.controller.ReleasedCmd;
import pcd.ass01.poool.model.board.Hole;
import pcd.ass01.poool.model.board.P2d;
import pcd.ass01.poool.model.dto.BallData;
import pcd.ass01.poool.model.dto.PlayerData;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import static pcd.ass01.poool.configuration.StaticConf.PLAYER_COLORS;

public class ViewFrame extends JFrame {

    private final VisualiserPanel panel;
    private final ViewModel model;
	private boolean gameOverDisplayed = false;
	private boolean showControlHints = true;

    public ViewFrame(ViewModel model, ActiveController controller, int w, int h){
    	this.model = model;

    	setTitle("Poool");
        setSize(w,h + 25);
        setResizable(false);
        panel = new VisualiserPanel(w,h);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});

		panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				controller.notifyNewCmd(new PressedCmd(System.currentTimeMillis()));
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				controller.notifyNewCmd(new ReleasedCmd(System.currentTimeMillis(), getNormalizedPos(e)));
				if (showControlHints) {
					showControlHints = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
    }

	public void render() {
		panel.repaint();
		Toolkit.getDefaultToolkit().sync(); // prevent GUI freeze on Linux while moving mouse
		if (model.isGameOver() && !gameOverDisplayed) {
			gameOverDisplayed = true;
			showGameOverDialog();
		}
	}

	public class VisualiserPanel extends JPanel {
        private final int dx;
        private final int dy;
        
        public VisualiserPanel(int w, int h){
            setSize(w,h + 25);
            dx = w/2;
            dy = h/2;
        }

        public void paint(Graphics g){
    		Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setColor(Color.getHSBColor(145f/360f, 0.6f, 0.45f));
    		g2.fillRect(0,0,this.getWidth(),this.getHeight());
            
    		g2.setColor(Color.getHSBColor(145f/360f, 0.6f, 0.65f));
		    g2.setStroke(new BasicStroke(1));
    		g2.drawLine(dx,0,dx,dy*2);
    		g2.drawLine(0,dy,dx*2,dy);

	        g2.setColor(Color.BLACK);
	        for (Hole h : model.getHoles()) {
		        P2d p = h.position();
		        int x0 = (int)(dx + p.x()*dx);
		        int y0 = (int)(dy - p.y()*dy);
		        int radiusX = (int)(h.radius()*dx);
		        int radiusY = (int)(h.radius()*dy);
		        g2.fillOval(x0 - radiusX,y0 - radiusY,radiusX*2,radiusY*2);
	        }

			/* Render balls */
	        int lastColor = -2;
			for (BallData b : model.getBalls()) {
				if (b.lastCollisionPlayerId() != lastColor) {
					lastColor = b.lastCollisionPlayerId();
					g2.setColor(PLAYER_COLORS.getOrDefault(lastColor, Color.LIGHT_GRAY));
				}
				P2d p = b.pos();
                int x0 = (int)(dx + p.x()*dx);
                int y0 = (int)(dy - p.y()*dy);
                int radiusX = (int)(b.radius()*dx);
                int radiusY = (int)(b.radius()*dy);
                g2.fillOval(x0 - radiusX,y0 - radiusY,radiusX*2,radiusY*2);
            }

			/* Render player */
            g2.setStroke(new BasicStroke(3));
			for (PlayerData p : model.getPlayers()) {
				if (p.isAlive()) {
					BallData b = p.ball();
					P2d pos = b.pos();
					int x0 = (int) (dx + pos.x() * dx);
					int y0 = (int) (dy - pos.y() * dy);
					int radiusX = (int) (b.radius() * dx);
					int radiusY = (int) (b.radius() * dy);
					g2.setColor(PLAYER_COLORS.getOrDefault(p.id(), Color.GRAY));
					g2.fillOval(x0 - radiusX, y0 - radiusY, radiusX * 2, radiusY * 2);
					if (p.id() == model.getHumanPlayerId()) {
						g2.setColor(Color.BLACK);
						g2.drawString("H", x0 - 5, y0 + 4);
						g2.setColor(PLAYER_COLORS.getOrDefault(p.id(), Color.GRAY));
					}
				} else {
					g2.setColor(Color.LIGHT_GRAY);
				}
				g2.drawString("Player " + p.id() + " score: " + p.score(), 20, 120 + 20*p.id());
			}

			g2.setColor(Color.BLACK);

	        g2.setStroke(new BasicStroke(1));
	        g2.drawString("Num small balls: " + model.getBalls().size(), 20, 60);
	        g2.drawString("FPS engine: " + model.getEngineFPS(), 20, 80);
	        g2.drawString("FPS view: " + model.getViewFPS(), 20, 100);

			if (showControlHints) {
				paintControlHints(g2);
			}
        }

    }

	private void showGameOverDialog() {
		SwingUtilities.invokeLater(() -> {
			StringBuilder sb = new StringBuilder("Game Over!\n\nScore leaderboard:\n");
			model.getPlayers().stream().filter(PlayerData::isAlive).sorted((p1, p2) -> p2.score() - p1.score())
					.forEach(p -> sb.append("Giocatore " + p.id() + ": " + p.score() + "\n"));

			Object[] options = {"Exit game"};
			JOptionPane.showOptionDialog(this,
					sb.toString(),
					"Game Over",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					options[0]);

			System.exit(0);
		});
	}

	private void paintControlHints(Graphics2D g2) {
		int panelW = this.getWidth();
		int panelH = this.getHeight();
		int boxW = Math.min(760, panelW - 60);
		int boxH = 110;
		int boxX = (panelW - boxW) / 2;
		int boxY = (panelH - boxH) / 2;

		g2.setColor(new Color(0, 0, 0, 210));
		g2.fillRoundRect(boxX, boxY, boxW, boxH, 16, 16);
		g2.setColor(new Color(255, 255, 255, 220));
		g2.drawRoundRect(boxX, boxY, boxW, boxH, 16, 16);

		Font oldFont = g2.getFont();
		g2.setColor(Color.WHITE);
		g2.setFont(oldFont.deriveFont(Font.BOLD, 15f));
		g2.drawString("Controls", boxX + 18, boxY + 30);
		g2.setFont(oldFont.deriveFont(Font.PLAIN, 14f));
		g2.drawString("Hold the mouse button to charge your shot.", boxX + 18, boxY + 58);
		g2.drawString("On release, the player ball moves along the vector from cursor to ball center.\n" +
				"Power depends on hold time.", boxX + 18, boxY + 82);
		g2.setFont(oldFont);
	}

	private P2d getNormalizedPos(MouseEvent e) {
		double normX = (e.getX() - panel.dx) / (double) panel.dx;
		double normY = (panel.dy - e.getY()) / (double) panel.dy;
		return new P2d(normX, normY);
	}

}
