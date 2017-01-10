/*
 * Is the soldier class
 * Is armored and sword character
 * Most else similar to archer
 */
public class Soldier extends Character {
	Sword sword;
	public Soldier(int x, int y, int t){
		super(x,y,"Archer.png",10,5,6,4,3,1,(int)(Math.random()*10));
		sword = new Sword(4,8,7);	
	}
	public String toString(){
		return "S";
	}
	public void attack(Character target){
		System.out.println(checkAdjacent(target.x,target.y));
		if(checkAdjacent(target.x,target.y) && target.team != this.team){
			sword.attack(this, target);
		}
	}
}
