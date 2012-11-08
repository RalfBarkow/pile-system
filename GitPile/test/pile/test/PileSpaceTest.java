package pile.test;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import pile.engine.IPileSpace;
import pile.engine.PileSpace;
import pile.util.Coordinates;

public class PileSpaceTest {
	
	@Test
	public void test() {
		final boolean allowValueReset = false;
		PileSpace ps = new PileSpace(allowValueReset);
		
		assertEquals(10, ps.setValue(1, 2, 10));
		assertEquals(10, ps.getValue(1, 2));
		
		assertEquals(20, ps.setValue(1, 3, 20));
		assertEquals(20, ps.getValue(1, 3));
		
		assertEquals(30, ps.setValue(2, 4, 30));
		assertEquals(40, ps.setValue(3, 1, 40));
		assertEquals(40, ps.getValue(3, 1));
		
		assertEquals(50, ps.setValue(5, 1, 50));
		assertEquals(50, ps.getValue(5, 1));
				
        Coordinates coords = ps.getCoordinates(10);
        assertNotNull(coords);
        assertEquals(1, coords.getX());
        assertEquals(2, coords.getY());
        
        coords = ps.getCoordinates(20);
        assertNotNull(coords);
        assertEquals(1, coords.getX());
        assertEquals(3, coords.getY());   
        
        coords = ps.getCoordinates(40);
        assertNotNull(coords);
        assertEquals(3, coords.getX());
        assertEquals(1, coords.getY());
        
        List<Integer> l = ps.findValues(1, true, 0, 255);
        assertNotNull(l);
        assertEquals(2, l.size());
        
        l = ps.findValues(2, true, 0, 255);
        assertNotNull(l);
        assertEquals(1, l.size());
        
        l = ps.findValues(3, true, 0, 255);
        assertNotNull(l);
        assertEquals(1, l.size());
        
        l = ps.findValues(5, true, 0, 255);
        assertNotNull(l);
        assertEquals(1, l.size());
        
        l = ps.findValues(2, false, 0, 255);
        assertNotNull(l);
        assertEquals(1, l.size());
        
        l = ps.findValues(3, false, 0, 255);
        assertNotNull(l);
        assertEquals(1, l.size());
        
        l = ps.findValues(4, false, 0, 255);
        assertNotNull(l);
        assertEquals(1, l.size());
        
        l = ps.findValues(5, false, 0, 255);
        assertNotNull(l);
        assertEquals(0, l.size());
        
        l = ps.findValues(4, true, 0, 255);
        assertNotNull(l);
        assertEquals(0, l.size());
	}
	
	@Test
	public void test2() {
		final boolean allowValueReset = false;
        IPileSpace ps = new pile.engine.PileSpace(allowValueReset);

        ps.setValue(1, 2, 3);
        assertEquals("GetValue() failed!", ps.getValue(1, 2), 3);
        assertEquals("GetValue() failed for non-existent relation!", ps.getValue(1, 3), 0);

        assertEquals("SetValue() failed for setting value twice!", ps.setValue(1, 2, 3), 3);
        
        if (allowValueReset) {
        	assertEquals("SetValue() failed for non-unique assignment of v!", ps.setValue(1, 3, 3), 0);
        }
        else try {
        	ps.setValue(1, 3, 3);
        }
        catch (Exception e) {
        	assertNotNull(e);
        }
        

        Coordinates c = ps.getCoordinates(3);
        assertTrue("GetCoords() failed!", c.getX() == 1 && c.getY() == 2);

        List<Integer> vl = ps.findValues(1, true, 0,0);
        assertEquals("FindValues() retrieved wrong number of values!", vl.size(), 1);
        assertEquals("FindValues() retrieved wrong value!", vl.get(0).intValue(), 3);

        ps.setValue(1, 4, 5);
        vl = ps.findValues(1, true, 0, 0);
        assertEquals("FindValues().2 retrieved wrong number of values!", vl.size(), 2);
        assertEquals("FindValues().2 retrieved wrong value!", vl.get(0).intValue(), 3);
        assertEquals("FindValues().3 retrieved wrong value!", vl.get(1).intValue(), 5);
	}
}