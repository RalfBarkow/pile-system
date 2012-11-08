package pile;

import pile.text.PileAgent;
import pile.ui.MainUIController;

public class PileMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PileAgent agent = new PileAgent();
		MainUIController uiController = new MainUIController(agent);
		
	}

}
