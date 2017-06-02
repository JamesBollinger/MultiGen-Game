public class Archer extends Unit {
	//used to determine maximum range
	int range;
	//used in order to store the actual arrows
	Quiver arrows;
	//is the type of bow, uses different arrow values for quantity and Armour piercing
	Bow bow;
	public Archer(int x, int y, int t){
		super(x, y, Logger.root_dir+"Archer.png", 10, 5,
			/*magical strength=*/ 3,
			/*hit speed=*/ 9,
			/*speed*/ 6,
			/*luck=*/ 5,
			/*agility*/ 10,
			/*armour*/ 0,
			/*accuracy*/ 10,
			/*constitution=*/ 4, (int)(Math.random()*10), t,
			/*magical resistance=*/ 5);
		range = 10;
		bow = new Bow(10,5,/*3,*/new Quiver(2,3), 3);
		weapons[0] = bow;
		name = ("Archer"+name.substring(4));
	}
	public Archer(int x, int y, int t, int ran){
		super(x, y, Logger.root_dir+"Archer.png", 10, 5,
			/*magical strength=*/ 3,
			/*hit speed=*/ 9,
			/*speed*/ 6,
			/*luck=*/ 5,
			/*agility*/ 10,
			/*armour*/ 0,
			/*accuracy*/ 10,
			/*constitution=*/ 4, (int)(Math.random()*10), t,
			/*magical resistance=*/ 5);
/*		super(x,y,"\"artwork\"/Archer.png",10,5,6,0,0,10,(int)(Math.random()*10), t);*/
		range = 10;
		bow = new Bow(10,5,/*3,*/new Quiver(2,3), ran);
		weapons[0] = bow;
		name = ("Archer"+name.substring(4));
	}
	//Used primarly for inconsole testing
	public String toString(){
		return "A";
	}

	//Is the attack function for the archer
	/* Update: since attack() is not going to be a method call
	 * of the Weapon class, this needs to be rewritten
	 * */
	public void attack(Unit target, Weapon chosenWeapon){
		/* 
		 * There are two important checks here:
		 * whether the quiver of arrows has enough (is now empty),
		 * and a check on whether the archer is not trying to 
		 *    shoot a teammate.
		 * (I suspect the team check is unnecessary but 
		 *  I will keep it in this version as a consistency check)
		 * 
		 * */
		if ((target.getTeam() != getTeam()) && (arrows.shoot())) {
			if(Weapon.accuracyTest(chosenWeapon.getRange(),
					target.getDodge(), getAccuracy(), 0)){
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
			} else {
/*				System.out.println("missed");*/
			}
		} else if ((target.getTeam() == getTeam())) {
			throw new UnsupportedOperationException("Inconsistency -- unit "+toString()+" is trying to target a teammate!");
		} else {
/*			System.out.println("Quiver of arrows is empty!");	*/
		}
	}
}

