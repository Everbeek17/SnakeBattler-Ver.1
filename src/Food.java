
public class Food {

	private Position pos;
	
	public Food(int x, int y) {
		pos = new Position(x, y);
	}
	
	public Position getPos() { return pos; }
	
	public void setPos(int x, int y) {
		pos.setX(x);
		pos.setY(y);
	}
	
}
