/**
 * Main class to instantiate and run the game from
 * 
 * @author Erkin Verbeek
 * @version 1.0.0
 */


public class OutputManager {

	private static SnakeFrame gameFrame;
	
	public static void main(String[] args) {
		
		gameFrame = new SnakeFrame();
		
		
		
		
		
		
	}
	
	/** Closes the entire game **/
	public static void quitGame() { gameFrame.dispose(); }
	
	/** starts the game in singleplayer **/
	public static void startGame() { gameFrame.startGame(); }

	// changes the gameFrame size to the specified values
	public static void changeScreenSize(int width, int height) { gameFrame.setSize(width, height); }
	
	
}
