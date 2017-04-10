public class Archer extends Character {
	//used to determine maximum range
	int range;
	//is the type of bow, uses different arrow values for quantity and Armour piercing
	Bow bow;
	public Archer(int x, int y, int t){
		super(x,y,"\"artwork\"/Archer.png",10,5,6,0,0,10,(int)(Math.random()*10), t);
		range = 10;
		bow = new Bow(10,5,/*3,*/new Arrow(2,3), 3);
		weapons[0] = bow;
	}
	public Archer(int x, int y, int t, int ran){
		super(x,y,"\"artwork\"/Archer.png",10,5,6,0,0,10,(int)(Math.random()*10), t);
		range = 10;
		bow = new Bow(10,5,/*3,*/new Arrow(2,3), ran);
		weapons[0] = bow;
	}
	//Used primarly for inconsole testing
	public String toString(){
		return "A";
	}
	//Is the attack function for the archer
	public void attack(Character target){
		if(!checkAdjacent(target.x,target.y) && target.team != this.team){
			bow.attack(this, target);
		}
	}
}

