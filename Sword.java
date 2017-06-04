/*
 * is the sword class
 * this governs the attack and how effective it is
 */
public class Sword extends Weapon {
	public Sword(int mI,int mD, int aR){
		super(mI,mD,aR);
	}

	public String toString() {
		return ("SWORD, "+minDamage+" to "+maxDamage);
	}
}
