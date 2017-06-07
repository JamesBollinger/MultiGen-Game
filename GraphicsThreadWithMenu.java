import java.util.ArrayList;

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
		TurnBasedSystemSideMenu manager = new TurnBasedSystemSideMenu(savedFriendlyUnits, savedEnemyUnits);
		manager.setVisible(true);
	}

}
