package pile.util;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import pile.engine.IPileSpace;
import pile.engine.SpacePoint;
import pile.text.PileAgent;
import pile.text.PileAgent.Qualifiers;

public class PilePrinter implements IConstants {

	private PilePrinter() {}
	
	
	public static String getAsString(PileAgent agent) {
		StringBuilder sb = new StringBuilder();
		
		IPileSpace pileSpace = agent.getPileEngine().getPileSpace();
		
		// Sort the map of space points
		SortedMap<Integer, SpacePoint> points
			= new TreeMap<Integer, SpacePoint>(pileSpace.getInternalMap());
		
		
		for (Entry<Integer, SpacePoint> entry : points.entrySet()) {
			
			int[] point = unpackPoint(pileSpace, entry);
			String text = agent.constructStringFromRelations(point[0]);
			appendSpacePoint(sb, point[0], point[1], point[2], point[3], text);
			sb.append(NEWLINE);
		}		
		
		return sb.toString();
	}

	
	/**
	 * @param sb the StringBuilder to append to
	 * @param v the handle
	 * @param p the qualifier
	 * @param x the normative child's handle
	 * @param y the associative child's handle
	 */
	private static void appendSpacePoint(StringBuilder sb, int v, int p, int x, int y, String text) {
		sb.append("h=").append(v).append(", ");
		sb.append("q=").append(getQualifierName(p)).append(", ");
		sb.append("np=").append(x).append(", ");
		sb.append("ap=").append(y).append(", ");
		sb.append(" Text=\"").append(text).append("\"");
	}
	
	private static String getQualifierName(int qualifier) {
		if (qualifier == Qualifiers.LETTERS.value()) {
			return Qualifiers.LETTERS.toString();
		}
		else if (qualifier == Qualifiers.ROOTS.value()) {
			return Qualifiers.ROOTS.toString();
		}
		else if (qualifier == Qualifiers.LINES.value()) {
			return Qualifiers.LINES.toString();
		}
		else if (qualifier == Qualifiers.TEXT.value()) {
			return Qualifiers.TEXT.toString();
		}
		else
			return Integer.toString(qualifier);
	}
	
	
	/**
	 * @param pileSpace
	 * @param entry the space point information to unpack
	 * @return an int array of length 4 having
	 *   the handle in pos 0,
	 *   the qualifier in pos 1,
	 *   the normative child's handle in pos 2
	 *   the associative child's handle in pos 3
	 */
	private static int[] unpackPoint(IPileSpace pileSpace, Entry<Integer, SpacePoint> entry) {
		// handle
		int v = entry.getKey();
		
		// qualifier
		int p = pileSpace.getPartition(v);
		
		Coordinates coords = entry.getValue().getCoordinates();
		
		// normative child's handle
		int x = coords.getX();
		
		// associative child's handle
		int y = coords.getY();
		
		return new int[] {v, p, x, y};
	}
}
