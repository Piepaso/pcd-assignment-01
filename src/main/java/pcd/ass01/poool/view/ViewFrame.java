package pcd.ass01.poool.view;

import pcd.ass01.poool.controller.ActiveController;
import pcd.ass01.poool.controller.PressedCmd;
import pcd.ass01.poool.controller.ReleasedCmd;
import pcd.ass01.poool.model.P2d;
import pcd.ass01.poool.model.V2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.stream.Stream;
import javax.swing.*;

public class ViewFrame extends JFrame {
    
    private final VisualiserPanel panel;
    private final ViewModel model;
	private final ActiveController controller;
    
    public ViewFrame(ViewModel model, ActiveController controller, int w, int h){
    	this.model = model;
		this.controller = controller;

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
    }
        
    public class VisualiserPanel extends JPanel {
        private int dx;
        private int dy;
        
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
    		g2.setColor(Color.LIGHT_GRAY);



			/* Render balls */
            for (var b: model.getBalls()) {
                var p = b.pos();
                int x0 = (int)(dx + p.x()*dx);
                int y0 = (int)(dy - p.y()*dy);
                int radiusX = (int)(b.radius()*dx);
                int radiusY = (int)(b.radius()*dy);
                g2.fillOval(x0 - radiusX,y0 - radiusY,radiusX*2,radiusY*2);
            }

			/* Render player */
            g2.setStroke(new BasicStroke(3));
            var pb = model.getPlayerBall();
            if (pb != null) {
				var p1 = pb.pos();
	            int x0 = (int)(dx + p1.x()*dx);
	            int y0 = (int)(dy - p1.y()*dy);
                int radiusX = (int)(pb.radius()*dx);
                int radiusY = (int)(pb.radius()*dy);
                g2.fillOval(x0 - radiusX,y0 - radiusY,radiusX*2,radiusY*2);
	            g2.setColor(Color.BLACK);
				g2.drawString("H", x0 - 4, y0 + 4);
            }

	        g2.setStroke(new BasicStroke(1));
	        g2.drawString("Num small balls: " + model.getBalls().size(), 20, 140);
	        g2.drawString("FPS engine: " + model.getEngineFPS(), 20, 160);
	        //g2.drawString("FPS view: " + model.getViewFPS(), 20, 180);

			double totalKineticEnergy = model.getBalls().stream()
				.map(b -> 0.5 * b.mass() * Math.pow(b.vel().abs(), 2))
				.reduce(0.0, Double::sum);

			g2.drawString("Total kinetic energy: " + totalKineticEnergy, 20, 180);
			g2.drawString("Score: " + model.getScore(), 20, 200);


        }
        
    }

	private P2d getNormalizedPos(MouseEvent e) {
		long time = System.currentTimeMillis();
		double normX = (e.getX() - panel.dx) / (double) panel.dx;
		double normY = (panel.dy - e.getY()) / (double) panel.dy;
		return new P2d(normX, normY);
	}

}
