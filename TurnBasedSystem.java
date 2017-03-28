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

class TacticalMapWindow extends JPanel implements MouseListener, ActionListener {
	/* 
	 * First, define some constants that are used
	 * throughout the class.
	 * It's better to have them all in one place,
	 * and adjust them later if we want a larger game board.
	 * Then all the references will be updated.
	 * 
	 * */
	private final int WIDTH = 640;
	private final int HEIGHT = 640;
	private final int TILE_SIZE  = 80; // number of pixels spanning the HEIGHT of each tile
	private final int MAP_CHOICES = 4; /* number of map choices possible */

	private int mouseState; // To determine what to do on mouse clicks... you'll see
	private boolean regenMap;
	private boolean showMenu;
	private Point menuPoint;
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
	private int mapFileChoice;

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

	//The following represents the WIDTH/HEIGHT(game currently is square)
	public int tilesX = 8;
	public int tilesY = 8;
	private Timer enemyAIWatch;
	private Timer animationWatch;

	public TacticalMapWindow(ArrayList<Character> playerUnits, ArrayList<Character> enemyUnits) {
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
		terrainMap = new int[tilesY][tilesX];
/*		terrainImage[0] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);*/
		unitsImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

		friendlies = playerUnits;
		enemies = enemyUnits;
		moved = new ArrayList<Character>();

		gameBoard = new Character[tilesY][tilesX];

		/* A new approach to "placing units on the map":
		 * what we need is a new function, to place these units.
		 * */
		mapFileChoice = (int) ((Math.random()*MAP_CHOICES));
		loadMapIcons();
		readMap();
		spawnUnits();

		enemyAIWatch = new Timer(350, this);
		enemyAIWatch.setInitialDelay(350);
		enemyAIWatch.setActionCommand("enemyAIWatch");
		enemyAIWatch.start();

		animationWatch = new Timer(300, this);
		animationWatch.setInitialDelay(300);
		animationWatch.setActionCommand("animationWatch");
		animationWatch.start();
	}

	private void spawnUnits() {
		for (int playerInd = 0; playerInd < friendlies.size(); playerInd ++) {
			Character nextUnit = friendlies.get(playerInd);
			boolean canPlace = false;
			for (int i=0; i < tilesY; i ++) {
				for (int j=0; j < tilesX; j++) {
					if ((terrainMap[i][j] != 0) && (gameBoard[i][j] == null)) {
						/* This tile CAN be used as a spawning tile.*/
						nextUnit.setX(j);
						nextUnit.setY(i);
						gameBoard[i][j] = nextUnit;
						canPlace = true;
						j=tilesX;
						i=tilesY;
					}
				}
			}
			if (!canPlace) {
				throw new UnsupportedOperationException("ERR -- not enough spaces for spawning player units");
			}
		}

		for (int enemyInd = 0; enemyInd < enemies.size(); enemyInd ++) {
			Character nextUnit = enemies.get(enemyInd);
			boolean canPlace = false;
			for (int i=(tilesY-1); i >= 0; i --) {
				for (int j=(tilesX-1); j >= 0; j --) {
					if ((terrainMap[i][j] != 0) && (gameBoard[i][j] == null)) {
						/* This tile CAN be used as a spawning tile.*/
						nextUnit.setX(j);
						nextUnit.setY(i);
						gameBoard[i][j] = nextUnit;
						canPlace = true;
						j=(-1);
						i=(-1);
					}
				}
			}
			if (!canPlace) {
				throw new UnsupportedOperationException("ERR -- not enough spaces for spawning enemy units");
			}
		}
	}

