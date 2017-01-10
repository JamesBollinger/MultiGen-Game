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
	ArrayList<ArrayList<Character>> entities = new ArrayList<ArrayList<Character>>();
	//Is sorted with characters with higher intiative ranking higher
	ArrayList<Character> intiativeRanking = new ArrayList<Character>();
	//Is an array of all the friendly characters
	ArrayList<Character> friendlies;
	//Enemy Array list
	ArrayList<Character> enemies;
	//Is the width/height(game currently is square)
	public int tiles = 5;
	Scanner in = new Scanner(System.in);
	//Takes in the arrayLists, is designed for latter implementation in which they will be chosen before hand
	public Battle(ArrayList<Character> friendlies, ArrayList<Character> enemies){
		this.friendlies = friendlies;
		this.enemies = enemies;
		entities.add(friendlies);
		entities.add(enemies);
		//Arranges the characters by intiative
		assignOrder();
	}
	//This searches through the entire arrayList to see if there is an entity at a location, returning it, or null if nothing is there
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
	//Prints the board
	public String toString(){
		String ret = "";
		for(int i = 0; i < tiles; i++){
			for(int j = 0; j < tiles; j++){
				if(search(j,i) != null)
				ret = ret + search(j,i) + " ";
				else ret = ret + "# ";
			}
			ret = ret + "\n";
		}
		return ret;
	}
	//checks to see if a move is valid
	private boolean moveTest(Character selected, int x, int y){
		if(x >= tiles || y >= tiles || x < 0 || y < 0)
			return false;
		double distance = Math.sqrt(Math.pow((selected.x-x), 2) + Math.pow(selected.y-y, 2));
		if(distance > selected.speed){
			System.out.println("distance too far");
			return false;
		}
		if(this.search(x,y) != null){
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
			selected.move(x, y);
		}
	}
	//is used to assess if team is down
	//returns -1 if entire team is down
	//returns the team that is entirely down
	private int checkTeamStatus(){
		for(int i = 0; i < entities.size(); i++){
			boolean ret = true;
			for(int j = 0; j < entities.get(i).size(); j++){
				if(entities.get(i).get(j).up){
					ret = false;
				}
			}
			if(ret)return i;
		}
		return -1;
	}
	//Is used solely in the production of the character
	public void makeAction(Character actor){
		//System.out.println("moving about");
		int x = in.nextInt();
		int y = in.nextInt();
		System.out.println(actor.x + " " + actor.y);
		move(actor,x,y);
	}
	//This is the game loop, designed to run until as single team is utterly defeated
	public void play(){
		while(checkTeamStatus() == -1){
			for(int i = 0; i < intiativeRanking.size(); i++){
				makeAction(intiativeRanking.get(i));
				System.out.println(this);
				System.out.println();
			}
			assignOrder();
		}
	}
	//This is a loop designed to get the intiatives ranked in order
	//Not 100% that it is working
	private void order(){
		for(int j = 0; j < intiativeRanking.size(); j++){
			for(int i = j; i < intiativeRanking.size(); i++){
				if(intiativeRanking.get(j).intiative > intiativeRanking.get(i).intiative){
					intiativeRanking.add(intiativeRanking.get(i));
					intiativeRanking.set(i, intiativeRanking.remove(j));
					j--;
					break;
				}
			}
		}
	}
	//This is designed to set the order to the intiatives
	private void assignOrder(){
		for(int i = 0; i < entities.size(); i++){
			for(int j = 0; j < entities.get(i).size(); j++){
				intiativeRanking.add(entities.get(i).get(j));
			}
		}
		order();
	}
	//How I actually run and test the program
	public static void main(String[] args){
		ArrayList<Character> one = new ArrayList<Character>(); one.add(new Archer(0,1)); one.add(new Archer(0,2));
		ArrayList<Character> two = new ArrayList<Character>(); two.add(new Soldier(3,1)); two.add(new Soldier(3,2));
		Battle fight = new Battle(one,two);
		System.out.println(fight);
		fight.play();
	}
}
