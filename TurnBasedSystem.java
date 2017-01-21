/*
 * Vincent Cozzo
 * A basic "game board" design,
 * partly inspired by the tutorial by Jan Bodnar fround at:
 * http://zetcode.com/gfx/java2d/basicdrawing/
 * 
 * 
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.*;

class WindowTest extends JPanel implements MouseListener, ActionListener {
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
	private boolean regenMap;
/*	private ArrayList<Character> entities;    */
/*	private Color storedColor;    */
	private int numLayers;
	private BufferedImage[] terrainImage;
	private Image[] terrainDefs;
	private int[][] terrainMap;
	private BufferedImage unitsImage;
	private Character highlighted;
	private int currentTeam;
	private int numMoved;
	private int state;
	private final int MAP_CHOICES = 2;

	//Every single object in the game, is made up of the enemies and friendlies arrayLists declared below
	Character[][] gameBoard;

	//ArrayList<ArrayList<Character>> entities = new ArrayList<ArrayList<Character>>();
	//Is sorted with characters with higher intiative ranking higher
	//ArrayList<Character> initiativeRanking = new ArrayList<Character>();
	//Is an array of all the friendly characters
	ArrayList<Character> friendlies;
	//Enemy Array list
	ArrayList<Character> enemies;
	ArrayList<Character> moved;

	//The following represents the width/height(game currently is square)
	public int tilesX = 8;
	public int tilesY = 8;
	private Timer watch;

	public WindowTest(ArrayList<Character> playerUnits, ArrayList<Character> enemyUnits) {
/*		entities = new ArrayList<Character>();
		entities.addAll(playerUnits);
		entities.addAll(enemyUnits);*/
		addMouseListener(this);
		state = 0;
		regenMap = true;
		numLayers = 3;
		currentTeam = 0; // start the game in Player Phase
		numMoved = 0;
		terrainImage = new BufferedImage[3];
		terrainMap = new int[numTiles][numTiles];
/*		terrainImage[0] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);*/
		unitsImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		friendlies = playerUnits;
		enemies = enemyUnits;
		moved = new ArrayList<Character>();

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
		watch = new Timer(1500, this);
		watch.setInitialDelay(2000);
		watch.start();
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
/*		* Now getting rid of this distance approach
		* Since it does not factor in whether the destination
		* is an ocean tile, etc.

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
*/
		/* */
		if ((0 == terrainMap[y][x]) ||
		    (((terrainImage[1].getRGB(x*tileSize, y*tileSize)) + 1387614848) != 0)) {
			return false;
		}
		return true;
	}

	//is the method called to move
	public boolean move(Character selected, int x, int y){
		if(moveTest(selected,x,y)){
			//alters the characters position
			gameBoard[selected.getY()][selected.getX()] = null;
			selected.move(x, y);
			gameBoard[y][x] = selected;
			return true;
		} else {
			return false;
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

	private void addTerrainToSquare(Graphics2D gm, int c, int r, int code) {
		double scaleFactor;
		AffineTransform placement = new AffineTransform();
		placement.translate((double) (c*tileSize), (double) (r*tileSize));
		scaleFactor = (((tileSize)) / (double) (terrainDefs[code].getWidth(this)));
		placement.scale(scaleFactor, scaleFactor);
		gm.drawImage(terrainDefs[code], placement, this);
	}

	public void drawTeamPhaseMessage(Graphics k, int num) {
		Graphics2D kAs2D = (Graphics2D) k;
		if (num == 0) {
			kAs2D.drawString("PLAYER PHASE", 5, 15);
		} else if (num == 1) {
			kAs2D.drawString("ENEMY PHASE", 5, 15);
		} else if (num == 2) {
			kAs2D.drawString("NEUTRAL PHASE", 5, 15);
		} else {
			kAs2D.drawString("TEAM NOT IDENTIFIED", 5, 15);
		}
	}

	public void drawBoard(Graphics manager) {
		Graphics2D kAs2D = (Graphics2D) manager;
		
		if (regenMap) {
			int choice = (int) (Math.random()*MAP_CHOICES);
			try {
				File randomMap = new File("maps/map" + choice + ".map");
				Scanner reader = new Scanner(randomMap);
				terrainDefs = new Image[7];
				terrainDefs[0] = (new ImageIcon("terrain/ocean.png")).getImage();
				terrainDefs[1] = (new ImageIcon("terrain/plains.png")).getImage();
				terrainDefs[2] = (new ImageIcon("terrain/forest.png")).getImage();
				// these next two images will be created later.
//				terrainDefs[3] = new ImageIcon("terrain/hills.png");
//				terrainDefs[4] = new ImageIcon("terrain/snow.png");
				terrainDefs[5] = (new ImageIcon("terrain/sand.png")).getImage();
				terrainDefs[6] = (new ImageIcon("terrain/mountain.png")).getImage();
				for (int y = 0; y < numTiles; y ++) {
					for (int x = 0; x < numTiles; x ++) {
/*
						int randomRed = (int) ((Math.random()*4096));
						int randomBlue = (int) ((Math.random()*4096));
						int randomGreen = (int) ((Math.random()*4096));
						int sum = (randomRed * randomBlue * randomGreen);
						if ((sum + 1587614848) != 0) {
							colorSquare(0, x*tileSize, y*tileSize, sum);
						} else {
							colorSquare(0, x*tileSize, y*tileSize, 122110000-sum);
						}
*/
						int nextCode;
						nextCode = reader.nextInt();
						terrainMap[y][x] = nextCode;
/*						System.out.println("ABOUT TO CALL addTerrainToSquare() on the code " + nextCode);*/
						addTerrainToSquare(kAs2D, x, y, nextCode);
					}
					kAs2D.drawLine(tileSize*y, 1, tileSize*y, height);
					kAs2D.drawLine(1, tileSize*y, width, tileSize*y);
				}
				reader.close();
				regenMap = false;
			} catch (FileNotFoundException j) {
				System.out.println("ERR -- image file not found");
				j.printStackTrace();
			}
		} else {
			for (int y = 0; y < numTiles; y ++) {
				for (int x = 0; x < numTiles; x ++) {
					int nextCode = terrainMap[y][x];
					addTerrainToSquare(kAs2D, x, y, nextCode);
				}
				kAs2D.drawLine(tileSize*y, 1, tileSize*y, height);
				kAs2D.drawLine(1, tileSize*y, width, tileSize*y);
			}
		}

		for (int ind=0; ind < numLayers; ind ++) {
/*			System.out.println("About to paint layer " + ind + " of the terrain image.");*/
			if (terrainImage[ind] == null) {
//				System.out.println("\tERR -- it's null!");
			} else {
				kAs2D.drawImage(terrainImage[ind], null, 0, 0);
			}
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
		result.translate((double) x, (double) y);

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
				if (terrainMap[yCoord][xCoord] == 0) {
					// No-op
				} else if (gameBoard[yCoord][xCoord] == null) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1387614848);
				} else if (gameBoard[yCoord][xCoord].getTeam() == currentTeam) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1387614848);
				}
			} else {
				if (terrainMap[yCoord][xCoord] == 0) {
					// No-op
				} else if (gameBoard[yCoord][xCoord] == null) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1387614848);
					showMoveRange(speed-1, xCoord-1, yCoord);
					showMoveRange(speed-1, xCoord+1, yCoord);
					showMoveRange(speed-1, xCoord, yCoord-1);
					showMoveRange(speed-1, xCoord, yCoord+1);
				} else if (gameBoard[yCoord][xCoord].getTeam() == currentTeam) {
					colorSquare(1, xCoord*tileSize, yCoord*tileSize, -1387614848);
					showMoveRange(speed-1, xCoord-1, yCoord);
					showMoveRange(speed-1, xCoord+1, yCoord);
					showMoveRange(speed-1, xCoord, yCoord-1);
					showMoveRange(speed-1, xCoord, yCoord+1);
				}
			}
		}
	}

	private int allUnitsMoved() {
		if (currentTeam == 0) {
			if (moved.size() != friendlies.size()) {
				return 0;
			}
			return 1;
/*
			for (int ind = 0; ind < friendlies.size(); ind ++) {
				if ( == ) {
					return 0;
				}
			}
			return 1;
*/
		} else if (currentTeam == 1) {
			if (moved.size() != enemies.size()) {
				return 0;
			}
			return 1;
		}
		return 1;
	}

	private boolean hasMoved(Character x) {
		for (int i = 0; i < moved.size();  i++) {
			if (x == moved.get(i)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void paintComponent(Graphics gm) {
		super.paintComponent(gm);
		drawBoard(gm);
		drawIcons(gm);
		drawTeamPhaseMessage(gm, currentTeam);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		System.out.println("TIMER WENT OFF JUST NOW!");
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
			if ((highlighted.getTeam() != currentTeam) ||
			    (hasMoved(highlighted))) {
				System.out.println("cannot move that unit.");
			} else {
				if (highlighted != null) {
					setState(1);
/*					System.out.println("Set state to 1 upon that click you just made!");*/
					terrainImage[1] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
/*					System.out.println("Initialized a new BufferedImage at index 1 of the terrain array!");
					System.out.println("showMoveRange() about to be called, at col = " + xInd + ", row = " + yInd);*/
					showMoveRange(highlighted.getSpeed(), xInd, yInd);
/*					System.out.println("Prepared the image of MOVE RANGE ahead of time");*/
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
			boolean moveStatus;
			moveStatus = move(highlighted, xInd, yInd);
			if (moveStatus) {
				moved.add(highlighted);
				terrainImage[1] = null;
				repaint();
				setState(0);
				int status = allUnitsMoved();
				if (status > 0) {
					currentTeam ++;
					currentTeam %= 2;
					moved.clear();
				} else {
					
				}
			}
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

public class TurnBasedSystem extends JFrame {
	public TurnBasedSystem(ArrayList<Character> sideA, ArrayList<Character> sideB) {
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
		sideOne.add(new Archer(2,5,0));
		sideOne.add(new Archer(2,6,0));

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
