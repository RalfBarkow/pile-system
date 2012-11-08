package pile.ui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;


@SuppressWarnings("serial")
public class MainUIView extends JFrame {

	public static final String COMMAND_MENU_OPENFILE = "Open File...";
	public static final String COMMAND_MENU_EXIT = "Exit";
	public static final String COMMAND_MENU_ABOUT = "About";
	public static final String COMMAND_BUTTON_SEARCH = "Search";

	
	private static final String FRAME_TITLE = "Pile System";
	private int frameWidth = 1000, frameHeight = 800;
	
	private JMenuBar menuBar;
	private JMenu menu_file, menu_help;
	private JMenuItem menuItem_openFile, menuItem_exit, menuItem_about;
	private JPanel panel_main, panel_search, panel_east;
	private JLabel label_pattern, label_statusbar;
	private JTextField textField_pattern;
	private JButton button_search;
	private Checkbox checkbox_caseSensitive;
	private JList list_searchResult;
	private DefaultListModel listModel;
	private JScrollPane scrollPane_searchResult;
	
	
	public MainUIView(ActionListener listener) {
		super(FRAME_TITLE);
		
		//
		// Menu
		//
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu_file = new JMenu("File");
		menu_file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu_file);		
		menuItem_openFile = new JMenuItem(COMMAND_MENU_OPENFILE);
		menuItem_openFile.addActionListener(listener);
		menu_file.add(menuItem_openFile);
		
		menu_file.addSeparator();
		menuItem_exit = new JMenuItem(COMMAND_MENU_EXIT);
		menuItem_exit.addActionListener(listener);
		menu_file.add(menuItem_exit);
		
		menu_help = new JMenu("Help");
		menu_help.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu_help);
		menuItem_about = new JMenuItem(COMMAND_MENU_ABOUT);
		menuItem_about.addActionListener(listener);
		menu_help.add(menuItem_about);
		
		//
		// panel_main
		//
		panel_main = new JPanel();
		panel_main.setLayout(new BorderLayout());
				
		//
		// panel_search, panel_result
		//
		panel_search = new JPanel(); // Use default flow layout
		
		//
		// label_pattern
		//
		label_pattern = new JLabel("Pattern");
		panel_search.add(label_pattern);
		
		//
		// textField_pattern
		//
		textField_pattern = new JTextField("", 20);
		
		//
		// button_search
		//
		button_search = new JButton("Search");
		button_search.addActionListener(listener);
		
		//
		// checkbox_casesensitive
		//
		checkbox_caseSensitive = new Checkbox("Case sensitive search", false);
				
		//
		// list_searchresult
		//
		listModel = new DefaultListModel();
		list_searchResult = new JList(listModel);
		list_searchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//
		// scrollPane_searchResult
		//
		scrollPane_searchResult = new JScrollPane(list_searchResult);
		
		//
		// panel_east
		//
		panel_east = new JPanel(new BorderLayout());

		
		//
		// label_statusbar
		//
		label_statusbar = new JLabel(" Status: ");
		label_statusbar.setBorder(LineBorder.createGrayLineBorder());
		panel_main.add(label_statusbar, BorderLayout.SOUTH);
			


		panel_search.add(textField_pattern);
		panel_search.add(button_search);
		panel_search.add(checkbox_caseSensitive);
		panel_main.add(panel_search, BorderLayout.NORTH);

		JPanel panel_west = new JPanel();
		panel_west.setPreferredSize(new Dimension(50, -1));
		panel_east.setPreferredSize(new Dimension(50, -1));
		panel_main.add(panel_west, BorderLayout.WEST);
		
		panel_main.add(panel_east, BorderLayout.EAST);
		
		panel_main.add(scrollPane_searchResult, BorderLayout.CENTER);
		add(panel_main);
		
		
		setSize(frameWidth, frameHeight);		
	}
	
	public void setStatus(String status) {
		label_statusbar.setText(" Status: " + status);
	}

	
	public String getInputText() {
		return textField_pattern.getText();
	}
	
	public boolean isSearchCaseSensitive() {
		return checkbox_caseSensitive.getState();
	}
	
	public void removeAllSearchResults() {
		listModel.removeAllElements();
	}
	
	public void addSearchResult(String searchResult) {
		listModel.addElement(searchResult);
	}
}
