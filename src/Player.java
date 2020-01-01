import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.*;

/**
 * A Class to define and help organize each instance of a player
 * 
 * @author Erkin Verbeek
 * @versin 1.0.0
 */

// enum for Direction
enum DIR { UP, DOWN, LEFT, RIGHT }	// enum of directions

public class Player {

	private static int maxScale = 24;	// default maxScale is 24 (used for singleplayer)
	
	private boolean controlAllowed;
	private boolean unBounded;		// if player is bounded by the maxScale variable
	private boolean isAlive;		// if the player is currently alive
	
	private DIR currentDir;	// the direction the player is currently facing
	
	private int currentLength;	// the current length/size of the player
	private String PlayerName;	// the player's name

	private static int numOfPlayers;	// keeps track of how many players exist
	public static Player[] allPlayers = new Player[4];	// an array of 4 players to hold all players in
	private static Random randomizer = new Random();	// randomizer used to move foods around
	private static File chompFile = new File("chomp.wav");
	
	
	private Color playerColor;
	
	private Position[] bodyPositions;	// keeps track of where each body part is
	
	private Food food;

	public Player(String name, int X, int Y, int maxSize, Color playerColor) {
		PlayerName = name;
		unBounded = true;		// defines if the player is bounded by the maxScale variable
		this.playerColor = playerColor;	// the player's color
		numOfPlayers++;	// increments number of players
		currentDir = DIR.RIGHT;	// defaults starting direction to right
		bodyPositions = new Position[maxSize];	// sets the size of bodyPositions array
		controlAllowed = true;
		bodyPositions[0] = new Position(X, Y);	// sets the position of the head of the new player
		currentLength++;
		isAlive = true;		// sets the player to start off alive
		food = new Food(-1, -1);	// initializes player's food to off screen
	}
	
	// sets the array of all players
	public static void setArrayOfAllPlayers(Player[] p) { allPlayers = p; }
	
	/** resets the player from the preview window to a playable version **/
	public void initialize() {
		unBounded = false;
		isAlive = true;
		currentDir = DIR.RIGHT;	// sets player facing right
		// ensures every part of player is in bounds
		bodyPositions[0].setX(Math.abs(bodyPositions[0].getX()) % maxScale);
		bodyPositions[0].setY(Math.abs(bodyPositions[0].getY()) % maxScale);
		for (int i = 1; i < currentLength; i++) {	// sets every trailing body part to be to the left of the player
			bodyPositions[i].setX((bodyPositions[0].getX() - i) % maxScale);
			bodyPositions[i].setY(bodyPositions[0].getY() % maxScale);	
		}
		moveFood();	// moves food into playable area
	}
	
	private void chomp() throws UnsupportedAudioFileException, LineUnavailableException, IOException{
	    Clip clip = AudioSystem.getClip();
	    AudioInputStream ais = AudioSystem.
	        getAudioInputStream( chompFile );
	    clip.open(ais);
	    clip.start();
}
	
	private void moveFood() {
		boolean inEmptySpot = false;
		while (!inEmptySpot) {
			// randomizes the new location of the food item
			food.setPos(randomizer.nextInt(maxScale), randomizer.nextInt(maxScale));
			inEmptySpot = true;	// assumes the food item was moved into a random spot
			// checks if the new spot is actual empty
			for (int i = 0; i < numOfPlayers; i++) {
				// makes sure we're only looking at other players
				if (!allPlayers[i].getCoords(1).equals(bodyPositions[1])) {
					
					// if the new position is the same as another player's food item
					if (allPlayers[i].getFood().getPos().equals(food.getPos())) {
						inEmptySpot = false;
						break;	// exits for-loop
					}
					// checks if the food was spawned ontop of another player
					for (int j = 0; j < allPlayers[i].getLength(); j++)
						if (allPlayers[i].getCoords(j).equals(food.getPos())) {
							inEmptySpot = false;
							break;
						}
				}
			}
		}
	}
	
	private Food getFood() { return food; }

	// moves the player, potentially grabbing the food, growing, and respawning the food
	public void movePlayer() {
		// iterate through each player (including this one)
		for (int i = 0; i < numOfPlayers; i++) {
			
			// check for a head-on collision 
			// make sure we're not looking at the current player (if head locations are the same?)
			
			// iterate through each player's body parts
			for (int j = 0; j < allPlayers[i].getLength(); j++) {
				// if the next position is someone's body part, kill this snake
				if (getNextPos().equals(allPlayers[i].getCoords(j)))
					kill();
			}
			// if the player is gonna grab the food then move the player forward and grow them
			if (getNextPos().equals(food.getPos())) {
				try {
					chomp();
				} catch (Exception ex) {
					System.out.println("Chomp error in player");
				}
				advanceAndGrow();
				moveFood();	// moves the food to a new spot
				return;
			}	
		}
		advance();	// if all looks good, advance the player forward	
	}
	
