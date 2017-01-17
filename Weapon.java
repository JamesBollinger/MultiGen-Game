/*
 * is the main weapon class
 * all weapons are sub classes of this class
 * is where the target actually gets assigned damage to take
 */
public abstract class Weapon{
	int minDamage, maxDamage, armourPiercing;
	public Weapon(int mI,int mD, int aR){
	 	minDamage = mI; maxDamage = mD; armourPiercing = aR;
	}
	public abstract void attack(Character attacker, Character target);

	//This takes into account the targets armor, the armor piercing of the attack and the damage of the weapon
	//if the armor piercing is greater than the armor, it is set to the difference is automatically set to zero
	//it then deals a random amount of damage between the min and max damage range, with the armor difference subtracted from that
	public void dealDamage(Character target){
		int amourDifference = armourPiercing - target.armour;
		if(amourDifference > 0) amourDifference = 0;
		target.dealDamage(((int)(Math.random()*(maxDamage-minDamage)) + minDamage - amourDifference));
	}
}
