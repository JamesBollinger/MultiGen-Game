/*
 * is the main weapon class
 * all weapons are sub classes of this class
 * is where the target actually gets assigned damage to take
 */
public abstract class Weapon{
	protected int minDamage, maxDamage, armourPiercing, range;
	public Weapon(int mI,int mD, int aR){
	 	minDamage = mI; maxDamage = mD; armourPiercing = aR;
		// Make 1 the default range?
		range = 1;
	}

	public Weapon(int mI,int mD, int aR, int givenRange){
	 	minDamage = mI; maxDamage = mD; armourPiercing = aR;
		range = givenRange;
	}

	public int getMinDamage() {
		return minDamage;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public int getAP() {
		return armourPiercing;
	}

	public int getRange() {
		return range;
	}

	/* Transferred from Bow class; now a static method.
	 * Precondition: The stats given are valid (i.e. positive integers).
	 * Postcondition: will return true iff. a weapon attack actually hits
	 * (and will return false otherwise), based on the dodge stats, modifiers, etc.
	 * */
	public static boolean accuracyTest(int range, int dodge, int accuracy, int additions){
		if ((double)range / ((double)accuracy + additions) <= Math.random()){
			boolean check = ((int)(Math.random()*11) + 1 >= dodge);
			return check;
		}
		return false;
	}

	//this checks if the target can dodge
	//if the target's dodge is over three times the attackers speed this will always miss
	//the more that the attackers speed is above the targets dodge value the more it will succeed
	public static boolean dodgeTest(Unit attacker, Unit target){
		return ((double)target.getDodge()/(double)target.getSpeed() <= Math.random()*3);
	}
}
