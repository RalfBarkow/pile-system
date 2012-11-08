package pile.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import pile.text.PileAgent;
import pile.text.TextFound;
import pile.util.IConstants;
import pile.util.PilePrinter;

public class MainUIController implements ActionListener, IConstants {

	private PileAgent agent;
	private MainUIView view;
	private JFrame aboutFrame;
	private String lastOpenedFilePath;
	
	public MainUIController(PileAgent agent) {
		this.agent = agent;
		view = new MainUIView(this);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		if (command.equals(MainUIView.COMMAND_BUTTON_SEARCH)) {
			search();
		}
		else if (command.equals(MainUIView.COMMAND_MENU_OPENFILE)) {
			File file = chooseFile();
			if (file != null) {
				readFile(file);
			}
		}
		else if (command.equals(MainUIView.COMMAND_MENU_EXIT)) {
			view.setStatus("Exiting...");
			System.exit(0);
		}
		else if (command.equals(MainUIView.COMMAND_MENU_ABOUT)) {
			showAbout();
		}
		else {
			throw new IllegalArgumentException(
					"PileController.actionPerformed: Received unknown event: [" + event.toString() + "]");
		}
	}
	

	private File chooseFile() {
		if (lastOpenedFilePath == null || "".equals(lastOpenedFilePath.trim())) {
			lastOpenedFilePath = System.getProperty("user.dir");
		}	
		JFileChooser chooser = new JFileChooser(lastOpenedFilePath);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(view);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	lastOpenedFilePath = chooser.getSelectedFile().getAbsolutePath();
	    	
	    	// PileAgent must be reset
	    	view.removeAllSearchResults();
	    	agent.clear();
	    	
	    	return chooser.getSelectedFile();
	    }
	    else
	    	return null;
	}
	
	private void readFile(File file) {
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(file));
			/*int nl =*/ agent.storeText(reader);
			view.setStatus("Successfully read file: " + file.getAbsolutePath());
			if (DEBUG) System.out.println(PilePrinter.getAsString(agent));
			reader.close();
		}
		catch (FileNotFoundException e) {
			// Should not be thrown, because the file was selected using a
			// file chooser dialog. Could however be thrown, if the file was
			// deleted after it was chosen using the file chooser dialog.
			view.setStatus("Could not open file: " + file.getAbsolutePath());
		}
		catch (IOException e) {
			view.setStatus("Could not read from file: " + file.getAbsolutePath());
		}
	}
	
	private void search() {
		String pattern = view.getInputText()/*.trim()*/;
		view.setStatus("Starting search with pattern \"" + pattern + "\"");
		
		long startTime = System.currentTimeMillis();
		Collection<TextFound> searchResults = agent.findTexts(pattern, view.isSearchCaseSensitive());
		long duration = System.currentTimeMillis() - startTime;
		
		view.removeAllSearchResults();
		int i = 0, nLines = 0;
		for (TextFound tf : searchResults) {
			for (int j = 0; j < tf.getLineHandles().size(); j++) {
				String s = agent.retrieveLine(tf.getLineHandles().get(j));
				view.addSearchResult(String.format("%d.%d: %s", i, j, s));
			}
			nLines += tf.getLineHandles().size();
			++i;
		}
		
		view.setStatus(i + "/" + nLines + " text(s)/line(s) found; search time: " + duration + " ms");
		if (DEBUG) System.out.println(PilePrinter.getAsString(agent));
	}
	
	private void showAbout() {
		if (aboutFrame == null) {
			aboutFrame = new JFrame();
			aboutFrame.setSize(400, 200);
			JLabel label = new JLabel();
			label.setText(
					"<html><head></head><body><p>Pile System implementation by Fabian Kostadinov.</p>" +
					"<br /><p>Based on the original reference implementation of Ralf Westphal, Erez " +
					"Elul and the former Pile Systems' team.</p></body></html>");
			aboutFrame.add(label);
			aboutFrame.setTitle("About");
		}
		aboutFrame.setEnabled(true);
		aboutFrame.setVisible(true);
	}
}
