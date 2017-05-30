/*
 * is the sword class
 * this governs the attack and how effective it is
 */
public class Sword extends Weapon {
	public Sword(int mI,int mD, int aR){
		super(mI,mD,aR);
	}
	//this checks if the target can dodge
	//if the targets dodge is over three times the attackers speed this will always miss
	//the more that the attackers speed is above the targets dodge value the more it will succeed
	private boolean dodgeTest(Unit attacker, Unit target){
		return ((double)target.dodge/(double)target.speed <= Math.random()*3);
	}
	public void attack(Unit attacker, Unit target){
		if(dodgeTest(attacker,target)){
/*			dealDamage(target);*/
			int armourDifference = armourPiercing - target.armour;
			if(armourDifference > 0) armourDifference = 0;
			target.dealDamage(
				((int)(Math.random()*(maxDamage-minDamage)) + minDamage - armourDifference),
				attacker,
				attacker.getStrength(),
				maxDamage,
				0);
/*			System.out.println("hit");*/
		}
	}
}