	private void loadMapIcons() {
		terrainDefs = new Image[7];
		terrainDefs[0] = (new ImageIcon("terrain/ocean.png")).getImage();
		terrainDefs[1] = (new ImageIcon("terrain/plains.png")).getImage();
		terrainDefs[2] = (new ImageIcon("terrain/forest.png")).getImage();
		/* these next two images will be created later. */
/*		terrainDefs[3] = new ImageIcon("terrain/hills.png");*/
/*		terrainDefs[4] = new ImageIcon("terrain/snow.png");*/
		terrainDefs[5] = (new ImageIcon("terrain/sand.png")).getImage();
		terrainDefs[6] = (new ImageIcon("terrain/mountain.png")).getImage();
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
		    (((terrainImage[1].getRGB(x*TILE_SIZE, y*TILE_SIZE)) + 1387614848) != 0)) {
			return false;
		}
		return true;
	}

	/* A simpler moveTest, for enemies.
	 * Does not depend on the size of a colored map,
	 * since the EnemyAI takes care of movement decisions.
	 * */
	private boolean moveTestEnemy(Character selected, int x, int y){
		if (x >= tilesX || y >= tilesY || x < 0 || y < 0)
			return false;
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

	//is the method called to move an ENEMY unit
	public boolean moveEnemy(Character selected, int x, int y){
		if(moveTestEnemy(selected,x,y)){
			//alters the characters position
			gameBoard[selected.getY()][selected.getX()] = null;
			selected.move(x, y);
			gameBoard[y][x] = selected;
			return true;
		} else {
			return false;
		}
	}

	/***
	 * Colors a particular square of the grid a particular color,
	 * on the given layer number (ranging from 0 to numLayers
	 * */
	private void colorSquare(int layer, int xInd, int yInd, int color) {
		int r = TILE_SIZE*(xInd / TILE_SIZE);
		int s = TILE_SIZE*(yInd / TILE_SIZE);
		for (int i = 0; i < TILE_SIZE; i ++) {
			for (int j = 0; j < TILE_SIZE; j ++) {
/*
				if ((i == 0) && (j == 0)) {
					System.out.println("\tpixel has been colored " + color);
				}
*/
				terrainImage[layer].setRGB(r+i, s+j, color);
			}
		}
	}

	private void addTerrainToSquare(Graphics2D gm, int c, int r, int code) {
		double scaleFactor;
		AffineTransform placement = new AffineTransform();
		placement.translate((double) (c*TILE_SIZE), (double) (r*TILE_SIZE));
		scaleFactor = (((TILE_SIZE)) / (double) (terrainDefs[code].getWidth(this)));
		placement.scale(scaleFactor, scaleFactor);
		gm.drawImage(terrainDefs[code], placement, this);
	}

	public void drawTeamPhaseMessage(Graphics k, int num) {
		Graphics2D kAs2D = (Graphics2D) k;
/*		System.out.println("Just converted graphics obj, about to switch/case on the team code");*/
		if (num == 0) {
/*			System.out.println("PLAYER PHASE about to type it up");*/
			kAs2D.drawString("PLAYER PHASE", 5, 15);
/*			System.out.println("That took too long.");*/
		} else if (num == 1) {
			kAs2D.drawString("ENEMY PHASE", 5, 15);
		} else if (num == 2) {
			kAs2D.drawString("NEUTRAL PHASE", 5, 15);
		} else {
			kAs2D.drawString("TEAM NOT IDENTIFIED", 5, 15);
		}
	}

	private void readMap() {
		try {
			File randomMap = new File("maps/map" + mapFileChoice + ".map");
			Scanner reader = new Scanner(randomMap);
			for (int y = 0; y < tilesY; y ++) {
				for (int x = 0; x < tilesX; x ++) {
					int nextCode;
					nextCode = reader.nextInt();
					terrainMap[y][x] = nextCode;
				}
			}
			reader.close();
			regenMap = false;
		} catch (FileNotFoundException j) {
			System.out.println("ERR -- image file not found");
			j.printStackTrace();
		}

	}

	private void renderMap(Graphics2D renderer) {
		for (int y = 0; y < tilesY; y ++) {
			for (int x = 0; x < tilesX; x ++) {
				addTerrainToSquare(renderer, x, y, terrainMap[y][x]);
			}
			renderer.drawLine(TILE_SIZE*y, 1, TILE_SIZE*y, HEIGHT);
			renderer.drawLine(1, TILE_SIZE*y, WIDTH, TILE_SIZE*y);
		}
	}

	public void drawBoard(Graphics manager) {
		Graphics2D kAs2D = (Graphics2D) manager;
		
		if (regenMap) {
			renderMap(kAs2D);
		} else {
			for (int y = 0; y < tilesY; y ++) {
				for (int x = 0; x < tilesX; x ++) {
					int nextCode = terrainMap[y][x];
					addTerrainToSquare(kAs2D, x, y, nextCode);
				}
				kAs2D.drawLine(TILE_SIZE*y, 1, TILE_SIZE*y, HEIGHT);
				kAs2D.drawLine(1, TILE_SIZE*y, WIDTH, TILE_SIZE*y);
			}
		}

/* in general, you can change the "2" (the upper bound of the conntrol variable
 * to be numLayers-1
 * (the final layer is treated separately, as a menu layer
 * */
		for (int ind=0; ind < 2; ind ++) {
/*			System.out.println("About to paint layer " + ind + " of the terrain image.");*/
			if (terrainImage[ind] == null) {
/*				System.out.println("\tERR -- it's null!");*/
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

	public void drawMenu(Graphics renderer) {
		Graphics2D rend2d = (Graphics2D) renderer;
		int yPos = (int) (menuPoint.getY());
		int xPos = (int) (menuPoint.getX());

		rend2d.drawImage(terrainImage[2], null, 0, 0);
		rend2d.drawString("Attack", xPos, yPos+18);
		rend2d.drawString("Item", xPos, yPos+36);
		rend2d.drawString("Trade", xPos, yPos+54);
		rend2d.drawString("Rescue", xPos, yPos+72);
		rend2d.drawString("Drop", xPos, yPos+90);
		rend2d.drawString("Pass", xPos, yPos+108);
		rend2d.drawString("Ability", xPos, yPos+126);
		rend2d.drawString("Wait", xPos, yPos+144);
		
	}

	private AffineTransform getTransformToResize(Character n) {
		double scaleFactor, difference;
		int x = TILE_SIZE*n.getX();
		int y = TILE_SIZE*n.getY();

		AffineTransform result = new AffineTransform();
		result.translate((double) x, (double) y);

		ImageIcon icon = n.getIcon();
		if (icon.getIconWidth() > icon.getIconHeight()) {
			scaleFactor = (((TILE_SIZE)) / (double) (icon.getIconWidth()));
			difference = (/*icon.getIconWidth()*/TILE_SIZE - (scaleFactor*(icon.getIconHeight())));
			if (difference <= 0) {
				difference = 0.0;
			} else {
				difference /= (2.0);
			}
			result.translate(0, difference);
		} else {
			scaleFactor = (TILE_SIZE / ((double) ((icon.getIconHeight()))));
			difference = (/*icon.getIconHeight()*/TILE_SIZE - (scaleFactor*(icon.getIconWidth())));
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
					colorSquare(1, xCoord*TILE_SIZE, yCoord*TILE_SIZE, -1387614848);
				} else if (gameBoard[yCoord][xCoord].getTeam() == currentTeam) {
					colorSquare(1, xCoord*TILE_SIZE, yCoord*TILE_SIZE, -1387614848);
				}
			} else {
				if (terrainMap[yCoord][xCoord] == 0) {
					// No-op
				} else if (gameBoard[yCoord][xCoord] == null) {
					colorSquare(1, xCoord*TILE_SIZE, yCoord*TILE_SIZE, -1387614848);
					showMoveRange(speed-1, xCoord-1, yCoord);
					showMoveRange(speed-1, xCoord+1, yCoord);
					showMoveRange(speed-1, xCoord, yCoord-1);
					showMoveRange(speed-1, xCoord, yCoord+1);
				} else if (gameBoard[yCoord][xCoord].getTeam() == currentTeam) {
					colorSquare(1, xCoord*TILE_SIZE, yCoord*TILE_SIZE, -1387614848);
					showMoveRange(speed-1, xCoord-1, yCoord);
					showMoveRange(speed-1, xCoord+1, yCoord);
					showMoveRange(speed-1, xCoord, yCoord-1);
					showMoveRange(speed-1, xCoord, yCoord+1);
				}
			}
		}
	}

	/* Renders the "halt menu" in the proper location
	 * (this is to be called in state 5, when we wait for the user 
	 * to decide what to do with a unit)
	 * */
	private void makeHaltMenu(int x, int y) {
		/* Make a 60x80 rectangle, filled with a light blue color */
		terrainImage[2] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		showMenu = true;
		int xVal = x;
		int yVal = y;
		int widthSpace = (WIDTH - xVal);
		int heightSpace = (yVal);
		if ((widthSpace < 120) && (heightSpace < 160)) {
			/* generate it in the lower-left rather than upper-right */
/*			System.out.println("\tabout to generate a blue menu in the lower-left");*/
			int yBound = (yVal + 160);
			int xBound = xVal;
			for (int i = yVal; i < yBound; i ++) {
				for (int j = (xBound-120); j < xBound; j ++) {
					terrainImage[2].setRGB(j, i, -1658327104); /* a light blue color */
				}
			}
			menuPoint = new Point(xBound-120, yVal);
		} else if ((widthSpace < 120) && (heightSpace >= (HEIGHT-160))) {
			/* show menu in the upper-left corner */
			for (int i = (yVal-160); i < yVal; i ++) {
				for (int j = (xVal-120); j < xVal; j ++) {
					terrainImage[2].setRGB(j, i, -1658327104); /* a light blue color */
				}
			}
			menuPoint = new Point(xVal-120, yVal-160);
		} else if ((widthSpace >= (WIDTH-120)) && (heightSpace < 160)) {
			/* show menu in the lower-right corner */
			int yBound = (yVal + 160);
			int xBound = (xVal + 120);
			for (int i = yVal; i < yBound; i ++) {
				for (int j = xVal; j < xBound; j ++) {
					terrainImage[2].setRGB(j, i, -1658327104); /* a light blue color */
				}
			}
			menuPoint = new Point(xVal, yVal);
		} else if (heightSpace < 160) {
			/* show menu in the lower-right corner */
			int yBound = (yVal + 160);
			int xBound = (xVal + 120);
			for (int i = yVal; i < yBound; i ++) {
				for (int j = xVal; j < xBound; j ++) {
					terrainImage[2].setRGB(j, i, -1658327104); /* a light blue color */
				}
			}
			menuPoint = new Point(xVal, yVal);
		} else if (widthSpace < 120) {
			/* show menu in the lower-left corner */
			int yBound = (yVal + 160);
			int xBound = xVal;
			for (int i = yVal; i < yBound; i ++) {
				for (int j = (xBound-120); j < xBound; j ++) {
					terrainImage[2].setRGB(j, i, -1658327104); /* a light blue color */
				}
			}
			menuPoint = new Point(xBound-120, yVal);
		} else {
/*			System.out.println("\tabout to generate a blue menu in the upper-right");*/
			int yBound = yVal;
			int xBound = (xVal + 120);
			for (int i = (yVal-160); i < yBound; i ++) {
				for (int j = xVal; j < xBound; j ++) {
					terrainImage[2].setRGB(j, i, -1658327104);
				}
			}
			menuPoint = new Point(xVal, yVal-160);
		}
	}

	private boolean allUnitsMoved() {
		if (currentTeam == 0) {
			if (moved.size() != friendlies.size()) {
				return false;
			}
			return true;
/*
			for (int ind = 0; ind < friendlies.size(); ind ++) {
				if ( == ) {
					return false;
				}
			}
			return true;
*/
		} else if (currentTeam == 1) {
			if (moved.size() != enemies.size()) {
				return false;
			}
			return true;
		}
		return true;
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
	/* Note: the many commented println statements
	 * were of course from a previous session of debugging. 
	 * Feel free to delete them if they no longer serve a 
	 * purpose
	 * */
		super.paintComponent(gm);
/*		System.out.println("\tabout to draw board");*/
		drawBoard(gm);
/*		System.out.println("\tabout to draw icons");*/
		drawIcons(gm);
/*		System.out.println("\tabout to draw team phase message");*/
		drawTeamPhaseMessage(gm, currentTeam);
		if (showMenu) {
			drawMenu(gm);
		}
/*		System.out.println("\tabout to finish drawing :P");*/
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
/*		System.out.println("TIMER WENT OFF JUST NOW!");*/
		String type = ev.getActionCommand();
		if (type.equals("enemyAIWatch")) {
			/* First, process the enemy AI if it is needed. */
			enemyAIWatch.stop();
			if (currentTeam == 1) {

				for (int enemyInd = 0; enemyInd < enemies.size(); enemyInd ++) {
					Character nextEn = enemies.get(enemyInd);
					EnemyAI mind = new EnemyAI(gameBoard, terrainMap, nextEn);
					EnemyMove res = mind.decide();
					System.out.println(res.toString());

					/* Now execute the EnemyMove action */
					Point recvDest = res.getDestination();
					Point recvTar = res.getTarget();
					int recvAct = res.getAction();
					if (res.getDestination() == null) {
						System.out.println("Error -- destination Point is null. Treating this as \"no movement\"");
					} else {
						int recvX = (int)(res.getDestination().getX());
						int recvY = (int)(res.getDestination().getY());
						boolean moveStatus;
						moveStatus = moveEnemy(nextEn, recvX, recvY);
						if (moveStatus) {
							moved.add(nextEn);
						} else {
							System.out.println("ERROR IN MOVING ENEMY "
							+ nextEn.toString() + " TO THE LOCATION (" +
							recvX + ", " + recvY + ")");
						}

					}
					/* ***
					 * Leaving some space here...
					 * so that we remember to deal with more complex actions,
					 * like giving / using an item, etc.
					 * */
					switch (recvAct) {
						case 0:
							System.out.println("Enemy unit " + enemyInd + " will wait.");
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						case 4:
							
							break;
						case 5:
							
							break;
						case 6:
							
							break;
						case 7:
							
							break;
						default:
							
							break;
					}
					repaint();
					try {
						Thread.sleep(500);
					} catch (InterruptedException j) {
						System.out.println("ERR -- Thread interrupted. Cannot sleep. Whoops.");
						j.printStackTrace();
					}
				}
				moved.clear();
				currentTeam = 0;
			}
			enemyAIWatch.start();
	
		} else if (type.equals("animationWatch")) {
			animationWatch.stop();
			/* --- TO DO: Fill in the animaton frame update --- */
			animationWatch.start();
		}
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		int xClick = ev.getX();
		int yClick = ev.getY();
		int xInd = (xClick / TILE_SIZE);
		int yInd = (yClick / TILE_SIZE);
/*
		System.out.println("Mouse click occurred at (" + xClick + ", " + yClick + ").");
*/
		int snapShotState = getState();
		if (snapShotState == 0) {
			highlighted = gameBoard[yInd][xInd];
			if ((highlighted.getTeam() != 0) ||
			    (hasMoved(highlighted))) {
				System.out.println("cannot move that unit.");
			} else {
				if (highlighted != null) {
					setState(1);
/*					System.out.println("Set state to 1 upon that click you just made!");*/
					terrainImage[1] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
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
		/* Idea: probably change this to an if, or better yet, 
		 * a switch/case structure...
		 * 
		 * but I will not change it yet, until I get
		 * some basic animation working first...
		 * */
		while (snapShotState == 2) {
//			snapShotState = getState();
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
				setState(4);
				snapShotState = 4;
			}
		}
		if (snapShotState == 4) {
			makeHaltMenu(xClick, yClick);
			repaint();
			setState(5);
		} else if (snapShotState == 5) {
			showMenu = false;
			terrainImage[2] = null;
			/* Finalize the move */
			repaint();
			if (allUnitsMoved()) {
				currentTeam ++;
				currentTeam %= 2;
				moved.clear();
			} else {
/*				System.out.println("Not all player units have moved, apparently.");*/
			}
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

public class TurnBasedSystem extends JFrame {
	public TurnBasedSystem(ArrayList<Character> sideA, ArrayList<Character> sideB) {
		initUI(sideA, sideB);
	}

	private void initUI(ArrayList<Character> ally, ArrayList<Character> opponent) {
		TacticalMapWindow allGraphics = new TacticalMapWindow(ally, opponent);
		add(allGraphics);
		setTitle("Mid-Level Simulation");
		setSize(720, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] argv) {
		ArrayList<Character> sideOne = new ArrayList<Character>();
		sideOne.add(new Archer(3,5,0));
		sideOne.add(new Archer(3,6,0));
		sideOne.add(new Archer(4,6,0));

		ArrayList<Character> sideTwo = new ArrayList<Character>();
		sideTwo.add(new Soldier(4,3,1));
		sideTwo.add(new Soldier(5,3,1));

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

