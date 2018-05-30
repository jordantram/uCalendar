package course;

import java.util.ArrayList;
import java.util.List;

import parser.CalendarBlock;

public class CourseBuilder {
	
	/*
	 * Constructs a list of courses based on the block list constructed by the parser.
	 * @param blockList a list of CalendarBlock objects constructed by Parser.
	 * @return a list of Course objects.
	 */
	public static List<Course> getCourseMap(List<CalendarBlock> blockList) {
		List<Course> courseList = new ArrayList<Course>();
		for(CalendarBlock block : blockList) {
			if(courseList.isEmpty()) {
				List<CalendarBlock> courseBlocks = new ArrayList<CalendarBlock>();
				courseBlocks.add(block);
				courseList.add(new Course(courseBlocks));
			} else {
				int found = 0;
				for(Course curr : courseList) {

					if(curr.getBlockList().get(0).getCode().equals(block.getCode())) {
						found = 1;
						curr.addNewBlock(block);
					}
				}
				
				if(found != 1) {
					List<CalendarBlock> courseBlocks = new ArrayList<CalendarBlock>();
					courseBlocks.add(block);
					courseList.add(new Course(courseBlocks));
				}
 			}
		}
		return courseList;
	}
}
