package pile.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for text with lines found matching the search pattern
 */
public class TextFound {
	
	private int textHandle;
	private List<Integer> lineHandles;
	
	
	public TextFound(int textHandle) {
		this.textHandle = textHandle;
		lineHandles = new ArrayList<Integer>();
	}

	public void addLine(int lineHandle) {
		lineHandles.add(lineHandle);
	}
	
	public int getTextHandle() {
		return textHandle;
	}
	
	// Ev. returning an array (of int) instead as in original code?
	public List<Integer> getLineHandles() {
		return lineHandles;
	}
}