/*
 * This is the main test class, it is designed to handle the logic
 * of the actual game, regarding grid placement and multiple combatents
 * uses a work in progress intiative system
 * Movement, placement and attacking all work
 * Have to hardcode them to do those movements however at the current time
 */

import java.util.ArrayList;
import java.util.*;
public class Battle {
	//Every single object in the game, is made up of the enemies and friendlies arrayLists declared below
	Unit[][] gameBoard;

	// a List for all Entities currently in the battle;
	// including friendly, enemy, and (possibly) neutral:
	// I would argue this is needed for 
	// the purpose of determining, efficiently, which character has the next lowest initiative. --VC
	ArrayList<Unit> initiativeRanking;

	//ArrayList<ArrayList<Unit>> entities = new ArrayList<ArrayList<Unit>>();
	//Is sorted with characters with higher intiative ranking higher
	//ArrayList<Unit> initiativeRanking = new ArrayList<Unit>();
	//Is an array of all the friendly characters
	//ArrayList<Unit> friendlies;
	//Enemy Array list
	//ArrayList<Unit> enemies;
	//Is the width/height(game currently is square)
	public int tilesX = 50;
	public int tilesY = 50;
	Scanner in = new Scanner(System.in);
	//Takes in the arrayLists, is designed for latter implementation in which they will be chosen before hand
	public Battle(ArrayList<Unit> friendlies, ArrayList<Unit> enemies){
		/* I'm not sure if we'll still be keeping these lists separate
		 * (the list of friendly entities versus enemy units) 
		 * given that we're using a 2d array for the game board, and each entity knows its "Team"
		 *
		 * However, I would argue that we *could* still have use
		 * for a general list of entities, in addition to the game baord.
		 * 
		 * Using a 2d array provides efficiency for some tasks, like
		 * determining the Team of a unit on an arbitrary square of the board.
		 *
		 * However, my concern is that the 2d array by itself
		 *  does NOT provide efficiency for other tasks, e.g.
		 *  processing the entities in order of their initiative.
		 *  (To get the unit with the next-lowest initiative, one would have to check all (tiles)^2
		 *    Depending on how we might store an entity list, getting
		 *    this next element could be far more efficient
		 *    than cycling through each square of the matrix/ 2d array.)
		 * This is somewhat complicated to prove, but the idea is that
		 * the number of comparisons done on average could be far less if we store entities in a heap
		 * versus if we only have a 2d array
		 * Using a 2d array: there are exactly t^2 comparisons to find the next
		 * max-initiative unit;
		 * Using a heap to store all entities: there are 0 comparisons to find the next max-initiative,
		 * plus the log(n) comparisons to maintain the heap invariant.
		 * 
		 * In either case, we'll also need to consider how this
		 * Constructor is going to be changed...
		 * i.e., will the "caller" be giving this class Lists of friendly and enemy units
		 * separately?
		 * (This is very possibly the way we'll do it, but it's something we'll need to think about.)
		 * */
/*
		this.friendlies = friendlies;
		this.enemies = enemies;
		entities.add(friendlies);
		entities.add(enemies);
*/
		gameBoard = new Unit[tilesY][tilesX];
		initiativeRanking = new ArrayList<Unit>();
		initiativeRanking.addAll(friendlies);
		initiativeRanking.addAll(enemies);
		
		/* Now for the fun part:
		 * Using the data from the entities, set them on the map! */
		
		System.out.println("Entities List - at end of constructor -- " + initiativeRanking.toString());

		for (int mapInd = 0; mapInd < initiativeRanking.size(); mapInd ++) {
			int nextX = initiativeRanking.get(mapInd).getX();
			int nextY = initiativeRanking.get(mapInd).getY();

			/* Note: I may want to clarify what X and Y represent
			* (because it's a grid, we may want to either
			* rename X and Y as 'row' and 'column'
			* or at least make sure we're on the same page 
			* on what X and Y represent */
			if (gameBoard[nextY][nextX] == null) {
				gameBoard[nextY][nextX] = initiativeRanking.get(mapInd);
			} else {
				throw new UnsupportedOperationException("ERR -- two units are on the same tile???");
			}
		}
		//Arranges the characters by intiative
//		assignOrder();
	}
	//This searches through the entire arrayList to see if there is an entity at a location, returning it, or null if nothing is there
	/*
	public Entity search(int p, int q){
		for(int j = 0; j < entities.size(); j ++){
			for(int i = 0; i < entities.get(j).size(); i++){
				if(entities.get(j).get(i).x == p && entities.get(j).get(i).y == q && entities.get(j).get(i).up){
					return entities.get(j).get(i);
				}
			}
		}
		return null;
	}
	*/
	// replacement for the preivous search
	public Unit recall(int p, int q){
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
	private boolean moveTest(Unit selected, int x, int y){
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
		if(distance > selected.getSpeed()){
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
	public void move(Unit selected, int x, int y){
		if(moveTest(selected,x,y)){
			//alters the characters position
			gameBoard[selected.getY()][selected.getX()] = null;
			selected.move(x, y);
			gameBoard[y][x] = selected;
		}
	}
	//returns the team down(0 for friendly, 1 for enemy)
	//if no team is down it return -1
	//if both teams are down it returns -2(simply so that if this happens we can record it)
	private int checkTeamStatus(){
		boolean checkTeam0 = false;
		boolean checkTeam1 = false;
		for(int i = 0; i < tilesY; i++){
			for(int j = 0; j < tilesX; j++){
				if(recall(i,j) != null && recall(i,j).up){
					if(recall(i,j).team == 0){
						checkTeam0 = true;	
					}
					else if(recall(i,j).team == 1){
						checkTeam1 = true;	
					}
				}
			}
		}
		if(!checkTeam0 && checkTeam1) return 0;
		if(!checkTeam1 && checkTeam0) return 1;
		if(!checkTeam0 && !checkTeam1) return 2;
		return -1;
	}
	//Is used solely in the production of the character
	public void makeAction(Unit actor){
		//System.out.println("moving about");
		System.out.print("Enter the destination column value: ");
		int x = in.nextInt();
		System.out.print("Enter the destination row value: ");
		int y = in.nextInt();
		System.out.println("About to move to col " + actor.getX() + ", row " + actor.getY());
		move(actor,x,y);
	}
	//This is the game loop, designed to run until as single team is utterly defeated
	public void play(){
		while(checkTeamStatus() == -1){
			for(int i = 0; i < initiativeRanking.size(); i++){
				makeAction(initiativeRanking.get(i));
				System.out.println(this);
				System.out.println();
			}
			assignOrder();
		}
	}
	//So i left this untouched in case you wanted to edit, but besides that I have pretty much removed lots of the arrayLists, and replaced it with the 2D array for O(1) time
/*
	private void order(){
		for(int j = 0; j < initiativeRanking.size(); j++){
			for(int i = j; i < initiativeRanking.size(); i++){
				if(initiativeRanking.get(j).intiative > initiativeRanking.get(i).intiative){
					initiativeRanking.add(initiativeRanking.get(i));
					initiativeRanking.set(i, initiativeRanking.remove(j));
					j--;
					break;
				}
			}
		}
	}
*/
	private ArrayList<Unit> heapify(ArrayList<Unit> inputList){
		int index = inputList.size() - 1;
		if ((index % 2) == 1) {
			/* make one initial comparison beforehand, then
			 * decrement index to make sure that index begins the
			 * routine as an even integer.
			 * */
			Unit lastEle = inputList.get(index);
			int parentInd = ((index - 1) / 2);
			Unit parent = inputList.get(parentInd);
			if ((lastEle.getInit() < (parent.getInit()))) {
				/* swap the child and parent Objects */
				Unit tmp = parent;
				inputList.set(parentInd, lastEle);
				inputList.set(index, tmp);
			}
			index --;
		}
		while (index > 0) {
			int childIndex;
			Unit lesserChild;
			if (inputList.get(index-1).getInit() < (inputList.get(index).getInit())) {
				lesserChild = inputList.get(index-1);
				childIndex = index-1;
			} else {
				lesserChild = inputList.get(index);
				childIndex = index;
			}
			int parentInd = ((index - 1) / 2);
			Unit parent = inputList.get(parentInd);
			if ((lesserChild.getInit() < (parent.getInit()))) {
				/* swap the child and parent Objects */
				Unit tmp = parent;
				inputList.set(parentInd, lesserChild);
				inputList.set(childIndex, tmp);
				/* NOTE: so that I don't forget!!!
				 * There is an important step in here, 
				 * where you keep sifting down the element
				 * if it is larger than its children
				 * it's not as simple as "do one swap"
				 * so remember to keep checking!
				 * * */
				inputList = siftDown(inputList, childIndex);
			}
			index -= 2;
		}
/*		System.out.println("Heapify'd version is " + inputList.toString());	*/
		return inputList;
	}

	private ArrayList<Unit> siftDown(ArrayList<Unit> preSift, int startInd){
		int size = preSift.size();
		int currentInd = startInd;
		int leftInd = (2*startInd) + 1;
		int rightInd = (2*startInd) + 2;
		while ((rightInd < size) && (preSift.get(rightInd) != null)) {
			/*
			 * Get the minimum of the two children
			 * and compare to the current node
			 *  
			 * */
			Unit lesserChild;
			int nextCurrent;
			if (preSift.get(leftInd).getInit() < (preSift.get(rightInd).getInit())) {
				lesserChild = preSift.get(leftInd);
				nextCurrent = leftInd;
			} else {
				lesserChild = preSift.get(rightInd);
				nextCurrent = rightInd;
			}

			if (lesserChild.getInit() < (preSift.get(currentInd).getInit())) {
				/* swap the relevant data entries */
				Unit tmp = preSift.get(currentInd);
				preSift.set(currentInd, lesserChild);
				preSift.set(nextCurrent, tmp);
				/* and continue sifting down      */
				currentInd = nextCurrent;
				leftInd = ((2*currentInd) + 1);
				rightInd = ((2*currentInd) + 2);
				System.out.println("Now continuing to sift down using " + currentInd + " and the daughter node indices " + leftInd + " / " + rightInd);
			} else {
				/* this will cause the loop to stop sifting, as it should.   */
				rightInd = size;
				leftInd = size;
			}
		}
		if ((leftInd < size) && (preSift.get(leftInd) != null)) {
			Unit lesserChild = preSift.get(leftInd);
			int nextCurrent = leftInd;
			if (lesserChild.getInit() < (preSift.get(currentInd).getInit())) {
				/* swap the relevant data entries */
				Unit tmp = preSift.get(currentInd);
				preSift.set(currentInd, lesserChild);
				preSift.set(nextCurrent, tmp);
			}
		}
		return preSift;
	}

	private ArrayList<Unit> heapsort(ArrayList<Unit> input){
		int size = input.size();
		ArrayList<Unit> inter = heapify(input);
		ArrayList<Unit> result = new ArrayList<Unit>(size);
		for (int ind = 0; ind < size; ind ++) {
			result.add(null);
		}
		while (size > 0) {
			Unit tmp = inter.get(0);
			inter.set(0, inter.get(size-1));
			inter.set(size-1, null);
			result.set(size-1, tmp);
			inter = siftDown(inter, 0);
			size--;
		}
		return result;
	}
	// Calls heapsort.
	private void order() {
		initiativeRanking = heapsort(initiativeRanking);
	}

	//This is designed to set the order to the intiatives
	private void assignOrder(){
		initiativeRanking.clear();
		for(int i = 0; i < tilesY; i++){
			for(int j = 0; j < tilesX; j++){
				if(recall(i,j) != null){
					initiativeRanking.add(recall(i,j));
				}
			}
		}
		order();
	}
	//How I actually run and test the program
	public static void main(String[] args){
		ArrayList<Unit> one = new ArrayList<Unit>(); one.add(new Archer(0, 1, 0)); one.add(new Archer(0,2,0));
		ArrayList<Unit> two = new ArrayList<Unit>(); two.add(new Soldier(3,1, 1)); two.add(new Soldier(3,2,1));
		Battle fight = new Battle(one,two);
		System.out.println(fight);
		fight.play();
	}
}
