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
	//This are the primary instances that everything uses
	ImageIcon sprite;int x;int y;
	public Entity(int r, int j, String img) {x = r;y=j;try {
		sprite  = new ImageIcon(ImageIO.read(Entity.class.getResourceAsStream(img) ) );
	} catch (IOException e) {}
	}
	public void setSprite(String img){sprite = new ImageIcon(img);}
	public String toString(){
		return "n";
	}
	public boolean checkAdjacent(int x, int y){
		return(x + 1 == this.x && y + 0 == this.y ||
			   x - 1 == this.x && y + 0 == this.y ||
			   x + 1 == this.x && y + 1 == this.y ||
			   x + 1 == this.x && y - 1 == this.y ||
			   x - 1 == this.x && y + 1 == this.y ||
			   x - 1 == this.x && y - 1 == this.y ||
			   x + 0 == this.x && y + 1 == this.y ||
			   x + 0 == this.x && y - 1 == this.y
			   );
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
}
