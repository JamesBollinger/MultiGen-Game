/*
 * Is the soldier class
 * Is armored and sword character
 * Most else similar to archer
 */
public class Soldier extends Unit {
	Sword sword;
	public Soldier(int x, int y, int t){
/*
		int imgIndex = (int) (Math.random()*5);
		String imgSrc = new String("\"artwork\"/nic-cage-" + imgIndex + ".jpg");
*/
		super(x,y,"\"artwork\"/nic-cage-"+((int)(Math.random()*5))+".jpg",10,5,
			/*magical strength=*/ 1,
			/*hit speed=*/ 10,
			/*speed*/ 6,
			/*luck=*/ 4,
			/*agility*/ 4,
			/*armour*/ 3,
			/*accuracy*/ 1,
			/*constitution=*/ 8, (int)(Math.random()*10), t,
			/*magical resistance=*/ 3);
		sword = new Sword(4,8,7);	
		name = "Soldier"+name.substring(4);
	}

	public String toString(){
		return "Soldier";
	}

	public void attack(Unit target, Weapon chosenWeapon) {
/*		System.out.println(checkAdjacent(target.getX(),target.getY()));*/
		if(checkAdjacent(target.getX(),target.getY()) && target.team != this.team){
			if(Weapon.dodgeTest(this, target)){
				int armourDifference = chosenWeapon.getAP()-target.getArmour();
				if(armourDifference > 0) armourDifference = 0;
				target.dealDamage(
					((int)(Math.random()*(chosenWeapon.getMaxDamage()-
						chosenWeapon.getMinDamage()))
						+ chosenWeapon.getMinDamage()
						- armourDifference),
					this,
					getStrength(),
					chosenWeapon.getMaxDamage(),
					0);
/*				System.out.println("hit");*/
			}
		}
	}
}
