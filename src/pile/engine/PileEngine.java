package pile.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pile.util.Coordinates;
import pile.util.IConstants;

public class PileEngine implements IPileEngine, IConstants {

    private static final short MIN_Q_BITS = 1;
    private static final short MAX_Q_BITS = 31;

    private IPileSpace space;

    private Map<Integer, Integer> qCounters;
    
    // Will be true if the method createChild was called and a new child
    // was created rather than only returned by the method
    private boolean wasCreated = false;
    
    
    public PileEngine() {
    	space = new PileSpace(false);
    	
    	initialize();
    }
    
    // Could be put inside the constructor...
    private void initialize() {
    	if (N_QUALIFIER_BITS < MIN_Q_BITS || N_QUALIFIER_BITS > MAX_Q_BITS) {
    		throw new RuntimeException(String.format(
    				"The number of Qualifier bits must be in the range of %d to %d", MIN_Q_BITS, MAX_Q_BITS));
    	}

    	// Initialize the (by default 2^8=256) qualifiers
    	qCounters = new HashMap<Integer, Integer>();
    	int stop = (int) Math.pow(2, N_QUALIFIER_BITS);
    	for (int i = 0; i < stop; i++) {
    		
    		// Set a 0 as the initial counter value for all qualifiers
    		qCounters.put(i, 0);
    	}
    }
	
    /**
     * @see IPileEngine#createTop(int)
     */
	public int createTop(int qualifier) {
		int counter = qCounters.get(qualifier);
		++counter;
		qCounters.put(qualifier, counter);
		
		// signed bit shift left (<<) operator has precedence over bitwise AND (|) operator
		int result = qualifier << (32 - N_QUALIFIER_BITS) | counter;
		return result;
	}

	
	/**
	 * @see IPileEngine#createChild(int, int, int, Boolean)
	 */
	public int createChild(int normParent, int assocParent, int qualifier) {
		// Reset wasCreated flag first
		wasCreated = false;
		
		int h = space.getValue(normParent, assocParent);
		
		if (h == 0) {
			h = createTop(qualifier);
			space.setValue(normParent, assocParent, h);
			wasCreated = true;
		}
		//else {
		//	wasCreated = false;
		//}
		return h;
	}
	
	
	public boolean wasChildCreated() {
		return wasCreated;
	}

	/**
	 * @see IPileEngine#getChild(int, int)
	 */
	public int getChild(int normParent, int assocParent) {
		return space.getValue(normParent, assocParent);
	}
	
	/**
	 * @see IPileEngine#getParents(int)
	 */
	public IParents getParents(int child) {
       Coordinates coords = space.getCoordinates(child);
        if (coords.getX() > 0)
            return new RelationParents(coords);
        else
            return null;
	}

	/**
	 * @see IPileEngine#getQualifier(int)
	 */
	public int getQualifier(int relation) {
		return relation >> (32 - N_QUALIFIER_BITS);
	}

	/**
	 * @see IPileEngine#findChildren(int, Manners, int, int)
	 */
	public List<Integer> findChildren(int parent, Manners manner, int qFrom, int qUntil) {
		
		List<Integer> l = space.findValues(parent, manner == Manners.NORMATIVE, qFrom, qUntil);
		return l;
	}	

	
	private class RelationParents implements IParents {
			
        private Coordinates coords;
        
        public RelationParents(Coordinates coords) {
        	this.coords = coords;
        }
        
		public int getNormParent() {
			return coords.getX();
		}
		
		public int getAssocParent() {
			return coords.getY();
		}
	}
	
	public IPileSpace getPileSpace() {
		return space;
	}
}