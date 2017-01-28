/*
 * is the main weapon class
 * all weapons are sub classes of this class
 * is where the target actually gets assigned damage to take
 */
public abstract class Weapon{
 int damage, armourPiercing;
 int k = 2; //This is the value that we will be shifting for balance purposes
 boolean ranged, melee
 public Weapon(int d, int aR){
 	damage = d; armourPiercing = aR;
 }
 public abstract void attack(Character attacker, Character target);
 //This takes into account the targets armor, the armor piercing of the attack and the damage of the weapon
 //if the armor piercing is greater than the armor, it is set to the difference is automatically set to zero
 //it then deals a random amount of damage between the min and max damage range, with the armor difference subtracted from that
 public void dealDamage(Character attacker, Character target, int modifiers){
		int armorAbsorption;
	 	if(armourPiercing >= target.armor) armorAbsorption = 0;
	 	else armorAbsorption = (target.armor - armourPiercing)*k;
	 	int attackPower = damage + attacker.strength + modifiers;
	 	if(attackPower - armorAbsorption <= 0){
			target.dealDamage(attackPower - armorAbsorption);	
		}
	}
}
