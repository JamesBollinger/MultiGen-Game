# MultiGen-Game
This is designed for the creation of the game  

So I think that we could use this readMe to easily to set tasks and other stuff that we want to get done  

I just think it would be an easy way of looking at stuff  
We should probably still use the google docs for brainstorming new general ideas, leave this for specific code stuff  

### Programming notes / suggestions
NOTE (Jan 15th, VC): "Character" is the name of an existing wrapper class in Java.  
This may be *practically* irrelevant, since we're not going to be using the wrapper class.  
But it got me thinking as to whether we should rename the Character.java class.  
Seriously -- should we rename Character.java -> Unit.java ? I could do this, and replace all instances of the word, if you want. -VC  

Additional notes:

The Weapon.java class contains the abstract method attack(), but it also contains a method called dealDamage(). Is this a redundant method? This is something to consider.

Also, though this is marginally less important, we should reconsider the placement of println() statements. At the very least, if we include them for debugging / logging purposes, we should (i) have them all in one saved log file, or (ii) make them more descriptive to describe what unit is attacking / has just hit / missed, etc.

### Programs Developed so Far (these programs work!)
*   ActionListenerDemo/GraphicsTest.java -- Produces a grid of random-colored tiles, and clicking transfers a tile's color to a different tile.
*   ActionListenerDemo/GraphicsLines.java -- Produces a grid of random-colored tiles, and clicking refreshes the coloring.
*   TurnBasedSystemSideMenu.java -- the main "mid-level" layer that initiates the tactical map. Now functions with the inner-layer Combat system. (Keeps track of the player phase & enemy phase, units on the map, etc.)
*   TurnBasedSystemFloatingMenu.java (reborn from "TmpGraphics") -- an older tactical map / graphical manager, in which the halt menu is designed to be 'floating' on top of the tactical map itself. Has temporarily been abandoned / superseded by the 'SideMenu' approach.

### Map Codes

0 = Ocean  
1 = Plains  
2 = Forest  
3 = Hills  
4 = Ice  
5 = Sand  
6 = Moutains  

## Future plans for the mid-level/tactical end (these are still in development)
*   ./TurnBasedSystemWithHighlightedMenu.java (I will shorten the name later) -- same as TurnBasedSystem, but also includes a menu for the user to click after using a unit
*   Ensuring that units do not spawn on Ocean tiles (ok, a main mechanism has been added to do this; but I might improve it later)
*   Develop an AI for the tactical map (again, a basic one exists, but improvements are in progress)
*   Display a menu for the user to choose certain actions to perform on a particular unit (add a new image overlay. But make sure it's placed in a reasonable location, on screen [not partly off-screen] regardless of the unit location.)
*   Allowing the client/user to click a terrain square to get more info about that square/tile (TBD)


