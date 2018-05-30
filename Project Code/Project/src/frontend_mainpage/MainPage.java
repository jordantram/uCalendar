package frontend_mainpage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import calendar.Calendar;
import calendar.SerializerDeserializer;
import course.Course;
import event.Event;
import frontend.FrontEndUtilities;
import frontend_coursepage.CourseButton;
import frontend_eventpage.EventButton;

@SuppressWarnings("serial")
public class MainPage extends JPanel{
	
	private Calendar calendar;
	private LocalDate startDay;
	private LocalDate endDay;
	private LocalDate currDay;
	private JLabel[] timesLabel = new JLabel[12];
	private JLabel[] dayLabel = new JLabel[7];
	private JButton next;
	private JButton previous;
	private JLabel currentMonthLabel;
	private HashMap<String, Integer> timeY = new HashMap<String, Integer>();
	private HashMap<String, Integer> dayX = new HashMap<String, Integer>();
	private List<CourseButton> courseButtons;
	private List<EventButton> eventButtons;
	private MainJMenu menu;
	private int maxButtonWidth = 0;
	private int maxButtonHeight = 0;

	public MainPage() {
		String pwd = System.getProperty("user.dir") + File.separator + "calendar.ser";
		this.calendar = SerializerDeserializer.deserializeCalendar(pwd);
		if(this.calendar == null) {
			this.calendar = new Calendar(new ArrayList<Course>(), new ArrayList<Event>());
		}
		this.courseButtons = new ArrayList<CourseButton>();
		this.eventButtons = new ArrayList<EventButton>();
		this.setLayout(new GridBagLayout());
		this.menu = new MainJMenu(this);
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT );
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.white);
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 0, 0, 8, 1, 0, 0);
		menuPanel.setLayout(flow);
		menuPanel.add(menu);
		this.add(menuPanel, c);
		
		this.previous = new PreviousButton();
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 0, 1, 1, 0, 1);
		this.add(this.previous, c);
		
		this.next = new NextButton();
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 0, 1, 1, 7, 1);
		this.add(this.next, c);
		
		this.currDay = LocalDate.now();

	    // Go forward to get saturday
	    this.endDay = this.currDay;
	    while (endDay.getDayOfWeek() != DayOfWeek.SATURDAY) {
	    	endDay = endDay.plusDays(1);
	    }

	    // Go backwards to get Sunday
	    this.startDay = this.currDay;
	    while (startDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
	    	startDay = startDay.minusDays(1);
	    }
		
		this.currentMonthLabel = new JLabel("", SwingConstants.CENTER);
		if(this.startDay.getMonth().equals(this.startDay.getMonth())) {
			this.currentMonthLabel.setText(this.startDay.getMonth().toString());
		}else {
			this.currentMonthLabel.setText(this.startDay.getMonth().toString() + " \u2192 " + this.startDay.getMonth().toString());
		}
		c.fill = GridBagConstraints.BOTH;
		FrontEndUtilities.setGridBag(c, 1, 0, 6, 1, 1, 1);
		this.add(this.currentMonthLabel, c);
		
		int x = 9;
		String s;
		for(int i = 0; i < 12; i++) {
			if(x > 12) {
				s = String.format("%d:00 pm", x - 12);
			}else {
				if(x == 12) {
					s = String.format("%d:00 pm", x);
				}else {
					s = String.format("%d:00 am", x);
				}
				
			}
		
			this.timesLabel[i] = new JLabel(s, SwingConstants.CENTER);
			if(this.maxButtonHeight < this.timesLabel[i].getPreferredSize().height) {
				this.maxButtonHeight = this.timesLabel[i].getPreferredSize().height;
			}
			c.fill = GridBagConstraints.BOTH;
			FrontEndUtilities.setGridBag(c, 1, 1, 1, 1, 0, 3 + i);
			if(x == 9) {
				s = String.format("0%d:00", x);
			}else {
				s = String.format("%d:00", x);
			}
			this.timeY.put(s, 3 + i);
			this.add(this.timesLabel[i], c);
			x++;
		}
		this.timeY.put("21:00", 15);

		LocalDate y = this.startDay;
		for(int i = 0; i < 7; i++) {
			this.dayLabel[i] = new JLabel(String.format("<html>" + MainPageController.days[i] + "<br>" + "<center>" + y.getDayOfMonth() + "</center>" + "</html>"), SwingConstants.CENTER);
			if(this.maxButtonWidth < this.dayLabel[i].getPreferredSize().width) {
				this.maxButtonWidth = this.dayLabel[i].getPreferredSize().width;
			}
			if(y.equals(this.currDay)) {
				this.dayLabel[i].setForeground(Color.red);
			}else {
				this.dayLabel[i].setForeground(Color.black);
			}
			y = y.plusDays(1);
			c.fill = GridBagConstraints.BOTH;
			FrontEndUtilities.setGridBag(c, 1, 1, 1, 1, i + 1, 2);
			this.dayX.put(MainPageController.days[i], i + 1);
			this.add(dayLabel[i], c);
		}
		
		for(JLabel label: this.dayLabel) {
			label.setPreferredSize(new Dimension(this.maxButtonWidth, label.getPreferredSize().height));
		}
	}
	
	@Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < 12; i++) {
        	g.drawLine(0, this.timesLabel[i].getY(), this.getWidth(), this.timesLabel[i].getY());
        }
        
        g.drawLine(this.dayLabel[0].getX(), this.timesLabel[0].getY(), this.dayLabel[0].getX(), this.getHeight());
    }
	
	public HashMap<String, Integer> getTimeY(){
		return this.timeY;
	}
	
	public HashMap<String, Integer> getDayX(){
		return this.dayX;
	}
	
	public LocalDate getStartDay() {
		return this.startDay;
	}
	
	public void setStartDay(LocalDate start) {
		this.startDay = start;
	}
	
	public LocalDate getEndDay() {
		return this.endDay;
	}
	
	public void setEndDay(LocalDate end) {
		this.endDay = end;
	}
	
	public Calendar getCalendar() {
		return this.calendar;
	}
	
	public void setCalendar(Calendar calendar2) {
		this.calendar = calendar2;
	}
	
	public List<CourseButton> getCourseButtons(){
		return this.courseButtons;
	}
	
	public List<EventButton> getEventButtons(){
		return this.eventButtons;
	}
	
	public JLabel[] getDayLabels() {
		return this.dayLabel;
	}
	
	public JLabel[] getTimeLabels() {
		return this.timesLabel;
	}
	
	public JButton getNextButton() {
		return this.next;
	}
	
	public JButton getPreviousButton() {
		return this.previous;
	}
	
	public MainJMenu getMenuBar() {
		return this.menu;
	}
	
	public JLabel getCurrMonthLabel() {
		return this.currentMonthLabel;
	}

	public LocalDate getCurrDay() {
		return this.currDay;
	}

	public int getMaxButtonWidth() {
		return this.maxButtonWidth;
	}

	public int getMaxButtonHeight() {
		return this.maxButtonHeight;
	}
}
