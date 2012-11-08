package pile.engine;

import java.util.LinkedHashMap;
import java.util.Map;

import pile.util.Coordinates;

public class SpacePoint {

	private Coordinates coords;
	private Map<Integer, Integer> xPartners, yPartners;
	
	public SpacePoint(int xCoord, int yCoord) {
		if (xCoord < 0 || yCoord < 0) {
			throw new IllegalArgumentException("SpacePoint.<constructor>: All coordinates must be of value > 0.");
		}
		
		coords = new Coordinates(xCoord, yCoord);
		
		// Attention: Using a LinkedHashMap rather than a HashMap preserves the order
		// with which elements were originally inserted into xPartners and yPartners.
		//xPartners = new HashMap<Integer, Integer>();
		//yPartners = new HashMap<Integer, Integer>();
		xPartners = new LinkedHashMap<Integer, Integer>();
		yPartners = new LinkedHashMap<Integer, Integer>();
	}
	
	public Coordinates getCoordinates() {
		return coords;
	}
	
	public Map<Integer, Integer> getXPartners() {
		return xPartners;
	}
	
	public Map<Integer, Integer> getYPartners() {
		return yPartners;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(coords.toString()).append(" ");
		sb.append("xPartners:").append(xPartners.toString()).append(" ");
		sb.append("yPartners:").append(yPartners.toString());
		sb.append("]");
		return sb.toString();
	}
}
