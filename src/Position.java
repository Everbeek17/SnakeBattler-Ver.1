
public class Position {

	private int x, y;
	
	
	public Position (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	
	// compares the x and y values of two positions
	public boolean equals(Position other) {
		return ((x == other.getX()) && (y == other.getY()));
	}
}
