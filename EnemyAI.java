/* Vincent Cozzo
 * 
 * A basic AI for the enemy team of a turn-based tactical game.
 * 
 * For now, it makes a decision for an individual enemy unit.
 * 
 * (A work in progress)
 * */
import java.awt.geom.Point2D;
import java.awt.Point;

public class EnemyAI {

	private Unit[][] unitMap;
	private int[][] landMap;
	private Unit currentEnemy;
	private int tilesX, tilesY;
	private int enemySide;
	private int opponentSideNW, opponentSideNE,
		    opponentSideSE, opponentSideSW;

	public EnemyAI(Unit[][] map, int[][] land, Unit unit) {
		unitMap = map;
		landMap = land;
		currentEnemy = unit;
		tilesX = map[0].length;
		tilesY = map.length;
		if (unit == null) {
			throw new UnsupportedOperationException("ERR - no unit found at that tile!");
		}
		else if (unit.getTeam() != 1) {
			throw new UnsupportedOperationException("ERR - unit at that tile is not an enemy unit!");
		}
	}

	/* An alternate Constructor, in case we need it.
	 * I doubt we will, but I include it just in case 
	 * for some reason we DON'T have immediate access to
	 * the enemy Unit object reference
	 * when we want to use this class. 
	 * 
	 * Or, better yet, in the future we might consolidate the EnemyAI framework,
	 * e.g. make just one massive EnemyAI rather than separate
	 * EnemyAI objects for each enemy unit. (That way, enemy's strategies 
	 * may be better coordinated with one another in a grand strategy.) 
	 * */
	public EnemyAI(Unit[][] map, int[][] land, int col, int row) {
		unitMap = map;
		landMap = land;
		currentEnemy = map[row][col];
		tilesX = map[0].length;
		tilesY = map.length;
		if (currentEnemy == null) {
			throw new UnsupportedOperationException("ERR - no unit found at that tile!");
		}
		else if (currentEnemy.getTeam() != 1) {
			throw new UnsupportedOperationException("ERR - unit at that tile is not an enemy unit!");
		}
	}

