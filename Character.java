/*
 * This is the main class that every entity currently is descended from
 * It contains the majority of methods that generally everyone would have to do(take damage, move, and not be down)
 * also contains the abstract attack which is defined differently for each type of character
 * also contains the values of health(hitpoints), strength(planned to be incorporated into damage calc),
 * speed(used is melee test, and to see how far the character can move), dodge(used to avoid attacks),
 * armour(used to soak attacks(i realized while typing these comments its meant to be spelled "armor" lol),
 * accuracy(used in ranged attack calculations), and initiative(used to determine when this character is allowed to move)
*/
public abstract class Character extends Entity {
	int health, strength, speed, dodge, armour, accuracy, intiative;
	int team; //0 for friendly, 1 for enemy, -1 for no team
	boolean up = true;
	public Character(int x,int y,String img, int h, int s, int sp, int d, int ar,int ac, int i, int t){
		super(x,y,img);
		health = h; strength = s; speed = sp; dodge = d; armour = ar;accuracy = ac; intiative = i; team = t;
	}
	public void move(int x,int y){this.x = x;this.y = y;}
	public abstract void attack(Character target);
	public void dealDamage(int d){
		System.out.println(d);
		health -= d;
		checkUp();
	}
	public void checkUp(){
		if(health <= 0)
			up = false;
	}
	public void getTeam(){
		return team;
	}
}
