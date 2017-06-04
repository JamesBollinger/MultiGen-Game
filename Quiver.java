public class Quiver{
	private int armourPiercing, available, max;
	
	public Quiver(int a, int q){
		armourPiercing = a;
		max = q;
		available = q;
	}
	
	public int replenish (int additional){
		int newArrowCount = available+additional;
		if ((newArrowCount) > max) {
			available = max;
			return (newArrowCount-max);
		} else {
			available = newArrowCount;
			return 0;
		}
	}
	
	public boolean shoot(){
		if (available > 0) {
			available --;
			return true;
		} else {
			return false;
		}
	}

	public int getNumAvailable(){
		return available;
	}
	
	public int getAP(){
		return armourPiercing;
	}

	public int getMax(){
		return max;
	}
}
