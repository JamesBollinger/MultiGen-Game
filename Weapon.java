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
	public abstract void attack(Unit attacker, Unit target);

	//This takes into account the targets armor, the armor piercing of the attack and the damage of the weapon
	//if the armor piercing is greater than the armor, it is set to the difference is automatically set to zero
	//it then deals a random amount of damage between the min and max damage range, with the armor difference subtracted from that
	
    /* EDIT: Is this method still needed,
     * even if there is also an abstract attack() method?
     * 
     * If we must keep it, then it must be updated (with more parameters),
     * so that additional information (about the attacking unit)
     * could be "forwarded" (from the weapon class to the unit dealDamage method)
     * */
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
	

	public int getRange() {
		return range;
	}
}
