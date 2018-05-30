package frontend_coursepage;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class CourseJMenu extends JMenuBar{
	
	private JMenuItem deleteCourse;
	
	public CourseJMenu(CoursePage coursePage) {
		JMenu fileMenu = new JMenu("File");
		
		this.deleteCourse = new JMenuItem("Delete this course");
		fileMenu.add(deleteCourse);
		this.add(fileMenu);
		this.setBackground(Color.white);
		this.setBorder(null);
	}
	
	public JMenuItem getDeleteCourseButton() {
		return this.deleteCourse;
	}

}
