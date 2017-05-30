
import java.util.*;
public class GraphicsThread implements Runnable {

/*
	int[][] terrain;
	Unit[][] players;
*/
	private ArrayList<Unit> savedFriendlyUnits;
	private ArrayList<Unit> savedEnemyUnits;

	public GraphicsThread(ArrayList<Unit> playerUnits, ArrayList<Unit> enemyUnits) {
		savedFriendlyUnits = playerUnits;
		savedEnemyUnits = enemyUnits;
	}

	public void run() {
		TurnBasedSystem manager = new TurnBasedSystem(savedFriendlyUnits, savedEnemyUnits);
		manager.setVisible(true);
	}

}
