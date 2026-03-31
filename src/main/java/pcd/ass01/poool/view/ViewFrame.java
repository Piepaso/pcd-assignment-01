package pcd.ass01.poool.view;

import pcd.ass01.poool.controller.ActiveController;
import pcd.ass01.poool.controller.PressedCmd;
import pcd.ass01.poool.controller.ReleasedCmd;
import pcd.ass01.poool.model.P2d;
import pcd.ass01.poool.model.PlayerData;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import javax.swing.*;

public class ViewFrame extends JFrame {

    private final VisualiserPanel panel;
    private final ViewModel model;
	private final RenderMonitor renderMonitor;
	private boolean gameOverDisplayed = false;
	private static final Map<Integer, Color> PLAYER_COLORS = Map.of(
			0, Color.YELLOW,
			1, Color.RED,
			2, Color.PINK
			);
    
    public ViewFrame(ViewModel model, ActiveController controller, RenderMonitor renderMonitor, int w, int h){
    	this.model = model;
		this.renderMonitor = renderMonitor;

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
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
    }

	public void render() {
		panel.repaint();

		// Controllo se il gioco è finito e se non abbiamo ancora mostrato il popup
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
	        for (var h: model.getHoles()) {
		        var p = h.position();
		        int x0 = (int)(dx + p.x()*dx);
		        int y0 = (int)(dy - p.y()*dy);
		        int radiusX = (int)(h.radius()*dx);
		        int radiusY = (int)(h.radius()*dy);
		        g2.fillOval(x0 - radiusX,y0 - radiusY,radiusX*2,radiusY*2);
	        }

			/* Render balls */
	        int lastColor = -2;
            for (var b: model.getBalls()) {
				if (b.lastCollisionPlayerId() != lastColor) {
					lastColor = b.lastCollisionPlayerId();
					g2.setColor(PLAYER_COLORS.getOrDefault(lastColor, Color.LIGHT_GRAY));
				}
                var p = b.pos();
                int x0 = (int)(dx + p.x()*dx);
                int y0 = (int)(dy - p.y()*dy);
                int radiusX = (int)(b.radius()*dx);
                int radiusY = (int)(b.radius()*dy);
                g2.fillOval(x0 - radiusX,y0 - radiusY,radiusX*2,radiusY*2);
            }

			/* Render player */
            g2.setStroke(new BasicStroke(3));
            for (var p: model.getPlayers()) {
				if (p.isAlive()) {
					var b = p.ball();
					var pos = b.pos();
					int x0 = (int) (dx + pos.x() * dx);
					int y0 = (int) (dy - pos.y() * dy);
					int radiusX = (int) (b.radius() * dx);
					int radiusY = (int) (b.radius() * dy);
					g2.setColor(PLAYER_COLORS.getOrDefault(p.id(), Color.GRAY));
					g2.fillOval(x0 - radiusX, y0 - radiusY, radiusX * 2, radiusY * 2);
				}
				g2.drawString("Player " + p.id() + " score: " + p.score(), 20, 120 + 20*p.id());
			}

			g2.setColor(Color.LIGHT_GRAY);

	        g2.setStroke(new BasicStroke(1));
	        g2.drawString("Num small balls: " + model.getBalls().size(), 20, 60);
	        g2.drawString("FPS engine: " + model.getEngineFPS(), 20, 80);
	        g2.drawString("FPS view: " + model.getViewFPS(), 20, 100);

	        renderMonitor.signal();
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

	private P2d getNormalizedPos(MouseEvent e) {
		double normX = (e.getX() - panel.dx) / (double) panel.dx;
		double normY = (panel.dy - e.getY()) / (double) panel.dy;
		return new P2d(normX, normY);
	}

}
