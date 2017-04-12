import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collection;
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
	protected String name;
	int health, strength, speed, dodge, armour, accuracy, initiative;
	protected LinkedList<Item> inventory;
	protected Weapon[] weapons;
	int team; //0 for friendly, 1 for enemy, -1 for no team
	boolean up = true;
	public Character(int x,int y,String img, int h, int s, int sp, int d, int ar,int ac, int i, int t){
		super(x,y,img);
		health = h; strength = s; speed = sp; dodge = d; armour = ar;accuracy = ac; initiative = i; team = t;
		inventory = new LinkedList<Item>();
		weapons = new Weapon[Entity.MAX_NUM_WEAPONS];
		name = new String("Unit(Team "+t+"):HP "+h+";SPD "+sp+";ACC "+ac);
	}

	public Character(int x,int y,String img, int h, int s, int sp, int d, int ar,int ac, int i, int t, Weapon primaryWeapon){
		super(x,y,img);
		health = h; strength = s; speed = sp; dodge = d; armour = ar;accuracy = ac; initiative = i; team = t;
		inventory = new LinkedList<Item>();
		weapons = new Weapon[Entity.MAX_NUM_WEAPONS];
		name = new String("Unit(Team "+t+"):HP "+h+";SPD "+sp+";ACC "+ac);
		weapons[0] = primaryWeapon;
	}

	public Character(int x,int y,String img, int h, int s, int sp, int d, int ar,int ac, int i, int t, Collection<Weapon> initWeapons){
		super(x,y,img);
		health = h; strength = s; speed = sp; dodge = d; armour = ar;accuracy = ac; initiative = i; team = t;
		inventory = new LinkedList<Item>();
		weapons = new Weapon[Entity.MAX_NUM_WEAPONS];
		name = new String("Unit(Team "+t+"):HP "+h+";SPD "+sp+";ACC "+ac);
		Iterator<Weapon> weaponIter = initWeapons.iterator();
		int ind=0;
		while ((ind < weapons.length)
				&& (weaponIter.hasNext())) {
			weapons[ind] = weaponIter.next();
			ind++;
		}
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
	public int getTeam(){
		return team;
	}
	public boolean isUp(){
		return up;
	}
	public int getInit(){
		return initiative;
	}
	public int getSpeed(){
		return speed;
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
		initiative += change;	
	}
	public LinkedList<Item> getItems() {
		return inventory;
	}
	public boolean hasItem() {
		return (inventory.size() > 0);
	}
	public void addItem(Item ele) {
		inventory.addLast(ele);
	}

	public Weapon[] getWeapons() {
		return weapons;
	}

	/* Precondition: fedWeapon is a fully constructed non-null Weapon object
	 * Postcondition: adds fedWeapon to this character's list of objects in possession
	 * If sudo is true and this character already owns a complete set of weapons,
	 *  then one weapon will be automatically, siently replaced.
	 * TODO: give the client an option of replacing an item of his/her choice
	 * (perhaps send another parameter to this function specifying what location
	 * should be replaced ?)
	 * returns true if the operation was successful, false if the weapon set 
	 * is already at max capacity.
	*/
	public boolean setWeapon(Weapon fedWeapon, boolean sudo) {
		int ind=0;
		while ((ind < weapons.length)
			&& (weapons[ind] != null)) {
			ind ++;
		}
		if (ind < weapons.length) {
			weapons[ind] = fedWeapon;
			return true;
		} else if (sudo) {
			weapons[0] = fedWeapon;
			return true;
		} else {
			return false;
		}
	}

	public void setWeapons(Collection<Weapon> weaponFeed) {
		Iterator<Weapon> weaponIter = weaponFeed.iterator();
		int ind=0;
		while ((ind < weapons.length)
				&& (weaponIter.hasNext())) {
			weapons[ind] = weaponIter.next();
			ind++;
		}
	}

	public String toString() {
		return name;
	}

	public String name() {
		return name;
	}
}
