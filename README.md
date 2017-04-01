# MultiGen-Game
This is designed for the creation of the game  

So I think that we could use this readMe to easily to set tasks and other stuff that we want to get done  

I just think it would be an easy way of looking at stuff  
We should probably still use the google docs for brainstorming new general ideas, leave this for specific code stuff  

NOTE (Jan 15th, VC): You know what I just realized? "Character" is the name of an existing wrapper class in Java.  
Actually, this may be irrelevant, since we're not going to be using the wrapper class.  
But it got me thinking as to whether we should rename the Character.java class.  
Seriously -- should we rename Character.java -> Unit.java ? I could do this, and replace all instances of the word, if you want. -VC  


###Programs Developed so Far (these programs work!)
*   ActionListenerDemo/GraphicsTest.java -- Produces a grid of random-colored tiles, and clicking transfers a tile's color to a different tile.
*   ActionListenerDemo/GraphicsLines.java -- Produces a grid of random-colored tiles, and clicking refreshes the coloring.
*   ./TurnBasedSystem.java (reborn from "TmpGraphics") -- manages moving a unit around the map. (Keeps track of player phase & enemy phase).

###Map Codes

0 = Ocean  
1 = Plains  
2 = Forest  
3 = Hills  
4 = Ice  
5 = Sand  
6 = Moutains  

##Future plans for the mid-level/tactical end (these are still in development)
*   ./TurnBasedSystemWithHighlightedMenu.java (I will shorten the name later) -- same as TurnBasedSystem, but also includes a menu for the user to click after using a unit
*   Ensuring that units do not spawn on Ocean tiles (ok, a main mechanism has been added to do this; but I might improve it later)
*   Develop an AI for the tactical map (again, a basic one exists, but improvements are in progress)
*   Display a menu for the user to choose certain actions to perform on a particular unit (add a new image overlay. But make sure it's placed in a reasonable location, on screen [not partly off-screen] regardless of the unit location.)
*     Allowing the client/user to click a terrain square to get more info about that square/tile (TBD)