	public EnemyMove decide() {
		int playerUnits;
		scanAllZones();
		playerUnits = (opponentSideNW + opponentSideNE + opponentSideSE + opponentSideSW);
/*		System.out.println("\tThere are " + playerUnits
			+ " player units within range...");
		System.out.println("\t" + opponentSideNW +
			" players to the NorthWest,\n\t" + opponentSideNE
			+ " players to the NorthEast,");
		System.out.println("\t" + opponentSideSE +
			" players to the SouthEast,\n\t" + opponentSideSW
			+ " players to the SouthWest,");
*/

		if ((enemySide+1) < playerUnits) {
			System.out.println("\t\"BRAVE SIR ROBIN RAN AWAY\"");
			if ((opponentSideNW > opponentSideNE) &&
			    (opponentSideNW > opponentSideSE) &&
			    (opponentSideNW > opponentSideSW)) {
				System.out.println("Need to retreat SE");
				Point destination = eagerTravelSE();
				// Make the EnemyMove objec and then return it!
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNE > opponentSideNW) &&
			           (opponentSideNE > opponentSideSE) &&
			           (opponentSideNE > opponentSideSW)) {
				System.out.println("Need to retreat SW");
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideSE > opponentSideNE) &&
			           (opponentSideSE > opponentSideNW) &&
			           (opponentSideSE > opponentSideSW)) {
				System.out.println("Need to retreat NW");
				Point destination = eagerTravelNW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideSW > opponentSideNE) &&
			           (opponentSideSW > opponentSideSE) &&
			           (opponentSideSW > opponentSideNW)) {
				System.out.println("Need to retreat NW");
				Point destination = eagerTravelNW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			}/* ***
			  * TO DO: add more cases, so that
			  * even if there is an equal number of player units
			  * located in different zones,
			  * the AI makes a decision that makes sense.
			  * 
			  * To do this, I might want to write a moveWest() method, etc.
			  * to be able to travel in the four primary directions.
			  * **/
			  /* NOTE: if none of the above four cases applies,
			   * then there is a tie.
			   * */
			  else if ((opponentSideNE == opponentSideNW) &&
			           (opponentSideNE > opponentSideSE) &&
			           (opponentSideNE > opponentSideSW)) {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNE > opponentSideNW) &&
			           (opponentSideNE == opponentSideSE) &&
			           (opponentSideNE > opponentSideSW)) {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNE > opponentSideNW) &&
			           (opponentSideNE > opponentSideSE) &&
			           (opponentSideNE == opponentSideSW)) {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNW > opponentSideNE) &&
			           (opponentSideNW > opponentSideSE) &&
			           (opponentSideNW == opponentSideSW)) {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNW > opponentSideNE) &&
			           (opponentSideNW == opponentSideSE) &&
			           (opponentSideNW > opponentSideSW)) {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideSW > opponentSideNE) &&
			           (opponentSideSW == opponentSideSE) &&
			           (opponentSideSW > opponentSideNW)) {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			/* Only one broad case remains:
			 * there is a three-way tie (or four-way tie, but that can fit into an else case)
			 * */
			} else if ((opponentSideNE < opponentSideNW) &&
			           (opponentSideNE < opponentSideSE) &&
			           (opponentSideNE < opponentSideSW)) {
				System.out.println("Need to retreat SW");
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNE < opponentSideNW) &&
			           (opponentSideNE < opponentSideSE) &&
			           (opponentSideNE < opponentSideSW)) {
				System.out.println("Need to retreat SW");
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else if ((opponentSideNE < opponentSideNW) &&
			           (opponentSideNE < opponentSideSE) &&
			           (opponentSideNE < opponentSideSW)) {
				System.out.println("Need to retreat SW");
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			} else {
				Point destination = eagerTravelSW();
				EnemyMove result = new EnemyMove(destination, null, 0);
				return result;
			}
		} else {
			/* No need to retreat.... yet. */
			System.out.println("\t\"My life for Aiur!\"");
			Point destination = eagerTravelNW();
			EnemyMove attack = new EnemyMove(destination, null, 0);
			return attack;
		}
	}

	private void scanAllZones() {
		scanNE();
		scanSE();
		scanSW();
		scanNW();
	}

	private void scanNE() {
		int speed = currentEnemy.getSpeed();
		int y = currentEnemy.getY()-1;
		int j = 1;
		while ((j <= speed) && (y >= 0)) {
			int x = currentEnemy.getX();
			int i = 0;
			while (((i+j) <= speed) && (x < tilesX)) {
				if (unitMap[y][x] != null) {
					Unit nextUnit = unitMap[y][x];
					if (nextUnit.getTeam() == 1) {
						enemySide ++;
					} else {
						opponentSideNE ++;
					}
				}
				x++;
				i++;
			}
			y--;
			j++;
		}
	}

	private void scanSE() {
		int speed = currentEnemy.getSpeed();
		int x = currentEnemy.getX()+1;
		int i = 1;
		while ((i <= speed) && (x < tilesX)) {
			int y = currentEnemy.getY(); int j = 0;
			while (((i+j) <= speed) && (y < tilesY)) {
				if (unitMap[y][x] != null) {
					Unit nextUnit = unitMap[y][x];
					if (nextUnit.getTeam() == 1) {
						enemySide ++;
					} else {
						opponentSideSE ++;
					}
				}
				y++;
				j++;
			}
			x++;
			i++;
		}
	}

	private void scanSW() {
		int speed = currentEnemy.getSpeed();
		int y = currentEnemy.getY()+1;
		int j = 1;
		while ((j <= speed) && (y < tilesY)) {
			int x = currentEnemy.getX();
			int i = 0;
			while (((i+j) <= speed) && (x >= 0)) {
				if (unitMap[y][x] != null) {
					Unit nextUnit = unitMap[y][x];
					if (nextUnit.getTeam() == 1) {
						enemySide ++;
					} else {
						opponentSideSW ++;
					}
				}
				x--;
				i++;
			}
			y++;
			j++;
		}
	}

	private void scanNW() {
		int speed = currentEnemy.getSpeed();
		int x = currentEnemy.getX()-1;
		int i = 1;
		while ((i <= speed) && (x >= 0)) {
			int y = currentEnemy.getY();
			int j = 0;
			while (((i+j) <= speed) && (y >= 0)) {
				if (unitMap[y][x] != null) {
					Unit nextUnit = unitMap[y][x];
					if (nextUnit.getTeam() == 1) {
						enemySide ++;
					} else {
						opponentSideNW ++;
					}
				}
				y--;
				j++;
			}
			x--;
			i++;
		}
	}

	/* ***
	 * Below are the 'movement' methods
	 * */
	private Point eagerTravelNEAux(int remainingSpeed, int x, int y) {
		if ((x < tilesX) && (x >= 0) && (y >= 0) && (y < tilesY)) {
			if (remainingSpeed == 0) {
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				return (new Point (x, y));
			} else {
				/* The algorithm should be along the lines of:
				 * 1) Try this point
				 * 2) If it fails for some reason (ocean or occupied) then:
				 * 3) -move up or right depending on remainingSpeed.
				 * */
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				Point result = new Point(x, y);
				if ((remainingSpeed % 2) == 0) {
					Point fartherResult = eagerTravelNEAux(remainingSpeed-1, x, y-1);
					if (fartherResult == null) {
						fartherResult = eagerTravelNEAux(remainingSpeed-1, x+1, y);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				} else {
					Point fartherResult = eagerTravelNEAux(remainingSpeed-1, x+1, y);
					if (fartherResult == null) {
						fartherResult = eagerTravelNEAux(remainingSpeed-1, x, y-1);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				}
			}

		} else {
			return null;
		}
	}

	private Point eagerTravelNE() {
		Point recResult = eagerTravelNEAux(currentEnemy.getSpeed(), currentEnemy.getX(), currentEnemy.getY());
		if (recResult == null) {
			return (new Point(currentEnemy.getX(), currentEnemy.getY()));
		} else {
			return recResult;
		}
	}

	private Point eagerTravelSEAux(int remainingSpeed, int x, int y) {
		if ((x < tilesX) && (x >= 0) && (y >= 0) && (y < tilesY)) {
			if (remainingSpeed == 0) {
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				return (new Point (x, y));
			} else {
				/* The algorithm should be along the lines of:
				 * 1) Try this point
				 * 2) If it fails for some reason (ocean or occupied) then:
				 * 3) -move up or right depending on remainingSpeed.
				 * */
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				Point result = new Point(x, y);;
				if ((remainingSpeed % 2) == 0) {
					Point fartherResult = eagerTravelSEAux(remainingSpeed-1, x+1, y);
					if (fartherResult == null) {
						fartherResult = eagerTravelSEAux(remainingSpeed-1, x, y+1);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				} else {
					Point fartherResult = eagerTravelSEAux(remainingSpeed-1, x, y+1);
					if (fartherResult == null) {
						fartherResult = eagerTravelSEAux(remainingSpeed-1, x+1, y);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				}
			}
		} else {
			return null;
		}
	}

	private Point eagerTravelSE() {
		Point recResult = eagerTravelSEAux(currentEnemy.getSpeed(), currentEnemy.getX(), currentEnemy.getY());
		if (recResult == null) {
			return (new Point(currentEnemy.getX(), currentEnemy.getY()));
		} else {
			return recResult;
		}
	}

	private Point eagerTravelSWAux(int remainingSpeed, int x, int y) {
		if ((x < tilesX) && (x >= 0) && (y >= 0) && (y < tilesY)) {
			if (remainingSpeed == 0) {
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				return (new Point (x, y));
			} else {
				/* The algorithm should be along the lines of:
				 * 1) Try this point
				 * 2) If it fails for some reason (ocean or occupied) then:
				 * 3) -move up or right depending on remainingSpeed.
				 * */
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				Point result = new Point(x, y);
				if ((remainingSpeed % 2) == 0) {
					Point fartherResult = eagerTravelSWAux(remainingSpeed-1, x, y+1);
					if (fartherResult == null) {
						fartherResult = eagerTravelSWAux(remainingSpeed-1, x-1, y);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				} else {
					Point fartherResult = eagerTravelSWAux(remainingSpeed-1, x-1, y);
					if (fartherResult == null) {
						fartherResult = eagerTravelSWAux(remainingSpeed-1, x, y+1);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				}
			}
		} else {
			return null;
		}
	}

	private Point eagerTravelSW() {
		Point recResult = eagerTravelSWAux(currentEnemy.getSpeed(), currentEnemy.getX(), currentEnemy.getY());
		if (recResult == null) {
			return (new Point(currentEnemy.getX(), currentEnemy.getY()));
		} else {
			return recResult;
		}
	}

	private Point eagerTravelNWAux(int remainingSpeed, int x, int y) {
/*		System.out.println("\t\tnow searching NorthWest at the point ("+x+","+y+") where decremented val="+remainingSpeed);*/
		if ((x < tilesX) && (x >= 0) && (y >= 0) && (y < tilesY)) {
			if (remainingSpeed == 0) {
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				return (new Point (x, y));
			} else {
				/* The algorithm should be along the lines of:
				 * 1) Try this point
				 * 2) If it fails for some reason (ocean or occupied) then:
				 * 3) -move up or right depending on remainingSpeed.
				 * */
				if ((landMap[y][x] == 0) || ((unitMap[y][x] != null) && (unitMap[y][x] != currentEnemy))) {
					return null;
				}
				Point result = new Point(x, y);
				if ((remainingSpeed % 2) == 0) {
					Point fartherResult = eagerTravelNWAux(remainingSpeed-1, x, y-1);
					if (fartherResult == null) {
						fartherResult = eagerTravelNWAux(remainingSpeed-1, x-1, y);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				} else {
					Point fartherResult = eagerTravelNWAux(remainingSpeed-1, x-1, y);
					if (fartherResult == null) {
						fartherResult = eagerTravelNWAux(remainingSpeed-1, x, y-1);
						if (fartherResult == null) {
							return result;
						} else {
							return fartherResult;
						}
					} else {
						return fartherResult;
					}
				}
			}
		} else {
			return null;
		}
	}

	private Point eagerTravelNW() {
		Point recResult = eagerTravelNWAux(currentEnemy.getSpeed(), currentEnemy.getX(), currentEnemy.getY());
		if (recResult == null) {
			return (new Point(currentEnemy.getX(), currentEnemy.getY()));
		} else {
			return recResult;
		}
	}

/* Rewritten the below methods in order to be more accurate,
 * and obey the enemy unit's speed.
 * The new versions are modeled after the showMoveRange()
 * method in the TurnBasedSystem class. */
/*	private Point eagerTravelNE() {
		int speed = currentEnemy.getSpeed();
		int y = currentEnemy.getY()-1;
		int j = 1;
		while ((j <= speed) && (y >= 0)) {
			int x = currentEnemy.getX();
			int i = 0;
			while (((i+j) <= speed) && (x < tilesX)) {
				if (unitMap[y][x] == null) {
					if (landMap[y][x] != 0) {
						return (new Point(x, y));
					}
				}
				x++;
				i++;
			}
			y--;
			j++;
		}
		return (new Point(currentEnemy.getX(), currentEnemy.getY()));
	}

	private Point eagerTravelSE() {
		int speed = currentEnemy.getSpeed();
		int x = currentEnemy.getX()+1;
		int i = 1;
		while ((i <= speed) && (x < tilesX)) {
			int y = currentEnemy.getY();
			int j = 0;
			while (((i+j) <= speed) && (y < tilesY)) {
				if (unitMap[y][x] == null) {
					if (landMap[y][x] != 0) {
						return (new Point(x, y));
					}
				}
				y++;
				j++;
			}
			x++;
			i++;
		}
		return (new Point(currentEnemy.getX(), currentEnemy.getY()));
	}

	private Point eagerTravelSW() {
		int speed = currentEnemy.getSpeed();
		int y = currentEnemy.getY()+1;
		int j = 1;
		while ((j <= speed) && (y < tilesY)) {
			int x = currentEnemy.getX();
			int i = 0;
			while (((i+j) <= speed) && (x >= 0)) {
				if (unitMap[y][x] == null) {
					if (landMap[y][x] != 0) {
						return (new Point(x, y));
					}
				}
				x--;
				i++;
			}
			y++;
			j++;
		}
		return (new Point(currentEnemy.getX(), currentEnemy.getY()));
	}

	private Point eagerTravelNW() {
		int speed = currentEnemy.getSpeed();
		int x = currentEnemy.getX()-1;
		int i = 1;
		while ((i <= speed) && (x >= 0)) {
			int y = currentEnemy.getY();
			int j = 0;
			while (((i+j) <= speed) && (y >= 0)) {
				if (unitMap[y][x] == null) {
					if (landMap[y][x] != 0) {
						return (new Point(x, y));
					}
				}
				y--;
				j++;
			}
			x--;
			i++;
		}
		return (new Point(currentEnemy.getX(), currentEnemy.getY()));
	}
*/
}
