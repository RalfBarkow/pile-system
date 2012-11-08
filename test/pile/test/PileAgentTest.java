package pile.test;


import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class PileAgentTest {

	
	/**
	 * Tests the pile.text.PileAgent class
	 */
	@Test
	public void test() {
		String textStr1 = "peter piper picked a pack of peckles";
		String textStr2 = "the quick brown fox jumps over the lazy dog";
		String textStr3 = "peter petra petro";
		
        pile.text.PileAgent pa = new pile.text.PileAgent();

        int r1 = -1, r2 = -1, r3 = -1;
        try {
	        r1 = pa.storeText(textStr1);
	        assertTrue(r1 > 0);
	        
	        r2 = pa.storeText(textStr2);
	        assertTrue(r2 > 0);
	        
	        r3 = pa.storeText(textStr3);
	        assertTrue(r3 > 0);
        }
        catch (IOException e) {
        	e.printStackTrace();
        	fail("PileAgentTest.test: Failed to store texts. Exception is: " + e.toString());
        }

        String resultStr1 = pa.retrieveText(r1);
        //System.out.println(resultStr1);
        assertNotNull(resultStr1);
        assertEquals(textStr1, resultStr1);
        
        String resultStr2 = pa.retrieveText(r2);
        //System.out.println(resultStr2);
        assertNotNull(resultStr2);
        assertEquals(textStr2, resultStr2);
        
        String resultStr3 = pa.retrieveText(r3);
        //System.out.println(resultStr3);
        assertNotNull(resultStr3);
        assertEquals(textStr3, resultStr3);
        

        String query = "ete", resultStr4 = null;
        for (pile.text.TextFound tf : pa.findTexts(query, false))
        {
        	resultStr4 = pa.retrieveText(tf.getTextHandle());
        	//System.out.println(resultStr4);
        	assertNotNull(resultStr4);
        }
	}

}
