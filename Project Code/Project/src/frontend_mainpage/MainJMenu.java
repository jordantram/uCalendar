package frontend_mainpage;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


@SuppressWarnings("serial")
public class MainJMenu extends JMenuBar{
	
	private JMenuItem addEvent;
	private JMenuItem fileChooser;
	private JMenuItem exportCalendar;
	private JMenuItem changeAccessToken;
	
	public MainJMenu(MainPage mainPage) {
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu dropBoxMenu = new JMenu("DropBox");
		this.setBackground(Color.white);
		this.setBorder(null);
		
		this.addEvent = new JMenuItem("Add new event");
		editMenu.add(addEvent);
		
		this.fileChooser = new JMenuItem("Import Course Calendar");
		fileMenu.add(fileChooser);
		
		this.exportCalendar = new JMenuItem("Export Calendar");
		fileMenu.add(exportCalendar);
		
		this.changeAccessToken = new JMenuItem("Change Access Token");
		dropBoxMenu.add(this.changeAccessToken);
		
		this.add(fileMenu);
		this.add(editMenu);
		this.add(dropBoxMenu);
	}
	
	public JMenuItem getAddEventItem() {
		return this.addEvent;
	}
	
	public JMenuItem getFileChooserItem() {
		return this.fileChooser;
	}
	
	public JMenuItem getExportCalendarItem() {
	  return this.exportCalendar;
	}
	
	public JMenuItem getChangeAccessTokenItem() {
		return this.changeAccessToken;
	}
}
