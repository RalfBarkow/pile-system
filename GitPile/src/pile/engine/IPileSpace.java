package pile.engine;

import java.util.List;
import java.util.Map;

import pile.util.Coordinates;

public interface IPileSpace {
	
	/**
	 * Put a value into a point in 2D space.
	 * @param x x coordinate in 2D space
	 * @param y y coordinate in 2D space
	 * @param v value (handle) of x/y coords in 2D space
	 * @return the value v (handle)
	 */
	public int setValue(int x, int y, int v);
	
	/**
	 * Retrieve value of point in 2D space.
	 * @param x x coordinate in 2D space
	 * @param y y coordinate in 2D space
	 * @return the value v (handle)
	 */
	public int getValue(int x, int y);
	
	/**
	 * Determine the (x,y) of a coordinate value.
	 * @param v v value (handle) to get x/y coordinates for
	 * @return the coordinates
	 */
	public Coordinates getCoordinates(int v);

	/**
	 * 
	 * @param xOrY x or y coordinate for the values to find
	 * @param isXCoordinate is xOrY the x or the y coordinate of the values to find?
	 * @param partitionFrom
	 * @param partitionUntil
	 * @return list of values where xOrY is the x (or y) coordinate
	 */
	public List<Integer> findValues(int xOrY, boolean isXCoordinate, int partitionFrom, int partitionUntil);

	/**
	 * @param v the value to return the partition for
	 * @return a partition (by default consisting of the leading 8 bits of v)
	 *   or a handle's qualifier in other words
	 */
	public int getPartition(int v);
	
	public Map<Integer, SpacePoint> getInternalMap();
}
