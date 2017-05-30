
import java.util.*;
public class GraphicsThreadWithMenu implements Runnable {

/*
	int[][] terrain;
	Unit[][] players;
*/
	private ArrayList<Unit> savedFriendlyUnits;
	private ArrayList<Unit> savedEnemyUnits;

	public GraphicsThreadWithMenu(ArrayList<Unit> playerUnits, ArrayList<Unit> enemyUnits) {
		savedFriendlyUnits = playerUnits;
		savedEnemyUnits = enemyUnits;
	}

	public void run() {
		TurnBasedSystemWithHighlightedMenu manager = new TurnBasedSystemWithHighlightedMenu(savedFriendlyUnits, savedEnemyUnits);
		manager.setVisible(true);
	}

}
