/**
 * JPanel that displays the home menu of the game
 * implements KeyListener to allow control of menu selection
 * 
 * @author Erkin Verbeek
 * @version 1.0.0
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuPanel extends JPanel implements KeyListener {

	private final int init_NumOfPlayers = 2;	// how many players to initialize for multiplayer
	
	
	// fonts that this panel uses
	private static Font header_font, menuItem_font, selector_font;
	
	private int selector;	// value to store where selector is currently at
	
	private static enum SCREEN { MAINMENU, SINGLEPLAYER, MULTIPLAYER, OPTIONS }
	private SCREEN currentScreen;
	
	private final int selectionChoices = 4;
	private final int maxPlayers = 4;	// defines the max amount of concurrent players
	
	private int moveCounter[];		// used to make demoSnake change directions ever couple moves
	
	private PlayerSelectionPanel[] playerPanel;
	
	private Timer t;
	
	/** constructor */
	public MenuPanel() {
		
		// needed to make the keyboard inputs apply to this
		setFocusable(true);
		
		// adds this class' key listener to this class' JPanel
		addKeyListener(this);
		
		setBackground(Color.black);	// sets the background color of the panel
		
		// defines the fonts to be used
		header_font = new Font("TimesRoman", Font.BOLD, 36);
		menuItem_font = new Font("TimesRoman", Font.PLAIN, 24);
		selector_font = new Font("TimesRoman", Font.BOLD, 24);
		
		
		// initialize variables
		selector = 0;
		currentScreen = SCREEN.MAINMENU;
		playerPanel = new PlayerSelectionPanel[maxPlayers];
		moveCounter = new int[maxPlayers];
		Player.allPlayers = new Player[maxPlayers];
		
	}

	/**
	 * overriding paintComponent allows us to paint things to the screen
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		
		// a Graphics2D object allows us specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		
		switch (currentScreen) {
			case MAINMENU:	// if on the main menu
				g2.setColor(Color.white);	// sets the brush to white
				
				// paints the header
				g2.setFont(header_font);	// sets the font
				g2.drawString("Welcome!", 225, 100);
				
				// paints the menu items
				g2.setFont(menuItem_font);
				g2.drawString("Singleplayer", 245, 200);
				g2.drawString("Multiplayer", 248, 260);
				g2.drawString("Options", 266, 320);
				g2.drawString("Quit", 281, 380);
				
				// paints the selectors
				g2.setFont(selector_font);
				g2.drawString(">", 210, 200 + 60 * selector);
				g2.drawString("<", 380, 200 + 60 * selector);
				break;
			case SINGLEPLAYER:	//if on the singleplayer menu
				
				playerPanel[0].paintPanel(g2, 200, 75);	// paints the selection panel for the player
				
				
				break;
			case MULTIPLAYER:
				
				// paints the selection panel for each player
				for (int i = 0; i < Player.getNumPlayers(); i++)
					playerPanel[i].paintPanel(g2, 75 + i * 250, 75);
				break;
		
		
		
		}
		
		
	}
	
	
	private void startAnimation(int numOfPlayers) {
		
		// instantiates each player
		for (int i = 0; i < numOfPlayers; i++ ) {
			Player.allPlayers[i] = new Player("Buddy", 0, 0, 500, Color.black);
			for (int j = 0; j < 3; j++)	// grows each player to length of 4
				Player.allPlayers[i].advanceAndGrow();
			playerPanel[i] = new PlayerSelectionPanel(Player.allPlayers[i]);	// creates a player selection panel for each player
		}

		Random randomizer = new Random();	// instantiates the randomizer
		
		
		// creates a function that gets called every specified interval
		int delay = 120; //milliseconds
		ActionListener everySecondPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// checks if every player has ready'd up
				boolean ready = true;
				for (int i = 0; i < numOfPlayers; i++)
					if (!playerPanel[i].isReady())
						ready = false;
				if (ready) {	// starts the game if everyone is ready
					clearMenu();	// clears the menu/stops this timer
					for (int i = 0; i < numOfPlayers; i++)	// reinitializes each player
						Player.allPlayers[i].initialize();
					OutputManager.startGame();
					System.out.println("everyone ready."); 	// for debugging
				}
				// gives each demoSnake a random next direction
				for (int i = 0; i < numOfPlayers; i++) {
					if (moveCounter[i] > randomizer.nextInt(3)) {
						switch (randomizer.nextInt(4)) {
							case 0:
								Player.allPlayers[i].setNextDir(DIR.UP);
								break;
							case 1:
								Player.allPlayers[i].setNextDir(DIR.DOWN);
								break;
							case 2:
								Player.allPlayers[i].setNextDir(DIR.LEFT);
								break;
							case 3:
								Player.allPlayers[i].setNextDir(DIR.RIGHT);
								break;
						}
						moveCounter[i] = 0;
					} else { moveCounter[i]++;	}	// makes the snake change direction only every once in a while
					Player.allPlayers[i].advance();			// moves the snake forward
				}
				removeAll();	// repaints the screen
				repaint();
			}
		};
		t = new Timer(delay, everySecondPerformer);
		t.start();	// starts the timer
	}
	
	/** clears this menu **/
	public void clearMenu() {
		t.stop();	// stops the timer
		removeKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyID = e.getKeyCode();	// saves the ID of the key pressed
		switch (currentScreen) {
			case MAINMENU:
				if (keyID == KeyEvent.VK_UP)
					selector = (selector - 1 + selectionChoices) % selectionChoices;
				else if (keyID == KeyEvent.VK_DOWN)
					selector = (selector + 1 + selectionChoices) % selectionChoices;
				else if (keyID == KeyEvent.VK_ENTER)
					selectMenuOption(selector);
				break;
			case SINGLEPLAYER:
				if (keyID == KeyEvent.VK_UP) {
					playerPanel[0].up();
				} else if (keyID == KeyEvent.VK_DOWN) {
					playerPanel[0].down();
				} else if (keyID == KeyEvent.VK_RIGHT) {
					playerPanel[0].right();
				} else if (keyID == KeyEvent.VK_LEFT) {
					playerPanel[0].left();
				} else if (keyID == KeyEvent.VK_ENTER) {
					playerPanel[0].select();
				}
				break;
			case MULTIPLAYER:
				// controls for player 1
				if (keyID == KeyEvent.VK_UP) {
					playerPanel[0].up();
				} else if (keyID == KeyEvent.VK_DOWN) {
					playerPanel[0].down();
				} else if (keyID == KeyEvent.VK_RIGHT) {
					playerPanel[0].right();
				} else if (keyID == KeyEvent.VK_LEFT) {
					playerPanel[0].left();
				} else if (keyID == KeyEvent.VK_ENTER) {
					playerPanel[0].select();
				}
				// controls for player 2
				else if (keyID == KeyEvent.VK_W) {
					playerPanel[1].up();
				} else if (keyID == KeyEvent.VK_S) {
					playerPanel[1].down();
				} else if (keyID == KeyEvent.VK_D) {
					playerPanel[1].right();
				} else if (keyID == KeyEvent.VK_A) {
					playerPanel[1].left();
				} else if (keyID == KeyEvent.VK_Q) {
					playerPanel[1].select();
				}
				// controls for player 3
				else if (keyID == KeyEvent.VK_T) {
					playerPanel[2].up();
				} else if (keyID == KeyEvent.VK_G) {
					playerPanel[2].down();
				} else if (keyID == KeyEvent.VK_H) {
					playerPanel[2].right();
				} else if (keyID == KeyEvent.VK_F) {
					playerPanel[2].left();
				} else if (keyID == KeyEvent.VK_R) {
					playerPanel[2].select();
				}
				// controls for player 4
				else if (keyID == KeyEvent.VK_I) {
					playerPanel[3].up();
				} else if (keyID == KeyEvent.VK_K) {
					playerPanel[3].down();
				} else if (keyID == KeyEvent.VK_L) {
					playerPanel[3].right();
				} else if (keyID == KeyEvent.VK_J) {
					playerPanel[3].left();
				} else if (keyID == KeyEvent.VK_U) {
					playerPanel[3].select();
				}
				break;
			case OPTIONS:
				break;
		}
		// updates screen, calls paintComponent method
		removeAll();
		repaint();
	}
	
	private void selectMenuOption(int selection) {
		switch (selection) {	
			case 0:	// if the user selected singleplayer
				currentScreen = SCREEN.SINGLEPLAYER;
				startAnimation(1);
				
				// updates screen, calls paintComponent method
				removeAll();
				repaint();
				//removeKeyListener(this);
				//OutputManager.startSinglePlayer();
				break;
			case 1:	// multiplayer
				currentScreen = SCREEN.MULTIPLAYER;
				OutputManager.changeScreenSize(1100, 624);
				startAnimation(init_NumOfPlayers);	// Starts the player selection screen with the specified number of players
				break;
			case 2:
				
				break;
			case 3:	// if user selects quit then quit the game
				OutputManager.quitGame();
				break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}
