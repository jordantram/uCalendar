package calendar;

import java.util.ArrayList;
import java.util.List;

import course.Course;
import course.CourseBuilder;
import event.Event;
import parser.CalendarBlock;
import parser.Parser;

public class TestSeriliazerDeserializer {
	public static void main(String[] args) {
		// Make sure Calendar object is constructed fine.
		List<CalendarBlock> blockList = Parser.getCalendarBlocks("C:\\Users\\user\\Desktop\\coursesCalendar.ics");
		List<Course> courseList = CourseBuilder.getCourseMap(blockList);
		List<Event> eventList = new ArrayList<Event>();
		Calendar calendar = new Calendar(courseList, eventList);
//		System.out.println(calendar.toString());
		calendar.addEvent(new Event("NEVER", "06-03-2018", "06-03-2018"));
//		System.out.println(calendar.toString());
		
		// Serialize and then deserialize Calendar object.
		SerializerDeserializer.serializeCalendar(calendar, "C:\\Users\\user\\Desktop\\calendar.ser");
		Calendar newCalendar = SerializerDeserializer.deserializeCalendar("C:\\Users\\user\\Desktop\\calendar.ser");
		
		// Check for valid contents.
		System.out.println(newCalendar.toString());
	}
}
