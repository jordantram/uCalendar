package frontend_coursepage;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import course.Course;
import frontend.FrontendStartup;
import parser.CalendarBlock;
import tuple.Tuple;

@SuppressWarnings("serial")
public class CourseButton extends JButton {
	
	private Course course;
	private static Color[] colors = {new Color(51, 153, 255), new Color(0, 150, 0), new Color(138, 43, 226), new Color(255, 182, 193), Color.yellow, Color.lightGray};
	private int cCode;
	private Tuple<String> block;
	
	public CourseButton(Course course, int cCode, Tuple<String> block) {
		this.course = course;
		this.block = block;
		this.cCode = cCode;
		this.setText(this.course.getCourseCode());
		this.setFocusable(false);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
		this.setBackground(colors[this.cCode]);
		this.setTextColour();
		CalendarBlock currBlock = null;
		for(CalendarBlock block2: course.getBlockList()) {
			if(block2.getDayOfTheWeek().equals(block.getItem3())) {
				currBlock = block2;
			}
		}
		String buttonText = "<html>" + course.getCourseCode() + "<br>" + currBlock.getNumber() + "<br>" + "Location: " + currBlock.getLocation() + "</html>";
		String description = "<html>" + "Description:" + "<br>" + "<br>"  + course.getCourseDescription() + "</html>";
		this.setText(buttonText);
		this.setToolTipText(description);
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FrontendStartup.switchCoursePage(course);
				
			}
			
		});
	}
	
	public Course getCourse() {
		return this.course;
	}
	
	public Tuple<String> getBlock(){
		return this.block;
	}
	
	private void setTextColour() {
		switch (this.cCode) {
        case 0:  this.setForeground(Color.black);
                 break;
        case 1:  this.setForeground(Color.white);
                 break;
        case 2:  this.setForeground(Color.white);
                 break;
        case 3:  this.setForeground(Color.black);
                 break;
        case 4:  this.setForeground(Color.black);
                 break;
        case 5:  this.setForeground(Color.black);
                 break;
        default: this.setForeground(Color.black);
                 break;
		}
	}
	
	public void changeToOriginalColour() {
		this.setBackground(colors[this.cCode]);
		this.setTextColour();
	}
}
