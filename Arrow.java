public class Arrow{
	int armourPiercing, quiver, total;
	public Arrow(int a, int q){
		armourPiercing = a; quiver = q; total = q;
	}
	public void shoot(){
		total --;
	}
}