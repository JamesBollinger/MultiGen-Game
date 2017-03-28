
import java.util.*;
public class GraphicsThreadWithMenu implements Runnable {

/*
	int[][] terrain;
	Character[][] players;
*/
	private ArrayList<Character> savedFriendlyUnits;
	private ArrayList<Character> savedEnemyUnits;

	public GraphicsThreadWithMenu(ArrayList<Character> playerUnits, ArrayList<Character> enemyUnits) {
		savedFriendlyUnits = playerUnits;
		savedEnemyUnits = enemyUnits;
	}

	public void run() {
		TurnBasedSystemWithHighlightedMenu manager = new TurnBasedSystemWithHighlightedMenu(savedFriendlyUnits, savedEnemyUnits);
		manager.setVisible(true);
	}

}
