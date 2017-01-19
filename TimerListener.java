/*
 * Vincent Cozzo
 * A basic "game board" design,
 * partly inspired by the tutorial by Jan Bodnar fround at:
 * http://zetcode.com/gfx/java2d/basicdrawing/
 * 
 * This will be a timer listener, once I develop it...
 * 
 */

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

class WindowTest extends JPanel implements MouseListener {
	/* 
	 * First, define some constants that are used
	 * throughout the class.
	 * It's better to have them all in one place,
	 * and adjust them later if we want a larger game board.
	 * Then all the references will be updated.
	 * 
	 * */
	private final int numTiles = 8;
	private final int width = 640;
	private final int height = 640;
	private final int tileSize  = 80; // number of pixels spanning the height of each tile

	private int mouseState; // To determine what to do on mouse clicks... you'll see
	private boolean regenColors;
	private ArrayList<Character> entities;
/*	private Color storedColor;    */
	private int numLayers;
	private BufferedImage[] terrainImage;
	private BufferedImage unitsImage;
	private Character highlighted;
	private int currentTeam;

	//Every single object in the game, is made up of the enemies and friendlies arrayLists declared below
	Character[][] gameBoard;

	//ArrayList<ArrayList<Character>> entities = new ArrayList<ArrayList<Character>>();
	//Is sorted with characters with higher intiative ranking higher
	//ArrayList<Character> initiativeRanking = new ArrayList<Character>();
	//Is an array of all the friendly characters
	ArrayList<Character> friendlies;
	//Enemy Array list
	ArrayList<Character> enemies;
	//Is the width/height(game currently is square)
	public int tilesX = 8;
	public int tilesY = 8;
	private int state;

