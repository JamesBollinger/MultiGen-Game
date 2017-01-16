/*
 * one of the weapons available
 * is the actuall class in which the attacking occurs
 * is sub-class of the weapon class
 */
public class Bow extends Weapon {
	int accuracy;
	Arrow arrows;
	//This tests whether the shot is successful or not
	//if the accuracy + any additons is less than that range, it will fail
	//it has a higher percentage chance of success the closer you are/more additions you have
	//after assessing if the arrow flies where it is suppossed to, it checks if the target dodges
	public boolean accuracyTest(int range, int dodge, int accuracy int additions){
		if ((double)range / ((double)accuracy + additions) <= Math.random()){
			boolean check = ((int)(Math.random()*11) + 1 >= dodge);
			return check;
		}
		return false;
	}
	public Bow(int a, int d, Arrow arrow) {
		super(d,mA,arrow.armourPiercing);
		arrows = new Arrow(arrow.armourPiercing, arrow.quiver);
		accuracy = a;
	}
	//This is the actual attack method
	//comes from weapon
	public void attack(Character attacker, Character target){
		arrows.shoot();
		if(accuracyTest((Archer)attacker).range, target.dodge, attacker.accuracy, 0)){
			//This method is a weapon method
			dealDamage(target);
			System.out.println("hit");
		}
		else System.out.println("missed");
		
			
	}
}

