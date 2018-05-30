package frontend_mainpage;

import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class PreviousButton extends JButton {
	
	public PreviousButton() {
		this.setText("<");
		this.setBackground(null);
		this.setBorder(null);
		this.setFont(new Font("Serif", Font.PLAIN, 30));
		this.setFocusable(false);
	}

}
