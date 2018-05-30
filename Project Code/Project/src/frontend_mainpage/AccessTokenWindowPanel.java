package frontend_mainpage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dropbox.DropboxSingleton;


@SuppressWarnings("serial")
public class AccessTokenWindowPanel extends JPanel{
	
	private int[] ComponentYCoord = {25, 75, 125};
	
	public AccessTokenWindowPanel(MainPage mainPage, JFrame eventFrame) {
		this.setLayout(null);
		
		JLabel introLabel = new JLabel("Change Access Token");
		introLabel.setBounds(190, this.ComponentYCoord[0], 200, 30);
		this.add(introLabel);
		
		JLabel accessTokenLabel = new JLabel("New Access Token:");
		accessTokenLabel.setBounds(50, this.ComponentYCoord[1], 120, 30);
		this.add(accessTokenLabel);
		
		JTextField accessTokenTextBox = new JTextField();
		accessTokenTextBox.setBounds(170, this.ComponentYCoord[1], 265, 30);
		this.add(accessTokenTextBox);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.setBounds(200, this.ComponentYCoord[2], 100, 30);
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean confirmOk = !accessTokenTextBox.getText().equals("");
				
				if(confirmOk) {
					DropboxSingleton.getInstance().reconfigureClient(accessTokenTextBox.getText());
					eventFrame.dispose();
				}
			}
		});
		this.add(confirmButton);
		
	}
}
