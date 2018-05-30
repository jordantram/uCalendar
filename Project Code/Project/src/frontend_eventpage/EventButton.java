package frontend_eventpage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import event.Event;
import frontend.FrontendStartup;

@SuppressWarnings("serial")
public class EventButton extends JButton {
		
	private Event event;
	
	public EventButton(Event event) {
		this.event = event;
		this.setText(this.event.getName());
		this.setFocusable(false);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		this.setBackground(new Color(255, 165, 0));
		String description;
		if(event.getDescription().equals("")) {
			description = "<html>" + "Description:" + "<br>" + "<br>"  + "No Description" + "</html>";
		}else {
			description = "<html>" + "Description:" + "<br>" + "<br>"  + event.getDescription() + "</html>";
		}
		this.setToolTipText(description);
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FrontendStartup.switchEventPage(event);
			}
			
		});
	}
	
	public Event getEvent() {
		return this.event;
	}
	
}
