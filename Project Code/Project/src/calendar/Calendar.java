package calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import course.Course;
import event.Event;
import parser.CalendarBlock;
import tuple.Tuple;

public class Calendar implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<Course> courseList;
	private List<Event> eventList;
	
	public Calendar(List<Course> courseList, List<Event> eventList) {
		if (courseList == null || eventList == null) {
			throw new NullPointerException();
		}
		this.courseList = courseList;
		this.eventList = eventList;
	}
	
	
	
	// TODO: Method to identify conflicts inside this Calendar object. DONE
	// TODO: Method to identify conflicts with another Calendar object. DONE
	// TODO: Method to identify free slots between calendar objects.
	
	public void addCourse(Course course) {
		if (course == null) {
			throw new NullPointerException();
		}
		if (!this.courseList.contains(course)) {
			this.courseList.add(course);
		}
	}
	
	public void removeCourse(Course course) {
		if (course == null) {
			throw new NullPointerException();
		}
		if (!this.courseList.contains(course)) {
			throw new IllegalArgumentException();
		}
		this.courseList.remove(course);
	}
	
	public void addEvent(Event event) {
		if (event == null) {
			throw new NullPointerException();
		}
		if (!this.eventList.contains(event)) {
			this.eventList.add(event);
		}
	}
	
	public void removeEvent(Event event) {
		if (event == null) {
			throw new NullPointerException();
		}
		if (!this.eventList.contains(event)) {
			throw new IllegalArgumentException();
		}
		this.eventList.remove(event);
	}
	
	public void removeAllCourses() {
		this.courseList.clear();
	}
	
	public void removeAllEvents() {
		this.eventList.clear();
	}
	
	public void removeEverything() {
		this.courseList.clear();
		this.eventList.clear();
	}
	
	public List<Course> getCourseList() {
		return this.courseList;
	}
	
	public List<Event> getEventList() {
		return this.eventList;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Calendar:" + "\n");
		stringBuilder.append("Courses: ");
		stringBuilder.append(Arrays.toString(this.courseList.toArray()));
		stringBuilder.append("\n");
		stringBuilder.append("Events: ");
		stringBuilder.append(Arrays.toString(this.eventList.toArray()));
		return stringBuilder.toString();
	}
	
	/*
	 * Will show all the events that are populated in a specific calendar in alphabetical order.
	 */
	public void sortEventsByName() {
		for (int i = 1; i < this.eventList.size(); i++) {
			for (int j = 0; j < (this.eventList.size() - i); j++) {
				if (this.eventList.get(j).getName().compareTo(this.eventList.get(j + 1).getName()) > 0) {
					Collections.swap(this.eventList, j, j + 1);
				}
			}
		}
	}
	
	/*
	 * Dates are in the form of DD-MM-YYYY and will return a list of all courses between
	 * the 2 given dates. 
	 */
	public List<Tuple<String>> getCourseFromAToB(String start, String end) {	
		List<Tuple<String>> result = new ArrayList<Tuple<String>>();
		int startMonth = Integer.parseInt(start.substring(3, 5));
		int endMonth = Integer.parseInt(end.substring(3, 5));
		int startDate = Integer.parseInt(start.substring(0, 2));
		int endDate = Integer.parseInt(end.substring(0, 2));
		int year = Integer.parseInt(start.substring(6));
		int courseDate;
		int courseMonth;
		int courseYear;
		for(Course course : this.courseList) {
			System.out.println(course.getCourseCode());
			for(CalendarBlock block : course.getBlockList()) {
				System.out.println(block.getStartDate());
				courseDate = Integer.parseInt(block.getStartDate().substring(6));
				courseMonth = Integer.parseInt(block.getStartDate().substring(4, 6));
				courseYear = Integer.parseInt(block.getStartDate().substring(0,4));
				if(courseYear == year) {
					if(startMonth == endMonth) {
						if((courseMonth == startMonth) && (courseDate > startDate - 1) &&  (courseDate < endDate + 1)) {
							result.add(new Tuple<String>(block.getStartTime(), block.getEndTime(), block.getDayOfTheWeek()));
						}
					} else {
						if(courseMonth == startMonth) {
							if(courseDate > startDate - 1) {
								result.add(new Tuple<String>(block.getStartTime(), block.getEndTime(), block.getDayOfTheWeek()));
							}
						} else if(courseMonth == endMonth) {
							if(courseDate < endDate + 1) {
								result.add(new Tuple<String>(block.getStartTime(), block.getEndTime(), block.getDayOfTheWeek()));
							}
						}	
					}
				}
			}
		}
		return result;
	}
	
	/*
	 * Dates are in the form of DD-MM-YYYY and will return a list of all events between
	 * the 2 given dates. 
	 */
	public List<Event> getEventFromAToB(String start, String end) {
		List<Event> result = new ArrayList<Event>();
		int startMonth = Integer.parseInt(start.substring(3, 5));
		int endMonth = Integer.parseInt(end.substring(3, 5));
		int startDate = Integer.parseInt(start.substring(0, 2));
		int endDate = Integer.parseInt(end.substring(0, 2));
		int year = Integer.parseInt(start.substring(6));
		int eventDate;
		int eventMonth;
		int eventYear;
		for(Event event : this.eventList) {
			for(String date: event.getDates()) {
				eventDate = Integer.parseInt(date.substring(0, 2));
				eventMonth = Integer.parseInt(date.substring(3, 5));
				eventYear = Integer.parseInt(date.substring(6));
				if(eventYear == year) {
					if(startMonth == endMonth) {
						if((eventMonth == startMonth) && (eventDate > startDate - 1) &&  (eventDate < endDate + 1)) {
							result.add(event);
							break;
						}
					} else {
						if(eventMonth == startMonth) {
							if(eventDate > startDate - 1) {
								result.add(event);
								break;
							}
						} else if(eventMonth == endMonth) {
							if(eventDate < endDate + 1) {
								result.add(event);
								break;
							}
						}	
					}
				}
			}
		}
		return result;
	}
}