public class Archer extends Character {
	//used to determine maximum range
	int range;
	//is the type of bow, uses different arrow values for quantity and Armour piercing
	public Archer(int x, int y, int t){
		super(x,y,"Archer.png",10,5,6,0,0,10,(int)(Math.random()*10), t);
		range = 10;
		main = new Bow(10,5,3,new Arrow(2,3));
		secondary = null;
		if(main.ranged || secondary.ranged){
			rangedWeapon = true;	
		}
		if(main.melee || secondary.melee){
			meleeWeapon = true;	
		}
	}
	//Used primarly for inconsole testing
	public String toString(){
		return "A";
	}
	//Is the attack function for the archer
	public void attack(Character target, Weapon weapon){
		if(!checkAdjacent(target.x,target.y) && target.team != this.team){
			weapon.attack(this, target);
		}
	}
}

