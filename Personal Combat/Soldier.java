/*
 * Is the soldier class
 * Is armored and sword character
 * Most else similar to archer
 */
public class Soldier extends Character {
	public Soldier(int x, int y, int t){
		//These values may be shifted up or down for reasonablness/balance
		super(x,y,"Archer.png",10,6,0,2,3,4,3,7,4,7,t,3,new Sword(4,8,7),null);
	}
	public String toString(){
		return "S";
	}
	public void attack(Character target, Weapon weapon){
		weapon.attack(this, target);
	}
}
