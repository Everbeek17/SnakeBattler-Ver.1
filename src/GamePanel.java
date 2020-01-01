import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePanel extends JPanel implements KeyListener {

	private int numOfPlayers;
	private static Font pauseHeaderFont = new Font("TimesRoman", Font.BOLD, 64);
	private static Font pauseFont = new Font("TimesRoman", Font.BOLD, 32);
	
	
	private Timer gameTimer;	// timer game will run on
	
	Random randomizer;
	
	
	/** constructor **/
	public GamePanel() {
		numOfPlayers = Player.getNumPlayers();	// saves how many players there are
		System.out.println(numOfPlayers);
		// instantiates the randomizer
		randomizer = new Random();

		setBackground(Color.white);	// makes background color white
		
		// needed to make the keyboard inputs apply to this
		setFocusable(true);
		
		// adds this class' key listener to this class' JFrame
		addKeyListener(this);
		
		// starts the player moving
		startMoving();
	}
	
	/**
	 * overriding paintComponent allows us to paint things to the screen
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		
		// a Graphics2D object allows us specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		
		// paints each player and their food
		for (int i = 0; i < numOfPlayers; i++)
			Player.allPlayers[i].paintPlayerAndFood(g2);
			
		if (!gameTimer.isRunning()) {	// if the game is paused
			// paints pause screen
			
			
			g2.setColor(Color.white);
			g2.fillRect(190, 100, 220, 100);	// clears space
			
			g2.setColor(Color.black);
			g2.drawRect(190, 100, 220, 100);
			g2.setFont(pauseHeaderFont);
			g2.drawString("Paused", 202, 170);
			
			g2.setFont(pauseFont);
			g2.drawString("player 1 press 'circle' to restart", 110, 240);
		}
		
		
		
		
	}
	
	// initiates the movement of the player
	private void startMoving() {
		
		// creates a function that gets called every specified interval
		int delay = 80; //milliseconds
		ActionListener everySecondPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// moves each player if they're alive
				boolean nobodyAlive = true;	// assumes nobody is alive
				for (int i = 0; i < numOfPlayers; i++)
					if (Player.allPlayers[i].isAlive()) {
						Player.allPlayers[i].movePlayer();
						nobodyAlive = false;
					}
				removeAll();	// repaints the screen
				repaint();
				if (nobodyAlive)	// if everyone is dead, pause the game
					pauseUnpause();
			}
		};
		// saves and starts the timer
		gameTimer = new Timer(delay, everySecondPerformer);	
		gameTimer.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyID = e.getKeyCode();	// saves the ID of the key pressed
		
		// updates the next direction of the player
		switch (keyID) {
			case KeyEvent.VK_ESCAPE:
				// only allows pausing/unpausing if anyone is alive still
				for (int i = 0; i < numOfPlayers; i++)
					if (Player.allPlayers[i].isAlive()) {
						pauseUnpause();
						break;
					}
				break;
			case KeyEvent.VK_O:
				if (!gameTimer.isRunning()) {	// if currently paused
					for (int i = 0; i < numOfPlayers; i++) {
						Player.allPlayers[i].setLength(4);
						Player.allPlayers[i].initialize();
					}
					pauseUnpause();	// unpauses
				}
				break;
			case KeyEvent.VK_UP:
				Player.allPlayers[0].setNextDir(DIR.UP);
				break;
			case KeyEvent.VK_DOWN:
				Player.allPlayers[0].setNextDir(DIR.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				Player.allPlayers[0].setNextDir(DIR.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				Player.allPlayers[0].setNextDir(DIR.RIGHT);
				break;
			case KeyEvent.VK_W:
				if (numOfPlayers > 1) { Player.allPlayers[1].setNextDir(DIR.UP); }
				break;
			case KeyEvent.VK_S:
				if (numOfPlayers > 1) { Player.allPlayers[1].setNextDir(DIR.DOWN); }
				break;
			case KeyEvent.VK_A:
				if (numOfPlayers > 1) { Player.allPlayers[1].setNextDir(DIR.LEFT); }
				break;
			case KeyEvent.VK_D:
				if (numOfPlayers > 1) { Player.allPlayers[1].setNextDir(DIR.RIGHT); }
				break;
			case KeyEvent.VK_T:
				if (numOfPlayers > 2) { Player.allPlayers[2].setNextDir(DIR.UP); }
				break;
			case KeyEvent.VK_G:
				if (numOfPlayers > 2) { Player.allPlayers[2].setNextDir(DIR.DOWN); }
				break;
			case KeyEvent.VK_F:
				if (numOfPlayers > 2) { Player.allPlayers[2].setNextDir(DIR.LEFT); }
				break;
			case KeyEvent.VK_H:
				if (numOfPlayers > 2) { Player.allPlayers[2].setNextDir(DIR.RIGHT); }
				break;
			case KeyEvent.VK_I:
				if (numOfPlayers > 3) { Player.allPlayers[3].setNextDir(DIR.UP); }
				break;
			case KeyEvent.VK_K:
				if (numOfPlayers > 3) { Player.allPlayers[3].setNextDir(DIR.DOWN); }
				break;
			case KeyEvent.VK_J:
				if (numOfPlayers > 3) { Player.allPlayers[3].setNextDir(DIR.LEFT); }
				break;
			case KeyEvent.VK_L:
				if (numOfPlayers > 3) { Player.allPlayers[3].setNextDir(DIR.RIGHT); }
				break;
		}
	}

	private void pauseUnpause() {
		if (gameTimer.isRunning()) {
			gameTimer.stop();
		} else {
			gameTimer.start();
		}
		repaint();
		
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
