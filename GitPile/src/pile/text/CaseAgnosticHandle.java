package pile.text;

/**
 * Structure to store upper & lower case handles for a character
 */
public class CaseAgnosticHandle {

	private int uch, lch;
	
    public CaseAgnosticHandle(int upperCaseHandle, int lowerCaseHandle) {
        uch = upperCaseHandle;
        lch = lowerCaseHandle;
    }
    
    public int handle() {
    	return lch;
    }
    
    /**
     * Either the other object is a case agnostic handle of the same kind (having the
     * same uch and lch values) or it is an Integer object representing
     * the same handle value.
     */
    @Override
    public boolean equals(Object o) {
    	
    	// Attention: Not sure if the equals method should also accept equality with
    	// other CaseAgnosticHandles having the same uch and lch values.
    	//if (o instanceof CaseAgnosticHandle) {
        //	CaseAgnosticHandle pc = (CaseAgnosticHandle) o;
        //	return uch == pc.getUpperCaseHandle() && lch == pc.getLowerCaseHandle();
    	//}
    	//else 
    		if (o instanceof Integer) {
    		Integer ic = (Integer) o;
    		return ic.equals(uch) || ic.equals(lch);
    	}
    	else
    		return false;
    }
    
    @Override
    // TODO: In the original code returns uch, but maybe it would be more logical
    // to return lch, becuase lch is also the handle value?
    public int hashCode() {
    	return uch;
    }
}