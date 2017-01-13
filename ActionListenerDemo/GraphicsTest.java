/*
 * Vincent Cozzo
 * A basic "game board" design,
 * partly inspired by the tutorial fround at:
 * http://zetcode.com/gfx/java2d/basicdrawing/
 * 
 */

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

class WindowTest extends JPanel implements MouseListener {
	private final int numTiles = 8;

	public WindowTest() {
		addMouseListener(this);
	}

	public void drawGreeting(Graphics k) {
		Graphics2D kAs2D = (Graphics2D) k;
		kAs2D.drawString("CHOCORHO'S Java 2D! Woo!", 45, 45);
	}

	public void drawLines(Graphics manager) {
		Graphics2D kAs2D = (Graphics2D) manager;
		
		int upperInd = numTiles*numTiles;
		for (int ind = 0; ind < numTiles; ind ++) {
			kAs2D.drawLine(40*ind, 1, 40*ind, 280);
			kAs2D.drawLine(1, 40*ind, 280, 40*ind);
		}
/*
		kAs2D.drawLine(20, 20, 20, 80);
		kAs2D.drawLine(40, 20, 40, 80);
		kAs2D.drawLine(60, 20, 60, 80);
		kAs2D.drawLine(80, 20, 80, 80);
*/
	}

	@Override
	public void paintComponent(Graphics gm) {
		super.paintComponent(gm);
/*		drawGreeting(gm);*/
		drawLines(gm);
	}


	@Override
	public void mouseClicked(MouseEvent ev) {
		System.out.println("MOUSE CLICK IDENTIFIED!");
		System.out.println("It occurred at (" + ev.getX()+ ", " + ev.getY() + ").");
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		//System.out.println("mouse has been released.");
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		//System.out.println("mouse has been pressed down.");
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
		//System.out.println("mouse has entered the window.");
	}

	@Override
	public void mouseExited(MouseEvent ev) {
		System.out.println("You seem to have stopped paying attention to the game. Are you alright? :P");
	}
}

public class GraphicsTest extends JFrame {
	public GraphicsTest() {
		initUI();
	}

	private void initUI() {
		WindowTest allGraphics = new WindowTest();
		add(allGraphics);
		setTitle("chocorho's Java2D Example, inspired by zetcode");
		setSize(320, 320);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] argv) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GraphicsTest manager = new GraphicsTest();
				manager.setVisible(true);
			}
		});
	}
}

