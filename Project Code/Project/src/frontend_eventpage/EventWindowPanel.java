package frontend_eventpage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import event.Event;
import frontend_mainpage.MainPage;
import frontend_mainpage.MainPageController;
import tuple.Tuple;
import utilities.CalendarFunctions;


@SuppressWarnings("serial")
public class EventWindowPanel extends JPanel{
	
	private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	private String[] days2 = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
	private int[] ComponentYCoord = {25, 75, 125, 175, 225, 400, 275, 300};
	
	public EventWindowPanel(MainPage mainPage, JFrame eventFrame) {
		this.setLayout(null);
		
		JLabel introLabel = new JLabel("Create Event");
		introLabel.setBounds(200, this.ComponentYCoord[0], 100, 30);
		this.add(introLabel);
		
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setBounds(50, this.ComponentYCoord[1], 100, 30);
		this.add(nameLabel);
		
		JTextField nameTextBox = new JTextField();
		nameTextBox.setBounds(200, this.ComponentYCoord[1], 250, 30);
		this.add(nameTextBox);
		
		String[] time = new String[13];
		String s;
		int x = 9;
		for(int i = 0; i < 13; i++) {
			if(x == 9) {
				s = String.format("0%d:00", x);
			}else {
				s = String.format("%d:00", x);
			}
			
			time[i] = s;
			x++;
		}
		
		JLabel startLabel = new JLabel("Time:");
		startLabel.setBounds(50, this.ComponentYCoord[2], 100, 30);
		this.add(startLabel);
		
		JComboBox<String> timeStartFromBox = new JComboBox<String>(time);
		timeStartFromBox.setBounds(200,this.ComponentYCoord[2],100,30);
		this.add(timeStartFromBox);
		
		JLabel toLabel1 = new JLabel("To:");
		toLabel1.setBounds(320,this.ComponentYCoord[2],30,30);
		this.add(toLabel1);
		
		JComboBox<String> timeEndBox = new JComboBox<String>(time);
		timeEndBox.setBounds(350,this.ComponentYCoord[2],100,30);
		this.add(timeEndBox);
		
		
		JLabel dayLabel = new JLabel("Date:");
		dayLabel.setBounds(50,this.ComponentYCoord[3],100,30);
		this.add(dayLabel);
		
		JComboBox<String> dateStartFromBox = new JComboBox<String>(days);
		dateStartFromBox.setBounds(200,this.ComponentYCoord[3],100,30);
		this.add(dateStartFromBox);
		
		JLabel toLabel2 = new JLabel("To:");
		toLabel2.setBounds(320,this.ComponentYCoord[3],30,30);
		toLabel2.setVisible(false);
		this.add(toLabel2);
		
		JComboBox<String> dateEndBox = new JComboBox<String>(days);
		dateEndBox.setBounds(350,this.ComponentYCoord[3],100,30);
		dateEndBox.setVisible(false);
		this.add(dateEndBox);
		
		String[] repeatList = {"NEVER", "DAILY", "WEEKLY", "MONTHLY"};
		
		JLabel repeatLabel = new JLabel("Repeat options:");
		repeatLabel.setBounds(50,this.ComponentYCoord[4],100,30);
		this.add(repeatLabel);
		
		JComboBox<String> repeatBox = new JComboBox<String>(repeatList);
		repeatBox.setSelectedIndex(0);
		repeatBox.setBounds(200,this.ComponentYCoord[4],250,30);
		this.add(repeatBox);
		
		repeatBox.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(repeatBox.getSelectedItem().toString().equals("NEVER")){
					toLabel2.setVisible(false);
					dateEndBox.setVisible(false);
				}else if(repeatBox.getSelectedItem().toString().equals("DAILY")){
					toLabel2.setVisible(true);
					dateEndBox.setVisible(true);
				}else if(repeatBox.getSelectedItem().toString().equals("WEEKLY")){
					toLabel2.setVisible(false);
					dateEndBox.setVisible(false);
				}else if(repeatBox.getSelectedItem().toString().equals("MONTHLY")){
					toLabel2.setVisible(false);
					dateEndBox.setVisible(false);
				}
			}
			
		});
		
		JLabel descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(50,this.ComponentYCoord[6],100,30);
		this.add(descriptionLabel);
		
		JTextArea descriptionTextArea = new JTextArea();
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
		descriptionScrollPane.setBounds(50, this.ComponentYCoord[7], 400, 75);
		this.add(descriptionScrollPane);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.setBounds(200, this.ComponentYCoord[5], 100, 30);
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean confirmOk = timeStartFromBox.getSelectedIndex() < timeEndBox.getSelectedIndex() && 
									!nameTextBox.getText().equals("");
				
				if(repeatBox.getSelectedItem().toString().equals("DAILY") && (dateStartFromBox.getSelectedIndex() >= dateEndBox.getSelectedIndex())) {
					confirmOk = false;
				}
				
				if(confirmOk) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					int start = Arrays.asList(days2).indexOf((mainPage.getStartDay().getDayOfWeek().toString()));
					int start2 = CalendarFunctions.day(dateStartFromBox.getSelectedItem().toString());
					int end = Arrays.asList(days2).indexOf((mainPage.getEndDay().getDayOfWeek().toString()));
					int end2 = CalendarFunctions.day(dateEndBox.getSelectedItem().toString());
					
					
					LocalDate weekStartDate = mainPage.getStartDay();
					LocalDate weekEndDate = mainPage.getEndDay();
					
					LocalDate startDate = weekStartDate.plusDays(start2 - start);
					LocalDate endDate = weekEndDate.minusDays(end - end2);
					
					String startDateString = startDate.format(formatter);
					String endDateString = endDate.format(formatter);
					System.out.println(repeatBox.getSelectedItem().toString());
					
					if(repeatBox.getSelectedItem().toString().equals("DAILY")){
						Event event = new Event(repeatBox.getSelectedItem().toString(), startDateString, endDateString);
						event.setDescription(descriptionTextArea.getText());
						
						for(int i = start2; i <= end2; i++){
							event.addInterval(new Tuple<String>(timeStartFromBox.getSelectedItem().toString(), timeEndBox.getSelectedItem().toString(), days[i]));
							event.setName(nameTextBox.getText());
						}
						event.addDates();
						MainPageController.addEvent(event);
					}else if(repeatBox.getSelectedItem().toString().equals("WEEKLY")){
						while(endDate.getDayOfYear() < 365){
							endDate = endDate.plusWeeks(1);
							if(endDate.getDayOfYear() + 7 > 365){
								break;
							}
						}
						
						Event event = new Event(repeatBox.getSelectedItem().toString(), startDate.format(formatter), endDate.format(formatter));
						event.setDescription(descriptionTextArea.getText());
						event.addInterval(new Tuple<String>(timeStartFromBox.getSelectedItem().toString(), timeEndBox.getSelectedItem().toString(), dateStartFromBox.getSelectedItem().toString()));
						event.addDates();
						event.setName(nameTextBox.getText());
						MainPageController.addEvent(event);
					}else if(repeatBox.getSelectedItem().toString().equals("MONTHLY")){
						int monthDiff = 12 - endDate.getMonthValue();
						endDate = endDate.plusMonths(monthDiff);
						Event event = new Event(repeatBox.getSelectedItem().toString(), startDate.format(formatter), endDate.format(formatter));
						event.addInterval(new Tuple<String>(timeStartFromBox.getSelectedItem().toString(), timeEndBox.getSelectedItem().toString(), dateStartFromBox.getSelectedItem().toString()));
						event.setName(nameTextBox.getText());
						event.setDescription(descriptionTextArea.getText());
						event.addDates();
						MainPageController.addEvent(event);
					}else{
						System.out.println(startDateString + "   " + endDateString);
						Event event = new Event(repeatBox.getSelectedItem().toString(), weekStartDate.format(formatter), weekEndDate.format(formatter));
						event.addInterval(new Tuple<String>(timeStartFromBox.getSelectedItem().toString(), timeEndBox.getSelectedItem().toString(), dateStartFromBox.getSelectedItem().toString()));
						event.setName(nameTextBox.getText());
						event.setDescription(descriptionTextArea.getText());
						event.addDates();
						MainPageController.addEvent(event);
						
					}
					eventFrame.dispose();
				}
			}
		});
		this.add(confirmButton);
		
	}
	
	

}
