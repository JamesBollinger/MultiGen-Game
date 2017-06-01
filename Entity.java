/*
 * this is the over arching class that every game object that can be interacted with belongs to
 * everything has a sprite defined here, also is where adjacency is checked
 * Primarily was used in the displayer class
 * was designed so inanimate objects,(rocks,trees could be used)
 */
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
public abstract class Entity {
	public static final int MAX_NUM_WEAPONS = 4;
	//This are the primary instances that everything uses
	ImageIcon sprite;
	private int x;
	private int y;
	public Entity(int r, int j, String img) {
		x = r;
		y = j;
		try {
			sprite  = new ImageIcon(ImageIO.read(Entity.class.getResourceAsStream(img)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setSprite(String img){sprite = new ImageIcon(img);}
	public String toString(){
		return "n";
	}
	public boolean checkAdjacent(int x, int y){
		/* This is a more efficient implementation
		 * of checking if two entities are adjacent:
		 * calculate rectangular distance, and if it equals one,
		 * then they must be adjacent. */
		int distance = ((getX() - x) + (getY() - y));
		return ((distance == 1) || (distance == (-1)));
		/*
		return(x + 1 == this.getX() && y + 0 == this.getY() ||
			   x - 1 == this.getX() && y + 0 == this.getY() ||
			   x + 1 == this.getX() && y + 1 == this.getY() ||
			   x + 1 == this.getX() && y - 1 == this.getY() ||
			   x - 1 == this.getX() && y + 1 == this.getY() ||
			   x - 1 == this.getX() && y - 1 == this.getY() ||
			   x + 0 == this.getX() && y + 1 == this.getY() ||
			   x + 0 == this.getX() && y - 1 == this.getY()
			   );
		*/
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public ImageIcon getIcon() {
		return sprite;
	}

	public void setX(int posX) {
		x = posX;
	}

	public void setY(int posY) {
		y = posY;
	}
}
