/*
 * Is the soldier class
 * Is armored and sword character
 * Most else similar to archer
 */
public class Soldier extends Character {
	public Soldier(int x, int y, int t){
		super(x,y,"Archer.png",10,5,6,4,3,1,(int)(Math.random()*10), t);
		main = new Sword(4,8,7);
		secondary = null;
		if(main.ranged || secondary.ranged){
			rangedWeapon = true;	
		}
		if(main.melee || secondary.melee){
			meleeWeapon = true;	
		}
		
	}
	public String toString(){
		return "S";
	}
	public void attack(Character target, Weapon weapon){
		sword.attack(this, target);
	}
}
