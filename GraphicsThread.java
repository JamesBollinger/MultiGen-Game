
import java.util.*;
public class GraphicsThread implements Runnable {

/*
	int[][] terrain;
	Character[][] players;
*/
	private ArrayList<Character> savedFriendlyUnits;
	private ArrayList<Character> savedEnemyUnits;

	public GraphicsThread(ArrayList<Character> playerUnits, ArrayList<Character> enemyUnits) {
		savedFriendlyUnits = playerUnits;
		savedEnemyUnits = enemyUnits;
	}

	public void run() {
		TurnBasedSystem manager = new TurnBasedSystem(savedFriendlyUnits, savedEnemyUnits);
		manager.setVisible(true);
	}

}
