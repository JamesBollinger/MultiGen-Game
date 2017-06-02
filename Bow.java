/*
 * one of the weapons available
 * is the actuall class in which the attacking occurs
 * is sub-class of the weapon class
 */
public class Bow extends Weapon {
	private int accuracy;
	Quiver arrows;
	//This tests whether the shot is successful or not
	//if the accuracy + any additons is less than that range, it will fail
	//it has a higher percentage chance of success the closer you are/more additions you have
	//after assessing if the arrow flies where it is suppossed to, it checks if the target dodges
	public Bow(int a, int d, Quiver givenSet, int givenRange) {
		/* 
		 * This "a" parameter used to be "mA"
		 * ('modified attack' or perhaps 'modified accuracy'?)
		 * (was this a mistsake, or do we need
		 *  to modify the value of "a", before using it?)
		 *  -VC
		 * */
		super(d, a, givenSet.getAP(), givenRange);
		/*
		arrows = new Quiver(arrow.getAP(), arrow.quiver);
		*/
		arrows = givenSet;
		accuracy = a;
	}
}
