/*
 * Vincent Cozzo
 * 
 * The older "game board" design,
 * partly inspired by the tutorial by Jan Bodnar fround at:
 * http://zetcode.com/gfx/java2d/basicdrawing/
 * 
 * This time, it simply uses lines to generate a grid. Pretty simple.
 * (This is the graphicaal representation of the game board.)
 * 
 */

import java.awt.*;
import java.awt.image.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

class WindowTest extends JPanel implements MouseListener {
	private final int numTiles = 8;
	private final int width = 320;
	private final int height = 320;
	private final int tileSize  = 40; // number of pixels spanning the height of each tile

	public WindowTest() {
		addMouseListener(this);
	}

	public void drawGreeting(Graphics k) {
		Graphics2D kAs2D = (Graphics2D) k;
		kAs2D.drawString("CHOCORHO'S Java 2D! Woo!", 45, 45);
	}

	public void drawBoard(Graphics manager) {
		Graphics2D kAs2D = (Graphics2D) manager;
		
/*		int upperInd = numTiles*numTiles;  */
		for (int x = 0; x < numTiles; x ++) {
			for (int y = 0; y < numTiles; y ++) {
				int randomRed = (int) ((Math.random()*256));
				int randomBlue = (int) ((Math.random()*256));
				int randomGreen = (int) ((Math.random()*256));
				int sum = (randomRed * randomBlue * randomGreen);
				Color nextRandomColor = new Color(randomRed, randomBlue, randomGreen);

				kAs2D.setPaint(nextRandomColor);
				kAs2D.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
			}
			kAs2D.drawLine(tileSize*x, 1, tileSize*x, 280);
			kAs2D.drawLine(1, tileSize*x, 280, tileSize*x);
		}
/*
		kAs2D.drawLine(20, 20, 20, 80);
		kAs2D.drawLine(tileSize, 20, tileSize, 80);
		kAs2D.drawLine(60, 20, 60, 80);
		kAs2D.drawLine(80, 20, 80, 80);
*/
	}

	@Override
	public void paintComponent(Graphics gm) {
		super.paintComponent(gm);
/*		drawGreeting(gm);*/
		drawBoard(gm);
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
/*		System.out.println("MOUSE CLICK IDENTIFIED!");    */
		int xClick = ev.getX();
		int yClick = ev.getY();
/*		System.out.println("It occurred at (" + ev.getX()+ ", " + ev.getY() + ").");    */
		if ((xClick < width) && (yClick < height)) {
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
/*		System.out.println("mouse has been released.");  */
	}

	@Override
	public void mousePressed(MouseEvent ev) {
/*		System.out.println("mouse has been pressed down.");  */
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
/*		System.out.println("mouse has entered the window.")  */
	}

	@Override
	public void mouseExited(MouseEvent ev) {
		System.out.println("You seem to have stopped paying attention to the game. Are you alright? :P");
	}
}

public class GraphicsLines extends JFrame {
	public GraphicsLines() {
		initUI();
	}

	private void initUI() {
		WindowTest allGraphics = new WindowTest();
		add(allGraphics);
		setTitle("chocorho's Java2D Example, inspired by zetcode");
		setSize(340, 360);
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
