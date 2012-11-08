package pile.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pile.util.Coordinates;
import pile.util.IConstants;

public class PileSpace implements IPileSpace, IConstants {
	
	
	private Map<Integer, SpacePoint> points;
	
	// Flag to indicate whether it is allowed to assign a new value to
	// a given x,y coordinate
	private boolean allowValueOverwrite = false;

	public PileSpace(boolean allowValueReset) {
		points = new HashMap<Integer, SpacePoint>();
		this.allowValueOverwrite = allowValueReset;
	}
	
	
	/**
	 * @see IPileSpace#getValue(int, int)
	 */
	public int getValue(int x, int y) {
		
        if (points.containsKey(x)) {
        	
            SpacePoint xp = points.get(x);
            if (xp.getYPartners().containsKey(y))
                return xp.getYPartners().get(y);
            else
                return 0;
            
        }
        else
            return 0;
	}
	
	
	/**
	 * @see IPileSpace#setValue(int, int, int)
	 */
	public int setValue(int x, int y, int v) {
		
		if (points.containsKey(v)) {
			SpacePoint sp = points.get(v);
			if (sp.getCoordinates().getX() == x && sp.getCoordinates().getY() == y) {
				return v;
			}
			else {
				if (allowValueOverwrite) {
					return 0; // indicate error
				}
				else {
					throw new IllegalStateException(
						"PileSpace.setValue: A space point already exists with x and y coords different from the given.");
				}
			}
		}
		else {
			points.put(v, new SpacePoint(x, y));
			
			// register v with x/y
			if (x > 0) {
				if (!points.containsKey(x)) {
					points.put(x, new SpacePoint(0, 0));
				}
				points.get(x).getYPartners().put(y, v);
			}
			
			if (y > 0) {
				if (!points.containsKey(y)) {
					points.put(y, new SpacePoint(0, 0));
				}
				points.get(y).getXPartners().put(x, v);
			}
			
			return v;
		}
	}
	
	/**
	 * @see IPileSpace#getCoordinates(int)
	 */
	public Coordinates getCoordinates(int v) {
		Coordinates coords = null;
		if (points.containsKey(v)) {
			coords = points.get(v).getCoordinates();
		}
		else {
			throw new IllegalArgumentException("PileSpace.getCoordinates: No SpacePoint found for value v=" + v);
		}
		return coords;
	}
	
	public int getPartition(int v) {
		return v >> (32 - N_QUALIFIER_BITS);
	}
	
	

	/**
	 * @see IPileSpace#findValues(int, boolean, int, int)
	 */
	public java.util.List<Integer> findValues(int xOrY, boolean isXCoordinate, int partitionFrom, int partitionUntil) {
		List<Integer> l = new ArrayList<Integer>();
		
		if (points.containsKey(xOrY)) {
			Map<Integer, Integer> xyPartners =
					isXCoordinate ? points.get(xOrY).getYPartners() : points.get(xOrY).getXPartners();
					
			for (Integer value : xyPartners.values()) {
				int p = getPartition(value);
				if (p >= partitionFrom && p <= partitionUntil) {
					l.add(value);
				}
			}
		}
		
		return l;
	}
	
	
//	public String toString() {
//		StringBuilder sb = new StringBuilder("{");
//		Iterator<Map.Entry<Integer, SpacePoint>> iter = points.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<Integer, SpacePoint> point = iter.next();
//			sb.append("<K=").append(point.getKey()).append(", ");
//			sb.append("V=").append(point.getValue()).append(">");
//			if (iter.hasNext()) {
//				sb.append(NEWLINE);
//			}
//		}
//		sb.append(NEWLINE);
//		return sb.toString();
//	}
	
    /**
     * Attention: Do NOT store references to the internal map of points from outside
     * the pile engine. Always only dynamically access it when needed.
     * @return the internal pile engine used by this agent.
     */
	public Map<Integer, SpacePoint> getInternalMap() {
		return points;
	}
	
}