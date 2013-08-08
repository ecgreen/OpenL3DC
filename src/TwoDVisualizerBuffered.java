/*
 * Author: Ethan Green
 * L3DC Project - Do NOT copy or use this code without authorized permission.
 */

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.JFrame;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener; 
import java.awt.Color; 
import java.awt.image.BufferStrategy;

public class TwoDVisualizerBuffered extends JFrame {

	private static final long serialVersionUID = 1L;
	FingerList fingers = new FingerList();
	private static final int size = 70;
	private static final int minX = -100;
	private static final int maxX = 100;
	private static final int minY = 100;
	private static final int maxY = 300;
	private static Dimension screenSize;
	Listener listener = new Listener();
	static Controller controller = new Controller();

	public TwoDVisualizerBuffered(){
		super("L3DC");
		// Create a new dimension and set it as the screen size for netbeans.
		// Dimension d = new Dimension(500,500);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(screenSize.width, screenSize.height);
		this.setVisible(true);
		this.createBufferStrategy(2);
		this.visualLoop();
	}



	public void drawStuff()  
	{  
		BufferStrategy bf = this.getBufferStrategy();
		Graphics2D g2d = (Graphics2D) bf.getDrawGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clearRect(0, 0, screenSize.width, screenSize.height);
		g2d.setColor(Color.RED);
		
		g2d.drawRect(trueX(minX) + size/2, trueY(maxY) + size/2, trueX(maxX) - trueX(minX), trueY(minY) - trueY(maxY));
		for(Finger f : fingers){
			int x = (int)f.tipPosition().getX();
			int y = (int)f.tipPosition().getY();
			if (x > minX && x < maxX && y > minY && y < maxY) drawCircleGreen(x,y,g2d);
			else drawCircleRed(x,y,g2d);
		}
		bf.show();
		g2d.dispose();
	}


	public void drawCircleGreen(int x, int y, Graphics2D g) {
		int trueX = trueX(x);
		int trueY = trueY(y);
		g.setColor(Color.GREEN);
		g.fillOval(trueX, trueY, size, size);
	}

	public void drawCircleRed(int x, int y, Graphics2D g) {
		int trueX = trueX(x);
		int trueY = trueY(y);
		g.setColor(Color.RED);
		g.fillOval(trueX, trueY, size, size);
	}

	public int trueX(int x){
		int temp = x + 200;
		double multiplier = screenSize.width/400.0;
		return temp *= multiplier;
	}

	public int trueY(int y){
		int tempY = -1* (y - 350);
		if(y < 0) y = 0;
		double multiplier = screenSize.height/400.0;
		return tempY *= multiplier;
	}

	public void visualLoop(){
		while(true){
			Frame frame = controller.frame();
			this.fingers = frame.fingers();
			this.drawStuff();
		}
	}

	public static void main (String[] args) throws InterruptedException, AWTException 
	{
		new TwoDVisualizerBuffered();
	}


}