	public WindowTest(ArrayList<Character> playerUnits, ArrayList<Character> enemyUnits) {
/*		entities = new ArrayList<Character>();
		entities.addAll(playerUnits);
		entities.addAll(enemyUnits);*/
		addMouseListener(this);
		state = 0;
		regenColors = true;
		numLayers = 3;
		currentTeam = 0; // start the game in Player Phase
		terrainImage = new BufferedImage[3];
		terrainImage[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		unitsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		friendlies = playerUnits;
		enemies = enemyUnits;

		gameBoard = new Character[tilesY][tilesX];

		/* Now for the fun part:
		 * Using the data from the entities, set them on the map! */
		System.out.println("Player Units List - at end of constructor -- " + friendlies.toString());
		System.out.println("Enemy Units List - at end of constructor -- " + enemies.toString());

		for (int playerInd = 0; playerInd < friendlies.size(); playerInd ++) {
			int nextX = friendlies.get(playerInd).getX();
			int nextY = friendlies.get(playerInd).getY();

			/* Note: I may want to clarify what X and Y represent
			* (because it's a grid, we may want to either
			* rename X and Y as 'row' and 'column'
			* or at least make sure we're on the same page 
			* on what X and Y represent */
			if (gameBoard[nextY][nextX] == null) {
				gameBoard[nextY][nextX] = friendlies.get(playerInd);
			} else {
				throw new UnsupportedOperationException("ERR -- two units are on the same tile???");
			}
		}
		for (int enemyInd = 0; enemyInd < enemies.size(); enemyInd ++) {
			int nextX = enemies.get(enemyInd).getX();
			int nextY = enemies.get(enemyInd).getY();

			/* Note: I may want to clarify what X and Y represent
			* (because it's a grid, we may want to either
			* rename X and Y as 'row' and 'column'
			* or at least make sure we're on the same page 
			* on what X and Y represent */
			if (gameBoard[nextY][nextX] == null) {
				gameBoard[nextY][nextX] = enemies.get(enemyInd);
			} else {
				throw new UnsupportedOperationException("ERR -- two units are on the same tile???");
			}
		}
	}

	public synchronized int getState() {
		return state;
	}

	public synchronized void setState(int nextState) {
		state = nextState;
	}

	public Character recall(int p, int q){
		if(gameBoard[p][q] != null) return gameBoard[p][q];
		return null;
	}

	//Prints the board
	public String toString(){
		String ret = "";
		for(int i = 0; i < tilesY; i++){
			for(int j = 0; j < tilesX; j++){
				if (recall(i,j) == null){
					ret = ret + "# ";
				} else {
					ret = ret + recall(i,j).toString() + " ";
				}
			}
			ret = ret + "\n";
		}
		return ret;
	}

	//checks to see if a move is valid
	private boolean moveTest(Character selected, int x, int y){
		if(x >= tilesX || y >= tilesY || x < 0 || y < 0)
			return false;
		int distance;
		int initX = selected.getX();
		int initY = selected.getY();
		if (initX < x) {
			if (initY < y) {
				distance = (x - initX) + (y - initY);
			} else {
				distance = (x - initX) + (initY - y);
			}
		} else {
			if (initY < y) {
				distance = (initX - x) + (y - initY);
			} else {
				distance = (initX - x) + (initY - y);
			}
		}
		if(distance > selected.speed){
			System.out.println("distance too far");
			return false;
		}
		if(recall(y,x) != null){
			System.out.println("is occupied");
			return false;
		}
		//System.out.println("passed");
		return true;
	}

	//is the method called to move
	public void move(Character selected, int x, int y){
		if(moveTest(selected,x,y)){
			//alters the characters position
			gameBoard[selected.getY()][selected.getX()] = null;
			selected.move(x, y);
			gameBoard[y][x] = selected;
		}
	}

	private void colorSquare(int layer, int xInd, int yInd, int color) {
		int r = tileSize*(xInd / tileSize);
		int s = tileSize*(yInd / tileSize);
		for (int i = 0; i < tileSize; i ++) {
			for (int j = 0; j < tileSize; j ++) {
/*				if ((i == 0) && (j == 0)) {
					System.out.println("\tpixel has been colored " + color);
				}*/
				terrainImage[layer].setRGB(r+i, s+j, color);
			}
		}
	}

	public void drawGreeting(Graphics k) {
		Graphics2D kAs2D = (Graphics2D) k;
		kAs2D.drawString("Our Java 2D! Woo!", 45, 45);
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

					if ((sum + 1587614848) != 0) {
						colorSquare(0, x*tileSize, y*tileSize, sum);
					} else {
						colorSquare(0, x*tileSize, y*tileSize, 122110000-sum);
					}
				}
				kAs2D.drawLine(tileSize*y, 1, tileSize*y, height);
				kAs2D.drawLine(1, tileSize*y, width, tileSize*y);
			}
			regenColors = false;
		} else {
			for (int y = 0; y < numTiles; y ++) {
				kAs2D.drawLine(tileSize*y, 1, tileSize*y, height);
				kAs2D.drawLine(1, tileSize*y, width, tileSize*y);
			}
		}

		for (int ind=0; ind < numLayers; ind ++) {
/*			System.out.println("About to paint layer " + ind + " of the terrain image.");
			if (terrainImage[ind] == null) {
				System.out.println("\tERR -- it's null!");
			}*/
			kAs2D.drawImage(terrainImage[ind], null, 0, 0);
/*			kAs2D.drawImage(terrainImage[ind], new AffineTransform(), null);*/
		}
	}

	public void drawIcons(Graphics manager) {
		for (int ind = 0; ind < enemies.size(); ind ++) {
			Character nextEnemy = enemies.get(ind);
			renderCharImage(manager, nextEnemy);
		}
		for (int ind = 0; ind < friendlies.size(); ind ++) {
			renderCharImage(manager, friendlies.get(ind));
		}
	}

	private AffineTransform getTransformToResize(Character n) {
		double scaleFactor, difference;
		int x = tileSize*n.getX();
		int y = tileSize*n.getY();

		AffineTransform result = new AffineTransform();
		result.translate((double)  x, (double) y);

		ImageIcon icon = n.getIcon();
		if (icon.getIconWidth() > icon.getIconHeight()) {
			scaleFactor = (((tileSize)) / (double) (icon.getIconWidth()));
			difference = (/*icon.getIconWidth()*/tileSize - (scaleFactor*(icon.getIconHeight())));
			if (difference <= 0) {
				difference = 0.0;
			} else {
				difference /= (2.0);
			}
			result.translate(0, difference);
		} else {
			scaleFactor = (tileSize / ((double) ((icon.getIconHeight()))));
			difference = (/*icon.getIconHeight()*/tileSize - (scaleFactor*(icon.getIconWidth())));
			if (difference > 0) {
				difference /= (2.0);
			} else {
				difference = 0.0;
			}
			result.translate(difference, 0);
		}
		result.scale(scaleFactor, scaleFactor);
		return result;
	}

	private void renderCharImage(Graphics gm,/* int indX, int indY,*/ Character p) {
		Graphics2D gm2d = (Graphics2D) gm;
		AffineTransform applied = getTransformToResize(p);
		gm2d.drawImage(p.getIcon().getImage(), applied, null);
	}

	/***
	 * A recursive method to color the area of tiles
	 * to which a player CAN move a particular unit. 
	 *
	 * */
	private void showMoveRange(int speed, int xCoord, int yCoord) {
		if ((xCoord >= 0) && (xCoord < tilesX) &&
		    (yCoord >= 0) && (yCoord < tilesY)) {
			if (speed == 0) {
				if (gameBoard[yCoord][xCoord] == null) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1587614848);
				} else if (gameBoard[yCoord][xCoord].getTeam() == currentTeam) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1587614848);
				}
			} else {
				if (gameBoard[yCoord][xCoord] == null) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1587614848);
					showMoveRange(speed-1, xCoord-1, yCoord);
					showMoveRange(speed-1, xCoord+1, yCoord);
					showMoveRange(speed-1, xCoord, yCoord-1);
					showMoveRange(speed-1, xCoord, yCoord+1);
				} else if (gameBoard[yCoord][xCoord].getTeam() == currentTeam) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1587614848);
					showMoveRange(speed-1, xCoord-1, yCoord);
					showMoveRange(speed-1, xCoord+1, yCoord);
					showMoveRange(speed-1, xCoord, yCoord-1);
					showMoveRange(speed-1, xCoord, yCoord+1);
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics gm) {
		super.paintComponent(gm);
		drawBoard(gm);
		drawIcons(gm);
/*		drawGreeting(gm);*/
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		int xClick = ev.getX();
		int yClick = ev.getY();
		int xInd = (xClick / tileSize);
		int yInd = (yClick / tileSize);
		System.out.println("Mouse click occurred at (" + xClick + ", " + yClick + ").");
		int snapShotState = getState();
		if (snapShotState == 0) {
			highlighted = gameBoard[yInd][xInd];
			if (highlighted.getTeam() != currentTeam) {
				System.out.println("cannot move that unit.");
			} else {
				if (highlighted != null) {
					setState(1);
					System.out.println("Set state to 1 upon that click you just made!");
					terrainImage[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					System.out.println("Initialized a new BufferedImage at index 1 of the terrain array!");
					System.out.println("showMoveRange() about to be called, at col = " + xInd + ", row = " + yInd);
					showMoveRange(highlighted.getSpeed(), xInd, yInd);
					System.out.println("Prepared the image of MOVE RANGE ahead of time");
					repaint();
				} else {
					/* Terrain selected at this point! */
					/* we could have some sode to deal with that
					 * (i.e., to process the terrain-click) here... */
					
				}
			}
		}
		/* Make comparisons such that:
		 * the NEXT time the user clicks somewhere (specifying a destination location for
		 * the so-called "selected" Character,
		 * the next if-branch is executed. */
		if (snapShotState == 1) {
			repaint();
			snapShotState = 2;
			setState(2);
		}
		while (snapShotState == 2) {
		/****
		 * Process the animation, which will probably take many frames
		 * and thus justifies a while-loop.
		 * (Later we might find a better design for this.)
		 * 
		 * */
		//	snapShotState = getState();
			repaint();
			setState(3);
			snapShotState = 3;
		}
		if (snapShotState == 3) {
			move(highlighted, xInd, yInd);
			terrainImage[1]  = null;
			repaint();
			setState(0);
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

public class TmpGraphics extends JFrame {
	public TmpGraphics(ArrayList<Character> sideA, ArrayList<Character> sideB) {
		initUI(sideA, sideB);
	}

	private void initUI(ArrayList<Character> ally, ArrayList<Character> opponent) {
		WindowTest allGraphics = new WindowTest(ally, opponent);
		add(allGraphics);
		setTitle("Mid-Level Simulation");
		setSize(720, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] argv) {
		ArrayList<Character> sideOne = new ArrayList<Character>();
		sideOne.add(new Archer(0,1,0));
		sideOne.add(new Archer(0,2,0));

		ArrayList<Character> sideTwo = new ArrayList<Character>();
		sideTwo.add(new Soldier(3,1,1));
		sideTwo.add(new Soldier(3,2,1));

		GraphicsThread intermediary = new GraphicsThread(sideOne, sideTwo);
		EventQueue.invokeLater(intermediary);

/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GraphicsTest manager = new GraphicsTest();
				manager.setVisible(true);
			}
		});
*/
	}
}
