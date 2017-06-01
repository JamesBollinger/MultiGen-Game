/*
 * is the main weapon class
 * all weapons are sub classes of this class
 * is where the target actually gets assigned damage to take
 */
public abstract class Weapon{
	int minDamage, maxDamage, armourPiercing, range;
	public Weapon(int mI,int mD, int aR){
	 	minDamage = mI; maxDamage = mD; armourPiercing = aR;
		// Make 1 the default range?
		range = 1;
	}

	public Weapon(int mI,int mD, int aR, int givenRange){
	 	minDamage = mI; maxDamage = mD; armourPiercing = aR;
		range = givenRange;
	}

    /*
     * EDIT: replaced by use of the attack() method in Unit.
     */
    /*
	public abstract void attack(Unit attacker, Unit target);
    */

	//This takes into account the targets armor, the armor piercing of the attack and the damage of the weapon
	//if the armor piercing is greater than the armor, it is set to the difference is automatically set to zero
	//it then deals a random amount of damage between the min and max damage range, with the armor difference subtracted from that
	
    /* EDIT: Is this method still needed,
     * even if there is also an abstract attack() method?
     * 
     * If we must keep it, then it must be updated (with more parameters),
     * so that additional information (about the attacking unit)
     * could be "forwarded" (from the weapon class to the unit dealDamage method)
     * 
     * EDIT: this has also been refactored into the Unit class.
     * */
    /*
	protected void dealDamage(Unit target, Unit attacker){
		int armourDifference = armourPiercing - target.armour;
		if(armourDifference > 0) armourDifference = 0;
		target.dealDamage(
		((int)(Math.random()*(maxDamage-minDamage)) + minDamage - armourDifference),
		attacker,
		attacker.getStrength(),
		maxDamage,
		0);
	}
	*/

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
