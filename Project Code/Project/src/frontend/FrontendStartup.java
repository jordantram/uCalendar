package frontend;


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.dropbox.core.DbxException;

import course.Course;
import event.Event;
import frontend_coursepage.CoursePage;
import frontend_coursepage.CoursePageController;
import frontend_eventpage.EventPage;
import frontend_eventpage.EventPageController;
import frontend_mainpage.MainPage;
import frontend_mainpage.MainPageController;

public class FrontendStartup {
	
	private static CardLayout layout = new CardLayout();
	private static JPanel cards;
	private static MainPage main;
	private static CoursePage course;
	private static EventPage event;
	
	public static void switchCoursePage(Course c) {
		layout.show(cards, "course panel");
		course.setCourse(c);
	}
	
	public static void deleteCourseAndSwitch(Course course) {
		MainPageController.deleteCourse(course);
		switchMainPage();
	}
	
	public static void switchEventPage(Event e) {
		layout.show(cards, "event panel");
		event.setEvent(e);
	}
	
	public static void deleteEventAndSwitch(Event event) {
		MainPageController.deleteEvent(event);
		switchMainPage();
	}
	
	public static void switchMainPage() {
		layout.show(cards, "main panel");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, DbxException {
		JFrame frame = new JFrame("Calendar");
		Container content = frame.getContentPane();
		cards = new JPanel(layout);
		
		main = new MainPage();
		MainPageController.setMain(main);
		MainPageController.createClassesButtons();
		Event.setStaticID(MainPageController.getHighestEventID());
		main.setBackground(Color.white);
		cards.add(main, "main panel");
		
		course = new CoursePage();
		CoursePageController.setCoursePage(course);
		course.setBackground(Color.white);
		course.setVisible(false);
		cards.add(course, "course panel");
		
		event = new EventPage();
		EventPageController.setEventPage(event);
		event.setBackground(Color.white);
		event.setVisible(false);
		cards.add(event, "event panel");
		
		content.add(cards);
		
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                FrontEndUtilities.serializeCalendar(main.getCalendar());
            }
        });
	}

}