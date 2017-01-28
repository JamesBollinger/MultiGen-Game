/* ***
 * EnemyMove - an object to store concise data
 * regarding the move that an enemy is going to take.

 * For now, all this data includes is:
 * - a destination (an ordered pair representing where the enemy plans to move)
 * - a target (an ordered pair representing the target of the enemy unit,
     or null if not applicable)
 * - action, an integer obeying the following code:
     0 => idle (meaning no target should be supplied)
     1 => attack (meaning the target is indeed an attack target)
     2 => give-item?
     4 => use-item?
     5 => ... haven't decided yet.
     6 => ... what other actions can an enemy take?
     7 => ... idk, taunt the player?
     8 => ... we'll have to think/talk about it.
 * I expect that the actual response to an action
 * (e.g., the actual call to .attack(...))
 * will take place somewhere else.
 * This class
 * is just a structure holding a bunch of data.
 * **/

import java.awt.geom.Point2D;
import java.awt.Point;

public class EnemyMove {

	private Point destination, target;
	private int action;

	public EnemyMove(Point dest, Point tar, int intention) {
		destination = dest;
		target = tar;
		action = intention;
	}

	public Point getDestination() {
		return destination;
	}

	public  Point getTarget() {
		return target;
	}

	public int getAction() {
		return action;
	}

	@Override
	public String toString() {
		String res = ("EnemyMove: to (" +
			    (int)(destination.getX()) + ", " +
			    (int)(destination.getY()) +
			    ") with Action " + action);
		return res;
	}
}
