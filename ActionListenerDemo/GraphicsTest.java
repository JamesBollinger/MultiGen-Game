/*
 * Vincent Cozzo
 * A basic "game board" design,
 * partly inspired by the tutorial by Jan Bodnar fround at:
 * http://zetcode.com/gfx/java2d/basicdrawing/
 * 
 * But --- now it uses a BufferedImage to model the gameBoardImage.
 * (This is the graphicaal representation of the game board.)
 * 
 */

import java.awt.*;
import java.awt.image.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

class WindowTest extends JPanel implements MouseListener {
	/* First, define some constants that are used
	 * throughout the class.
	 * It's better to have them all in one place,
	 * and adjust them later if we want a larger game board.
	 * Then all the references will be updated.
	 * 
	 * */
	private final int numTiles = 8;
	private final int width = 320;
	private final int height = 320;
	private final int tileSize  = 40; // number of pixels spanning the height of each tile

	private int state; // To determine what to do on mouse clicks...you'll see
	private boolean regenColors;
/*	private Color storedColor;    */
	private int srcX, srcY;
	private BufferedImage gameBoardImage;

	public WindowTest() {
		addMouseListener(this);
		state = 0;
		regenColors = true;
		gameBoardImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	private void colorSquare (int xInd, int yInd, int color) {
		int r = tileSize*(xInd / tileSize);
		int s = tileSize*(yInd / tileSize);
		for (int i = 0; i < tileSize; i ++) {
			for (int j = 0; j < tileSize; j ++) {
				gameBoardImage.setRGB(r+i, s+j, color);
			}
		}
	}

	public void drawGreeting(Graphics k) {
		Graphics2D kAs2D = (Graphics2D) k;
		kAs2D.drawString("CHOCORHO'S Java 2D! Woo!", 45, 45);
	}

	public void drawBoard(Graphics manager) {
		Graphics2D kAs2D = (Graphics2D) manager;
		
		if (regenColors) {
			for (int y = 0; y < numTiles; y ++) {
				for (int x = 0; x < numTiles; x ++) {
					int randomRed = (int) ((Math.random()*4096));
					int randomBlue = (int) ((Math.random()*4096));
					int randomGreen = (int) ((Math.random()*4096));
					int sum = (randomRed * randomBlue * randomGreen);
/*					Color nextRandomColor = new Color(randomRed, randomBlue, randomGreen);    */

					// How do the next two statements change if we use a BufferedImage?
					// Let's find out...
					colorSquare(x*tileSize, y*tileSize, sum);
				}
			}
			regenColors = false;
		}/* else if (state == 1) {
			colorSquare();
			manager.copyArea(srcX, srcY, tileSize, tileSize, dx, dy);
		}*/
		kAs2D.drawImage(gameBoardImage, null, 0, 0);
	}

	@Override
	public void paintComponent(Graphics gm) {
		super.paintComponent(gm);
/*		drawGreeting(gm);*/
		drawBoard(gm);
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		System.out.println("MOUSE CLICK IDENTIFIED!");
		int xClick = ev.getX();
		int yClick = ev.getY();
		System.out.println("It occurred at (" + ev.getX()+ ", " + ev.getY() + ").");
		if (state == 0) {
			// Get the color stored at the location specified.
			// Save it to a buffer in memory.
/*			storedColor = ;    */
			// Or --- alternate approach
			// store the source coordinates for a call to copyArea()
			if ((xClick < width) && (yClick < height)) {
				srcX = tileSize*(xClick / tileSize);
				srcY = tileSize*(yClick / tileSize);

				System.out.println("source data copied.");
			} else {
				System.out.println("Err -- out of bounds");
			}
			state ++;
		} else if (state == 1) {
			// Now use the color stored in memory to re-paint this square!
			// ...
			if ((xClick < width) && (yClick < height)) {
				int dstX = tileSize*(xClick / tileSize);
				int dstY = tileSize*(yClick / tileSize);
				colorSquare(dstX, dstY, gameBoardImage.getRGB(srcX, srcY));
				repaint();
			} else {
				System.out.println("Err -- out of bounds");
			}
			state --;
		} else {
			// no-op
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
/*		System.out.println("You seem to have stopped paying attention to the game. Are you alright? :P");  */
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
