import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class HaltMenuOption extends JComponent {
	/*private final int WIDTH = 640, HEIGHT = 640;*/
	private Point upperLeftCorner;
	private int height;
	private int width;
	private String text;
	private TacticalMapWindow2 master;
	private int color;
	private BufferedImage visual;

	public HaltMenuOption(Point givenCorner, int ndy, int ndx,
				String option, TacticalMapWindow2 manager) {
		upperLeftCorner = givenCorner;
		height = ndy;
		width = ndx;
		text = option;
		master = manager;
		color = (-1658327104);
		visual = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int boundY = /*((int) (givenCorner.getY()) +*/ ndy/* + 19)*/;
		int boundX = /*((int) (givenCorner.getX()) +*/ ndx/*120)*/;
		for (int pixlRow = /*ndy + ((int) (givenCorner.getY()))*/0; pixlRow < boundY; pixlRow ++) {
			for (int pixlCol = /*((int) (givenCorner.getX()))*/0; pixlCol < boundX; pixlCol ++) {
/*
				System.out.println("\t"+text+": "+" painting the bufferedImage at ("+pixlCol+", "+pixlRow+")");
*/
				visual.setRGB(pixlCol, pixlRow, -1658327104);
			}
		}
	}

	public void draw(Graphics2D rend) {
		rend.drawImage(visual, null, (int)(upperLeftCorner.getX()), (int)(upperLeftCorner.getY()));
		rend.drawString(text, ((int) (upperLeftCorner.getX())+5),
				      ((int) (upperLeftCorner.getY())+15));
	}

	public String getText() {
		return text;
	}

}
