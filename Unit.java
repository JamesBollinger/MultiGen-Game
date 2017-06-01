import java.util.LinkedList;
import java.util.ArrayList;
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
public abstract class Unit extends Entity {
	protected String name;
	protected int dodge;
	protected int health, strength, magicalStrength, hitSpeed;
	protected int luck, speed, agility, consitution;
	protected int armour, accuracy, magicalResistance, initiative;

	/* I recommend we replace these with a single array of type Weapon*/
	/*
	protected Weapon main;
	protected Weapon secondary;
	*/
	/* As for these boolean vars, 
	 * I imagine we should  either make melee/ranged
	 * a field or property of the Weapon class.
	 * However, if these variables are supposed to 
	 * represent whether a single Unit is *capable* of
	 * holding a melee weapon, then they can 
	 * stay members of this class. */
	protected boolean meleeWeapon, rangedWeapon = false;

	/* *** */
	private double k1 = .5; //temporary value
        private double k2 = .5; //temporary value
	protected ArrayList<String> statuses = new ArrayList<String>();
	protected LinkedList<Item> inventory;
	protected Weapon[] weapons;
	protected int team; //0 for friendly, 1 for enemy, -1 for no team
	boolean up = true;
	/*

	public Character(int x,int y,String img, int h, int s, int sp, int d, int ar,int ac, int i, int t){
	*/
	public Unit(int x, int y, String img, int h, int s, int ms,
			int hs, int sp, int l, int ag, int ar, int ac,
				int cs, int i, int t, int mR) {
		super(x,y,img);
		health = h;
		strength = s;
		speed = sp;
		agility = ag;
		armour = ar;
		accuracy = ac;
		initiative = i;
		team = t;
		luck = l;
		consitution = cs;
		magicalStrength = ms;
		hitSpeed =  hs;
		magicalResistance = mR;
		
		inventory = new LinkedList<Item>();
		weapons = new Weapon[Entity.MAX_NUM_WEAPONS];
		name = new String("Unit(Team "+t+"):HP "+h+";SPD "+sp+";ACC "+ac);
	}
	
	public Unit(int x, int y, String img, int h, int s, int ms,
			int hs, int sp, int l, int ag, int ar, int ac, int cs,
			int i, int t, int mR, Weapon primaryWeapon) {
		super(x,y,img);
		health = h;
		strength = s;
		speed = sp;
		agility = ag;
		armour = ar;
		accuracy = ac;
		initiative = i;
		team = t;
		luck = l;
		consitution = cs;
		magicalStrength = ms;
		hitSpeed =  hs;
		magicalResistance = mR;
		
		inventory = new LinkedList<Item>();
		weapons = new Weapon[Entity.MAX_NUM_WEAPONS];
		weapons[0] = primaryWeapon;
		name = new String("Unit(Team "+t+"):HP "+h+";SPD "+sp+";ACC "+ac);
	}
	
	public Unit(int x, int y, String img, int h, int s, int ms,
			int hs, int sp, int l, int ag, int ar, int ac, int cs,
			int i, int t, int mR, Collection<Weapon> initWeapons) {
		super(x,y,img);
		health = h;
		strength = s;
		speed = sp;
		agility = ag;
		armour = ar;
		accuracy = ac;
		initiative = i;
		team = t;
		luck = l;
		consitution = cs;
		magicalStrength = ms;
		hitSpeed =  hs;
		magicalResistance = mR;
		
		Iterator<Weapon> weaponIter = initWeapons.iterator();
		int ind=0;
		while ((ind < weapons.length)
				&& (weaponIter.hasNext())) {
			weapons[ind] = weaponIter.next();
			ind++;
		}
	}
	
	private boolean applyCritical(Unit attacker,int critModifier){
		int luckValue = (int) ((double)(k1*attacker.getLuck()+critModifier-k2*this.getLuck())/100);
		return (luckValue > Math.random());
	}
	public void dealDamage(int d, Unit attacker, int strength, int attackStrength, int critModifier) {
		System.out.println(d);
		if(applyCritical(attacker,critModifier)) {
			health -= (d*2 + 0.4*(attackStrength+strength));		
		} else {
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
	public int getSpeed(){
		return speed;
	}
	public int getArmour(){
		return armour;
	}
	public int getDodge(){
		return dodge;
	}
	public int getAccuracy(){
		return accuracy;
	}
	public int getLuck(){
		return luck;
	}
	public int getStrength(){
		return strength;
	}
	
	//All of these values should be inputed as either a plus of minus to change it up or down
	public void setStrength(int change){
		strength += change;	
	}
	public void setSpeed(int change){
		speed += change;	
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
	public void move(int x,int y){
		setX(x);
		setY(y);
	}
	//The attack methods have been combined, with which weapons and whether it is a ranged attack being down at a lower level
	//Actual computation of dodging and accuracy will be done at lower levels
	//will also assume it is a valid attack;
	/* EDIT: now, this method will actually run some internal
	 * checks to make sure it's a valid attack
	 * */
	public abstract void attack(Unit target, Weapon weapon);
	public void alter(Unit target, String stat, int change){
		if(stat.equals("dodge")) target.setDodge(change);
		else if(stat.equals("strength")) target.setStrength(change);
		else if(stat.equals("armour")) target.setArmour(change);
		else if(stat.equals("accuracy")) target.setAccuracy(change);
		else if(stat.equals("intiative")) target.setIntiative(change);
		else if(stat.equals("speed")) target.setSpeed(change);
		else if(stat.equals("hitSpeed")) target.setHitSpeed(change);
		else if(stat.equals("luck")) target.setLuck(change);
		else if(stat.equals("constitution")) target.setConsitution(change);
		else {
 	 		System.out.println("invalid attribute");
			throw new UnsupportedOperationException(stat+" is not a valid attribute.");
		}
	}
	
	//For the statuses it will simply add the status string to a string arrayList and then at lower levels in conjunction with action listener, block actions or add modifiers
	public void status(Unit target, String status){
		if(!checkExists(statuses,status))target.statuses.add(status);	
	}
	//I may think about creating a status class and creating a HashMap for this, but that would come later and not be hard to quickly implement
	public boolean checkExists(ArrayList<String> in, String input){
		for(int i = 0; i < in.size(); i++){
			if (input.equals(in.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public String removeElem(ArrayList<String> in, String input){
		for(int i = 0; i < in.size(); i++){
			if (input.equals(in.get(i))) {
				in.remove(i);
				return input;
			}
		}
		return null;
	}
	public void magicalAttack(Unit target, int spellPower, int modifiersOffensive, int modifiersDefensive, int critModifiers){
		target.dealDamage(spellPower + modifiersOffensive + magicalStrength - target.magicalResistance - modifiersDefensive,this,critModifiers, magicalStrength, spellPower);	
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
