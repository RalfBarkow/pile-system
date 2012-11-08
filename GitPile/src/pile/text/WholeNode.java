package pile.text;

import java.util.ArrayList;
import java.util.List;

class WholeNode {
    private int handle;
    private List<CaseAgnosticHandle> followers;

    public WholeNode(int handle)
    {
        this.handle = handle;
        followers = new ArrayList<CaseAgnosticHandle>();
    }
    
    public int handle() {
    	return handle;
    }
    
    public List<CaseAgnosticHandle> followers() {
    	return followers;
    }
    
    public String toString() {
    	return Integer.toString(handle);
    }

}
