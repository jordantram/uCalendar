package frontend_mainpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import calendar.Calendar;
import calendar.SerializerDeserializer;
import course.Course;
import course.CourseBuilder;
import dropbox.DropboxSingleton;
import event.Event;
import frontend_coursepage.CourseButton;
import frontend_eventpage.EventButton;
import frontend_eventpage.EventWindowPanel;
import parser.CalendarBlock;
import parser.Parser;
import tuple.Tuple;
import utilities.CalendarFunctions;

public class MainPageController {
	
	private static MainPage mainPage;
	public static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	private static Color conflict = new Color(204, 0, 0);
	
	public static void setMain(MainPage main) {
		mainPage = main;
		setActionListener();
	}

	public static void setActionListener() {
		mainPage.getNextButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalDate startDay = mainPage.getStartDay();
				LocalDate endDay = mainPage.getEndDay();
				mainPage.setStartDay(startDay.plusWeeks(1));
				mainPage.setEndDay(endDay.plusWeeks(1));
				
				updateWeek();
				updateDisplay();
			}
			
		});
		
		mainPage.getPreviousButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalDate startDay = mainPage.getStartDay();
				LocalDate endDay = mainPage.getEndDay();
				mainPage.setStartDay(startDay.minusWeeks(1));
				mainPage.setEndDay(endDay.minusWeeks(1));
				
				updateWeek();
				updateDisplay();
			}
			
		});
		
		mainPage.getMenuBar().getAddEventItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openEventCreationWindow();
			}
		});
		
		mainPage.getMenuBar().getFileChooserItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				importFile();
			}
		});
		
		mainPage.getMenuBar().getExportCalendarItem().addActionListener(new ActionListener() {

			@Override
		    public void actionPerformed(ActionEvent e) {
				exportCalendar();
		    }
		});
		
		mainPage.getMenuBar().getChangeAccessTokenItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openChangeAccessTokenWindow();
			}
		});
	}
	
	public static void importFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.showOpenDialog(null);
		
		if(fileChooser.getSelectedFile() != null){
			String filePath = fileChooser.getSelectedFile().getPath();
			List<CalendarBlock> blockList = Parser.getCalendarBlocks(filePath);
			List<Course> courseList = CourseBuilder.getCourseMap(blockList);
			List<Event> events = new ArrayList<Event>();
			Calendar calendar = new Calendar(courseList, events);
			MainPageController.transferEventsToNewCalendar(calendar);
		}
	}
	
	/**
	 * Export the calendar to the selected directory (using calendar serializer).
	 */
	public static void exportCalendar() {
	  JFileChooser fileChooser = new JFileChooser();
	  fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	  fileChooser.setDialogTitle("Export calendar");
	  fileChooser.showOpenDialog(null);
	  
	  if (fileChooser.getSelectedFile() != null) {
	    String directoryPath = fileChooser.getSelectedFile().getAbsolutePath();
	    String exportPath = directoryPath + File.separator + "calendar.ser";
	    SerializerDeserializer.serializeCalendar(mainPage.getCalendar(), exportPath);
	  }
	}
	
	public static void openChangeAccessTokenWindow() {
		JFrame changeAccessTokenFrame = new JFrame();
		JPanel panel = new AccessTokenWindowPanel(mainPage, changeAccessTokenFrame);
		changeAccessTokenFrame.getContentPane().add(panel);
		
		changeAccessTokenFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		changeAccessTokenFrame.setSize(500, 200);
		changeAccessTokenFrame.setResizable(false);
		changeAccessTokenFrame.setLocationRelativeTo(null);
		changeAccessTokenFrame.setVisible(true);
	}
	
	public static void openEventCreationWindow() {
		JFrame eventFrame = new JFrame();
		JPanel panel = new EventWindowPanel(mainPage, eventFrame);
		eventFrame.getContentPane().add(panel);
		
		eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		eventFrame.setSize(500, 500);
		eventFrame.setResizable(false);
		eventFrame.setLocationRelativeTo(null);
		eventFrame.setVisible(true);
	}

	public static void transferEventsToNewCalendar(Calendar calendar2) {
		List<Event> eventList = mainPage.getCalendar().getEventList();
		mainPage.setCalendar(calendar2);
		for(Event event: eventList) {
			mainPage.getCalendar().addEvent(event);
		}
		
		createClassesButtons();
		
	}
	
	public static void createClassesButtons() {
		GridBagConstraints c = new GridBagConstraints();
		
		int i = 0;
		for (Course course : mainPage.getCalendar().getCourseList()) {
			for(Tuple<String> block: course.getIntervalList()) {
				CourseButton button = new CourseButton(course, i, block);
				button.setPreferredSize(new Dimension(mainPage.getMaxButtonWidth(), mainPage.getMaxButtonHeight()));
				c.fill = GridBagConstraints.BOTH;
				c.gridheight = mainPage.getTimeY().get(block.getItem2()) - mainPage.getTimeY().get(block.getItem1());
				c.gridx = mainPage.getDayX().get(block.getItem3());
				c.gridy = mainPage.getTimeY().get(block.getItem1());
				mainPage.add(button, c);
				mainPage.getCourseButtons().add(button);
			}
			i++;
		}
		
		updateDisplay();
	}
	
	public static void addEvent(Event event) {
		mainPage.getCalendar().addEvent(event);
		
		updateDisplay();

	}
	
	public static void updateDisplay() {
		GridBagConstraints c = new GridBagConstraints();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		for(EventButton button: mainPage.getEventButtons()) {
			mainPage.remove(button);
		}

		mainPage.getEventButtons().clear();
		
		List<Tuple<String>> intervals = CalendarFunctions.collectCourse(mainPage.getCalendar().getCourseList());
		intervals.addAll(CalendarFunctions.collectEvent(mainPage.getCalendar().getEventFromAToB(mainPage.getStartDay().format(formatter), mainPage.getEndDay().format(formatter))));
		List<Tuple<String>> conflicts = CalendarFunctions.conflict(intervals);
		
		for(Event event: mainPage.getCalendar().getEventFromAToB(mainPage.getStartDay().format(formatter), mainPage.getEndDay().format(formatter))) {
			for(Tuple<String> block: event.getIntervalList()){
				EventButton button = new EventButton(event);
				button.setPreferredSize(new Dimension(mainPage.getMaxButtonWidth(), mainPage.getMaxButtonHeight()));
				if(conflicts != null && conflicts.contains(block)) {
					button.setBackground(conflict);
					button.setForeground(Color.white);
				}
				c.fill = GridBagConstraints.BOTH;
				c.gridheight = mainPage.getTimeY().get(block.getItem2()) - mainPage.getTimeY().get(block.getItem1());
				c.gridx = mainPage.getDayX().get(block.getItem3());
				c.gridy = mainPage.getTimeY().get(block.getItem1());
				mainPage.add(button, c);
				mainPage.getEventButtons().add(button);
			}
		}

		
		for(CourseButton button: mainPage.getCourseButtons()) {
			if(conflicts != null && conflicts.contains(button.getBlock())) {
				button.setBackground(conflict);
				button.setForeground(Color.white);
			}else if(button.getBackground() == conflict) {
				button.changeToOriginalColour();
			}
		}
		
		mainPage.revalidate();
		mainPage.repaint();
	}
	
	public static void deleteCourse(Course course) {
		for(CourseButton button: mainPage.getCourseButtons()) {
			if(button.getCourse().equals(course)) {
				mainPage.remove(button);
			}
		}
		
		// Delete all notes from dropbox.
//		DropboxSingleton.getInstance().deleteFile("/" + course.getCourseCode());
//		mainPage.getCalendar().removeCourse(course);
//		updateDisplay();
	}
	
	public static void deleteEvent(Event event) {
		for(EventButton button: mainPage.getEventButtons()) {
			if(button.getEvent().equals(event)) {
				mainPage.remove(button);
			}
		}
		mainPage.getCalendar().removeEvent(event);
		updateDisplay();
	}
	
	public static void updateWeek() {
		
		
		if(mainPage.getStartDay().getMonth().equals(mainPage.getEndDay().getMonth())) {
			mainPage.getCurrMonthLabel().setText(mainPage.getStartDay().getMonth().toString());
		}else {
			mainPage.getCurrMonthLabel().setText(mainPage.getStartDay().getMonth().toString() + " \u2192 " + mainPage.getEndDay().getMonth().toString());
		}
		
		LocalDate y = mainPage.getStartDay();
		for(int i = 0; i < 7; i++) {
			mainPage.getDayLabels()[i].setText(String.format("<html>" + days[i] + "<br>" + "<center>" + y.getDayOfMonth() + "</center>" + "</html>"));
			if(y.equals(mainPage.getCurrDay())) {
				mainPage.getDayLabels()[i].setForeground(Color.red);
			}else {
				mainPage.getDayLabels()[i].setForeground(Color.black);
			}
			y = y.plusDays(1);
		}
		
		mainPage.revalidate();
		mainPage.repaint();
	}
	
	public static int getHighestEventID() {
		int max = 0;
		for(Event event: mainPage.getCalendar().getEventList()) {
			if(max < event.getThisEventID()) {
				max = event.getThisEventID();
			}
		}
		return max;
	}
}
