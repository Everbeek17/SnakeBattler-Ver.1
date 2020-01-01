import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class PlayerSelectionPanel {

	private static enum OPTION { 
		COLOR, STYLE, READYUP;
		// these two methods increment/decrement
		public OPTION next() { return values()[(this.ordinal() + 1) % values().length]; }
	    public OPTION prev() { return values()[(this.ordinal() - 1 + values().length) % values().length]; }
	}
	
	private static Font menuItem_font = new Font("TimesRoman", Font.PLAIN, 24);
	private static Font selector_font = new Font("TimesRoman", Font.BOLD, 24);
	
	// the stroke used for the selection square and the default one
	private static final BasicStroke squareStroke = new BasicStroke(2);
	private static final BasicStroke defaultStroke = new BasicStroke();
	
	private Player player;
	
	private OPTION selector;	// which choice currently highlighted
	private boolean inSubMenu;		// if currently inside sub menu
	private Position colorSelector;	// which color is currently selected
	
	private boolean ready;
	
	// returns if the player has ready'd up or not
	public boolean isReady() { return ready; }
	
	public PlayerSelectionPanel(Player p) {
		player = p;
		selector = OPTION.COLOR;	// defaults highlighted choice to 'Color'
		colorSelector = new Position(0,0);	// defaults color choice to top left
		ready = false;	// defaults player to not ready
	}
	
	public void down() { 
		if (!inSubMenu)
			selector = selector.next(); 
		else
			if (selector == OPTION.COLOR)
				colorSelector.setY((colorSelector.getY() + 1) % 9);
	}
	public void up() {
		if (!inSubMenu)
		selector = selector.prev(); 
		else
			if (selector == OPTION.COLOR)	// do we need to check if in COLOR option?
				colorSelector.setY((colorSelector.getY() - 1 + 9) % 9);
	}
	public void left() {
		switch (selector) {
			case COLOR:
				if (inSubMenu) {
					colorSelector.setX((colorSelector.getX() - 1 + 13) % 13);
				}
				break;
			case STYLE:
				if (inSubMenu) {
					
				}
				break;
		}
	}
	public void right() {
		switch (selector) {
			case COLOR:
				if (inSubMenu) {
					colorSelector.setX((colorSelector.getX() + 1) % 13);
				}
				break;
			case STYLE:
				if (inSubMenu) {
					
				}
				break;
		}
	}
	
	public void paintPanel(Graphics2D g, int x, int y) {
		
		// draws the empty white square
		g.setColor(Color.white);
		g.fillRect(x, y, 199, 200);	
		
		// paints the word 'Color'
		g.setFont(menuItem_font);
		g.drawString("Color", x + 75, y + 250);
		
		// paints the word 'Style'
		g.drawString("Style", x + 78, y + 310);
		
		if (ready)
			g.drawString("Ready!", x + 68, y + 370);
		else	// paints the word 'Ready Up'
			g.drawString("Ready Up", x + 55, y + 370);
		
		
		
		// draws something different depending on which item is highlighted
		switch (selector) {
			case COLOR:
				if (!inSubMenu) {
					// paints the selectors
					g.setFont(selector_font);
					g.drawString(">", x + 15, y + 250);
					g.drawString("<", x + 175, y + 250);
				} else {	// draw a big rectangle with different color choices available
					
					// draw the outer rectangle
					g.setColor(Color.white);
					g.fillRect(x + 7, y + 257, 188, 133);
					
					// draws each color rectangle
					int R = 0, G = 0, B = 0;
					for (int xC = 0; xC < 13; xC++)
						for (int yC = 0; yC < 9; yC++) {
							switch (xC) {
								case 12:
									R = G = B = 5 + yC * 25;
									break;
								case 11:
									if (yC < 5) {
										R = 51 * yC;
										G = 0;
									} else {
										G = 51 * (yC - 5);
									}
									B = 25 * yC;
									break;
								case 10:
									if (yC < 5) {
										R = B = 51 * yC;
										G = 0;
									} else {
										G = 51 * (yC - 5);
									}
									break;
								case 9:
									if (yC < 5) {
										B = 51 * yC;
										G = 0;
									} else {
										G = 51 * (yC - 5);
									}
									R = 25 * yC;
									break;
								case 8:
									if (yC < 5) {
										B = 51 * yC;
										G = 0;
										R = 0;
									} else {
										R = G = 51 * (yC - 5);
									}
									break;
								case 7:
									if (yC < 5) {
										B = 51 * yC;
										R = 0;
									} else {
										R = 51 * (yC - 5);
									}
									G = 25 * yC;
									break;
								case 6:
									if (yC < 5) {
										G = B = 51 * yC;
										R = 0;
									} else {
										R = 51 * (yC - 5);
									}
									break;
								case 5:
									if (yC < 5) {
										G = 51 * yC;
										R = 0;
									} else {
										R = 51 * (yC - 5);
									}
									B = 25 * yC;
									break;
								case 4:
									if (yC < 5) {
										G = 51 * yC;
										R = B = 0;
									} else {
										R = B = 51 * (yC - 5);
									}
									break;
								case 3:
									if (yC < 5) {
										G = 51 * yC;
										B = 0;
									} else {
										B = 51 * (yC - 5);
									}
									R = 25 * yC;
									break;
								case 2:
									if (yC < 5) {
										R = G = 51 * yC;
										B = 0;
									} else {
										B = 51 * (yC - 5);
									}
									break;
								case 1:
									if (yC < 5) {
										R = 51 * yC;
										B = 0;
									} else {
										B = 51 * (yC - 5);
									}
									G = 25 * yC;
									break;
								case 0:
									if (yC < 5) {
										R = 51 * yC;
										G = B = 0;
									} else {
										G = B = 51 * (yC - 5);
									}
									break;
							}
							g.setColor(new Color(R, G, B));	// sets color to calculated color and draws square
							g.fillRect(x + 10 + (xC * 14), y + 260 + (yC * 14), 14, 14);
							if (colorSelector.getX() == xC && colorSelector.getY() == yC)	// if selector is here make snake this color
								player.setColor(new Color(R, G, B));
						}
					
					// draws a highlighter around which color is selected
					g.setColor(Color.white);
					g.setStroke(squareStroke);	// sets stroke to something thicker
					g.drawRect(x + 10 + (colorSelector.getX() * 14), y + 260 + (colorSelector.getY() * 14), 14, 14);
					g.setStroke(defaultStroke);	// resets stroke back to default
				}
				break;
			case STYLE:
				// paints the selectors
				g.setFont(selector_font);
				g.drawString(">", x + 15, y + 310);
				g.drawString("<", x + 175, y + 310);
				break;
			case READYUP:
				if (!ready) {
					// paints the selectors
					g.setFont(selector_font);
					g.drawString(">", x + 15, y + 370);
					g.drawString("<", x + 175, y + 370);
				} else {
					
					
				}
				break;
		}
		
		// draws the demoSnake's head
		g.setColor(player.getColor());
		g.fillRect(x + 87, y + 87, 25, 25);
		
		// draw each demoSnake square that's not the head
		for (int i = 1; i < player.getLength(); i++)
			g.fillRect((player.getCoords(i).getX() - player.getCoords(0).getX()) * 25 + x + 87,
					(player.getCoords(i).getY() - player.getCoords(0).getY()) * 25 + y + 87, 25, 25);
	}

	public void select() {
		switch (selector) {
			case COLOR:
				if (!inSubMenu)
					inSubMenu = true;
				else {
					inSubMenu = false;	// get out of the color sub menu
				}
				break;
			case STYLE:
				if (!inSubMenu)
					inSubMenu = true;
				else {

					inSubMenu = false;	// get out of the Style sub menu
				}
				break;
			case READYUP:
				if (ready) {
					ready = false;
					inSubMenu = false;	// doesn't allow player to move selector while ready
				} else {
					ready = true;
					inSubMenu = true;
				}
				break;
		}
	}
}
