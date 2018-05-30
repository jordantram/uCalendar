package frontend_eventpage;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class EventJMenu extends JMenuBar{

	private JMenuItem deleteEvent;
	
	public EventJMenu(EventPage eventPage) {
		JMenu fileMenu = new JMenu("File");
		this.deleteEvent = new JMenuItem("Delete this event");
		fileMenu.add(deleteEvent);
		this.add(fileMenu);
		this.setBackground(Color.white);
		this.setBorder(null);
	}
	
	public JMenuItem getDeleteEventButton() {
		return this.deleteEvent;
	}
}
