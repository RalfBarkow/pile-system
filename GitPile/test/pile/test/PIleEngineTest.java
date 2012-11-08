package pile.test;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import pile.engine.IPileEngine;
import pile.engine.IPileEngine.Manners;

public class PIleEngineTest {
	
	/**
	 * Tests the pile engine
	 */
	@Test
	public void test() {
		IPileEngine pe = new pile.engine.PileEngine();

        int t1 = pe.createTop(0);
        int t2 = pe.createTop(1);

        int r1 = pe.createChild(t1, t2, 2);
        assertEquals("GetChild() failed!", pe.getChild(t1,  t2), r1);
        assertEquals("GetChild() failed for non-ex relation!", pe.getChild(t1,  r1), 0);


        IPileEngine.IParents p = pe.getParents(r1);
        assertTrue("GetParents() failed!", p.getNormParent() == t1 && p.getAssocParent() == t2);

        int t3 = pe.createTop(2);

        int r2;
        boolean wasCreated;
        r2 = pe.createChild(t1, t2, 4);
        wasCreated = pe.wasChildCreated();
        assertFalse("CreateChild() did create new relation!", wasCreated);
        assertEquals("CreateChild() did not return prev. relation!", r1, r2);

        r2 = pe.createChild(t1, t3, 3);
        wasCreated = pe.wasChildCreated();
        assertTrue("CreateChild() did not create new relation!", wasCreated);

        List<Integer> l;
        l = pe.findChildren(t1, Manners.NORMATIVE, 0, 0);
        assertEquals("FindChildren() did not return right number of children!", l.size(), 0);
        
        l = pe.findChildren(t1, Manners.NORMATIVE, 0, 2);
        assertEquals("FindChildren().2 did not return right number of children!", l.size(), 1);
        assertTrue("FindChildren() did not return correct child!", l.get(0).equals(r1));

        l = pe.findChildren(t1, Manners.NORMATIVE, 0, 3);
        assertEquals("FindChildren().3 did not return right number of children!", l.size(), 2);
        
        // Attention: These two tests only work if the SpacePoint class uses a LinkedHashMap to
        // store its xPartners and yPartners. A HashMap would not guarantee to return the results
        // in the correct order. In such a case, we would instead have to test whether l contains
        // both r1 and r2 without knowing in which position they are.
        assertTrue("FindChildren().3 did not return all children!", l.contains(r1));
        assertTrue("FindChildren().3 did not return all children!", l.contains(r2));
        assertEquals("FindChildren().3 did not return correct child!", l.get(0), new Integer(r1));
        assertEquals("FindChildren().3 did not return correct child!", l.get(1), new Integer(r2));

        l = pe.findChildren(t2, Manners.ASSOCIATIVE, 0, 3);
        assertEquals("FindChildren().4 did not return right number of children!", l.size(), 1);
        assertEquals("FindChildren().4 did not return correct child!", l.get(0), new Integer(r1));
        
        assertEquals("GetQualifier() failed!", pe.getQualifier(r1), 2);

        for (int r : pe.findChildren(t1, Manners.NORMATIVE, 0, 3))
        	//assertTrue(r > 0);
            System.out.println("child: " + r);
	}

}
