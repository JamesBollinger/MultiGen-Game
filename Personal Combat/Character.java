import java.util.*;
/*
 * This is the main class that every entity currently is descended from
 * It contains the majority of methods that generally everyone would have to do(take damage, move, and not be down)
 * also contains the abstract attack which is defined differently for each type of character
 * also contains the values of health(hitpoints), strength(planned to be incorporated into damage calc),
 * speed(used is melee test, and to see how far the character can move), dodge(used to avoid attacks),
 * armour(used to soak attacks(i realized while typing these comments its meant to be spelled "armor" lol)),
 * accuracy(used in ranged attack calculations), and initiative(used to determine when this character is allowed to move)
*/
public abstract class Character extends Entity {
	int health, strength, magicalStrength, hitSpeed, luck, speed, agility, consitution, armour, accuracy, magicalResistance initiative;
	int team; //0 for friendly, 1 for enemy, -1 for no team
	boolean up = true;
	boolean meleeWeapon, rangedWeapon = false;
	Weapon main;
	Weapon secondary;
	double k1 = .5; //temporary value
        double k2 = .5; //temporary value
	ArrayList<String> statuses = new ArrayList<String>();
	public Character(int x,int y,String img, int h, int s, int ms, int hs, int sp, int l, int ag, int ar,int ac, int cs, int i, int t, int mR){
		super(x,y,img);
		health = h; strength = s; speed = sp; agility = ag; armour = ar;accuracy = ac; initiative = i; team = t; luck = l; consitution = cs; magicalStrength = ms; hitSpeed =  hs; magicalResitance = mR;
	}
	private boolean applyCritical(Character attacker,int critModifier){
		int luckValue = ((double)(k1*attacker.luck+critModifier-k2*this.luck)/100);
		return (luckValue > Math.random());
	}
	public void dealDamage(int d,Character attacker,int strength,int attackStrength,int critModifier){
		System.out.println(d);
		if(applyCritical(attacker,critModifier){
			health -= d*2 + 0.4(attackStrength+strength);		
		}else{
			health -= d;
		}
		checkUp();

	}
	public void checkUp(){
		if(health <= 0)
			up = false;
	}
	public int getTeam(){
		return team;
	}
	public boolean isUp(){
		return up;
	}
	public int getInit(){
		return initiative;
	}
	//All of these values should be inputed as either a plus of minus to change it up or down
	public void setStrength(int change){
		strength += change;	
	}
	public void setSpeed(int change){
		speed += change;	
	}
	public void setDodge(int change){
		dodge += change;	
	}
	public void setArmour(int change){
		armour += change;	
	}
	public void setAccuracy(int change){
		accuracy += change;	
	}
	public void setIntiative(int change){
		intiative += change;	
	}
	public void setMagicalStrength(int change){
		magicalStrength += change;	
	}
	public void setLuck(int change){
		luck += change;	
	}
	public void setConsitution(int change){
		consitution += change;	
	}
	public void setHitSpeed(int change){
		hitSpeed += change;	
	}	
	//Basic Combat Interactions
	public void move(int x,int y){this.x = x;this.y = y;}
	//The attack methods have been combined, with which weapons and whether it is a ranged attack being down at a lower level
	//Actual computation of dodging and accuracy will be done at lower levels
	//will also assume it is a valid attack;
	public void attack(Character target, Weapon weapon){
		weapon.attack(this,target);
	}
	public void alter(Character target, String stat, int change){
		if(stat.equals("dodge")) target.setDodge(change);
		else if(stat.equals("strength")) target.setStrength(change);
		else if(stat.equals("armour")) target.setArmour(change);
		else if(stat.equals("accuracy")) target.setAccuracy(change);
		else if(stat.equals("intiative")) target.setIntiative(change);
		else if(stat.equals("speed")) target.setSpeed(change);
		else if(stat.equals("hitSpeed")) target.setHitSpeed(change);
		else if(stat.equals("luck")) target.setLuck(change);
		else if(stat.equals("constitution")) target.setConsitution(change);
		else System.out.println("invalid attribute");
	}
	//For the statuses it will simply add the status string to a string arrayList and then at lower levels in conjunction with action listener, block actions or add modifiers
	public void status(Character target, String status){
		if(!checkExists(statuses,status))target.statuses.add(status);	
	}
	//I may think about creating a status class and creating a HashMap for this, but that would come later and not be hard to quickly implement
	public boolean checkExists(ArrayList<String> in, String input){
		for(int i = 0; i < in.size(); i++){
			if(input.equals(in.get(i)) return true;
		}
		return false;
	}
	public String removeElem(ArrayList<String> in, String input){
		for(int i = 0; i < in.size(); i++){
			if(input.equals(in.get(i)){
				in.remove(i);
				return input;
			}
		}
		return null;
	}
	public void magicalAttack(Character target, int spellPower, int modifiersOffensive, int modifiersDefensive, int critModifiers){
		target.dealDamage(spellPower + modifiersOffensive + magicalStrength - target.magicalResistance - modifiersDefensive,this,critModifiers);	
	}
}
