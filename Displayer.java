/*
 * this displays the characters
 * i started development on this a bit ago to
 * test some graphics, but decided to do it once
 * the logic engine in battle was working as I wanted it to be
 * NOT HIGH PRIOTY(even remotely)
 */
import java.awt.*;
   import java.awt.event.*;
   import java.awt.image.BufferStrategy;
   import javax.swing.*;
   import javax.imageio.*;
   import java.awt.image.BufferedImage;
   import java.io.*;

   public class Displayer extends Canvas{
	
		JFrame frame;
		private BufferStrategy strategy;
		
		Battle fight;
		public Archer testObject = new Archer(0,0,0);
 		public Displayer(){
 			
			frame = new JFrame("Displayer");
			frame.add(this);
			frame.setSize(1000,1000);
			frame.getContentPane().setBackground(Color.WHITE);
			frame.setVisible(true);
			frame.setResizable(false);
			frame.addWindowListener(
               new WindowAdapter() { 
                  public void windowClosing(WindowEvent e) {System.exit(0);} 
               });
         //fight = new Battle();
			render();
      }
      public static void main(String[] args){
         Displayer test = new Displayer();
      }
		public void paint(Graphics g){
				ImageIcon img = testObject.sprite;
				g.drawImage(img.getImage(),100,100,null);			
		}
		 public void render(){
        //renders # of frames in the background then shows them in order
        //the parameter is the number of frames, that are cycled through
         createBufferStrategy(2);
         strategy = getBufferStrategy();
         Graphics g = null;
         do {
            try{
               g =  strategy.getDrawGraphics();
            
            } 
            finally {
            //SEH - This line being here is the cause of the disappearing graphics on my laptops.  I moved it down but don't know
            //  if this will destroy the fact that this made it work on the school laptops.
            //  Perhaps the working or not working depends on whether the processor is fast enough to repaint before the dispose
            //  (or garbage collector?) actually clears the Graphics object g?
                //g.dispose();
               paint(g);
            }
            strategy.show();
            g.dispose();
         } while (strategy.contentsLost());
         Toolkit.getDefaultToolkit().sync();
      }
   }
   
   