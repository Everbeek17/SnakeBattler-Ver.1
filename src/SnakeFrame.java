/**
 * SnakeFrame class extends JFrame and is the frame that the entire game will run on
 * 
 * @author Erkin Verbeek
 * @version 1.0.0
 */


import javax.swing.JFrame;

public class SnakeFrame extends JFrame {

	public final static int windowWidth = 605, windowHeight = 624;	// the dimensions for the JFrame window
	
	
	private MenuPanel mp;
	private GamePanel gp;
	
	
	
	/** constructor */
	public SnakeFrame() {
		
		// initialization stuff for the frame
		setTitle("Snake");		// sets the title of the Window (the JFrame)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// frees up memory when the window is closed
		setSize(windowWidth, windowHeight);		// sets size of window
		setResizable(false);					// allows/disallows resizing of window
		setLocationByPlatform(true);			// lets the OS handle the positioning of the window
		
		// instantiates the menu panel
		mp = new MenuPanel();
		add(mp);	// adds the menu panel to the game's frame
		
		setVisible(true);	// makes the frame visible
	}
	
	// starts the game with the given array of players
	public void startGame() {
		remove(mp);	// removes the current menu panel
		
		// if not in singleplayer then change the window size
		if (Player.getNumPlayers() > 1) {
			Player.setMaxScale(30);
			setSize(755, 779);
		}
		
		// creates and starts a new instance of the game panel
		gp = new GamePanel();
		add(gp);
		
		// needed to switch between JPanels
		validate();
		repaint();
		gp.requestFocusInWindow();	// gives input control to gp
	}
	
}
