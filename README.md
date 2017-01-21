# MultiGen-Game
This is designed for the creation of the game  

So I think that we could use this readMe to easily to set tasks and other stuff that we want to get done  

I just think it would be an easy way of looking at stuff  
We should probably still use the google docs for brainstorming new general ideas, leave this for specific code stuff  

NOTE (Jan 15th, VC): You know what I just realized? "Character" is the name of an existing wrapper class in Java.  
Actually, this may be irrelevant, since we're not going to be using the wrapper class.  
But it got me thinking as to whether we should rename the Character.java class.  


###Programs Developed so Far
*   ActionListenerDemo/GraphicsTest.java -- Produces a grid of random-colored tiles, and clicking transfers a tile's color to a different tile.
*   ActionListenerDemo/GraphicsLines.java -- Produces a grid of random-colored tiles, and clicking refreshes the coloring.
*   TurnBasedSystem.java (reborn from "TmpGraphics") -- manages moving a unit around the map. (Keeps tack of player phase & enemy phase).

###Map Codes
0 = Ocean  
1 = Plains  
2 = Forest  
3 = Hills  
4 = Ice  
5 = Sand  
6 = Moutains  

##Future plans for the mid-level/tactical end
*   Develop an AI for the tactical map
*   Display a menu for the user to choose certain actions to perform on a particular unit
*   Ensuring that units do not spawn on Ocean tiles
*   Allowing the client/user to click a terrain square to get more info about that square/tile (TBD)
