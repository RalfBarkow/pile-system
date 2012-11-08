package pile.util;

public class Coordinates {
	
	private int xCoord, yCoord;
	
	public Coordinates(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public int getX() {
		return xCoord;
	}
	
	public int getY() {
		return yCoord;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("x=").append(xCoord);
		sb.append(", y=").append(yCoord);
		return sb.toString();
	}

}