	// advances the player forward one space, growing itself by one length as well
	public void advanceAndGrow() {
		Position tail = new Position(bodyPositions[currentLength - 1].getX(), bodyPositions[currentLength - 1].getY());
		advance();	// advances all body parts
		// adds another body section at the tail of the player
		bodyPositions[currentLength] = tail;
		currentLength++;
	}
	
	// advances the player forward one space, pulling all it's body parts with it
	public void advance() {
		// moves each of the previous body parts up one
		for (int i = currentLength - 1; i > 0; i--) {
			bodyPositions[i].setX(bodyPositions[i - 1].getX());
			bodyPositions[i].setY(bodyPositions[i - 1].getY());
		}
		// moves the head piece in the specified direction
		bodyPositions[0].setX(getNextX(currentDir));
		bodyPositions[0].setY(getNextY(currentDir));
		controlAllowed = true;	// allows player to select next direction again
	}
	
	// paints the player and their food on the screen
	public void paintPlayerAndFood(Graphics2D g) {
		
		if (isAlive)
			g.setColor(playerColor); 	// sets the brush to the specific player's color
		else
			g.setColor(Color.gray);		// sets the brush to grey
		// iterates through and paints each player's body part
		for (int i = 0; i < currentLength; i++) {
			g.fillRect(bodyPositions[i].getX() * 25, bodyPositions[i].getY() * 25, 25, 25);
		}
		
		// paints each player's food item
		g.fillRect(food.getPos().getX() * 25 + 3, food.getPos().getY() * 25 + 3, 19, 19);
	}
	
	
	
	// returns the the player's head's Y value incremented by 1 in the specified direction
	private int getNextY(DIR heading) {
		if (unBounded) {
			if (heading == DIR.DOWN)
				return bodyPositions[0].getY() + 1;
			else if (heading == DIR.UP)
				return bodyPositions[0].getY() - 1;
		} else {	// if bounded then mod the result to keep within maxScale
			if (heading == DIR.DOWN)
				return (bodyPositions[0].getY() + 1) % maxScale;
			else if (heading == DIR.UP)
				return (bodyPositions[0].getY() - 1 + maxScale) % maxScale;
		}
		return bodyPositions[0].getY();	// if not moving along Y coordinates then return current Y
	}
	
	// returns the the player's head's X value incremented by 1 in the specified direction
	private int getNextX(DIR heading) {
		if (unBounded) {
			if (heading == DIR.RIGHT)
				return bodyPositions[0].getX() + 1;
			else if (heading == DIR.LEFT)
				return bodyPositions[0].getX() - 1;
		} else {	// if bounded then mod the result to keep within maxScale
			if (heading == DIR.RIGHT)
				return (bodyPositions[0].getX() + 1) % maxScale;
			else if (heading == DIR.LEFT)
				return (bodyPositions[0].getX() - 1 + maxScale) % maxScale;
		}
		return bodyPositions[0].getX();	// if not moving along X coordinates then return current X
	}
	
	// returns a new instance of position that has the x and y coordinates of where the player will move to next
	public Position getNextPos() { return new Position(getNextX(currentDir), getNextY(currentDir)); }
	
	// returns the position of the indexed body part
	public Position getCoords(int bodyNum) { return bodyPositions[bodyNum]; }
	
	// returns the length of the player
	public int getLength() { return currentLength; }
	
	// gets the maxScale variable for all players
	public static int getMaxScale() { return maxScale; }
	
	// sets the maxScale variable for all players
	public static void setMaxScale(int scale) { maxScale = scale; }
	
	// returns how many players there are
	public static int getNumPlayers() { return numOfPlayers; }
	
	// returns the player's color
	public Color getColor() { return playerColor; }
	
	public void setColor(Color c) { playerColor = c; }

	// only allows the direction to be set once per interval/update
	public void setNextDir(DIR newDir) {
		if (controlAllowed) {
			switch (currentDir) {	// doesn't allow player to make a 180 degree turn
				case UP:
					if (newDir == DIR.DOWN) { return; }
					break;
				case DOWN:
					if (newDir == DIR.UP) { return; }
					break;
				case LEFT:
					if (newDir == DIR.RIGHT) { return; }
					break;
				case RIGHT:
					if (newDir == DIR.LEFT) { return; }
					break;
			}
			currentDir = newDir;
		}
		controlAllowed = false;	// doesn't allow player to change direction again until moving again
	}
	
	// returns if the player is alive or not
	public boolean isAlive() { return isAlive; }
	
	// sets the player to dead
	public void kill() {
		isAlive = false;
		food.setPos(-1, -1);	// moves the player's food off the screen if they're dead
	}

	public void setLength(int i) { currentLength = i; }
	
}
