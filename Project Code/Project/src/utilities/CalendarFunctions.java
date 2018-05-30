package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import calendar.Calendar;
import course.Course;
import event.Event;
import tuple.Tuple;

public class CalendarFunctions {
	/*
	 * String splice, similar to Python splicing
	 * slice_range taken from:
	 * https://stackoverflow.com/questions/17307761/is-there-a-java-equivalent-to-pythons-easy-string-splicing
	 */
	//can be removed
	/*
	public static String slice_start(String s, int startIndex) {
	    if (startIndex < 0) startIndex = s.length() + startIndex;
	    return s.substring(startIndex);
	}
	//can be removed
	public static String slice_end(String s, int endIndex) {
	    if (endIndex < 0) endIndex = s.length() + endIndex;
	    return s.substring(0, endIndex);
	}
	//can be removed
	public static String slice_range(String s, int startIndex, int endIndex) {
	    if (startIndex < 0) startIndex = s.length() + startIndex;
	    if (endIndex < 0) endIndex = s.length() + endIndex;
	    return s.substring(startIndex, endIndex);
	}
	*/
	
	/*
	 * Converts a representation of a day of the week (first three letters) to a number
	 */
	public static int day(String day) {
		switch(day){
	    case "Sunday":
	        return 0;
	    case "Monday":
	        return 1;
	    case "Tuesday":
	        return 2;
	    case "Wednesday":
	        return 3;
	    case "Thursday":
	        return 4;
	    case "Friday":
	        return 5;
	    case "Saturday":
	        return 6;
	    }
		return -1;
	}
	
	/*
	 * Given a Calendar object, will find all free slots within that calendar in sorted order in a list of tuples.
	 */
	public static List<Tuple<String>> getFreePeriods(Calendar obj) {
		List<Tuple<String>> freeSlots = new ArrayList<Tuple<String>>();
		List<Tuple<String>> intervals = collectCourse(obj.getCourseList());
		intervals.addAll(collectEvent(obj.getEventList()));
		
		
		sort(intervals);
		
		//List<Tuple<String>> schedule = listOfStringToListOfTuple(intervals);
		List<Tuple<String>> schedule = intervals;
		
		addFreeSlots(freeSlots, schedule, "Sunday");
		addFreeSlots(freeSlots, schedule, "Monday");
		addFreeSlots(freeSlots, schedule, "Tuesday");
		addFreeSlots(freeSlots, schedule, "Wednesday");
		addFreeSlots(freeSlots, schedule, "Thursday");
		addFreeSlots(freeSlots, schedule, "Friday");
		addFreeSlots(freeSlots, schedule, "Saturday");
		
		return freeSlots;
	}
	
	/*
	 * Helper function for getFreePeriods(Calendar obj)
	 * Note: schedule arg is in sorted order (important for the working of this algorithm)
	 */
	private static void addFreeSlots(List<Tuple<String>> freeSlots, List<Tuple<String>> schedule, String day) {
		String startTime = "00:00";
		String endTime = "00:00";
		Tuple<String> freeSlot = null;
		Tuple<String> prevSlot = null;
		
		if (schedule.size() == 0 || !schedule.get(0).getItem3().equals(day)) {
			// if there are no time slots taken up on day, the whole day is free
			freeSlot = new Tuple<String>(startTime, "23:59", day);
			freeSlots.add(freeSlot);
		} else {
			while (schedule.size() > 0 && schedule.get(0).getItem3().equals(day)) {
				// iterate through all the booked time slots on the given day
				endTime = schedule.get(0).getItem1();
				if (prevSlot != null && (timeStringToFloat(prevSlot.getItem2()) >= timeStringToFloat(startTime))) {
					// this condition ensures that there will be no conflicts between free slots reported and booked time slots
					startTime = prevSlot.getItem2();
				}
				prevSlot = schedule.remove(0);
				
				if (timeStringToFloat(startTime) < timeStringToFloat(endTime)) {
					freeSlot = new Tuple<String>(startTime, endTime, day);
					freeSlots.add(freeSlot);
				}
			}
		}
		
		if (prevSlot != null) {
			// handles and adds the last free time slot (if it exists), 
			// i.e. from the end time of the last booked time slot to 11:59 pm
			endTime = prevSlot.getItem2();
			if (timeStringToFloat(endTime) < timeStringToFloat("23:59")) {
				startTime = endTime;
				endTime = "23:59";
				freeSlot = new Tuple<String>(startTime, endTime, day);
				freeSlots.add(freeSlot);
			}
		}
	}
	
	/*
	public static List<Tuple<String>> getFreePeriods(Calendar obj1, Calendar obj2) {
	  List<Tuple<String>> conflicts = otherConflict(obj1, obj2);
	  List<Tuple<String>> freePeriods = new ArrayList<>();
	  
	  for (Tuple<String> conflict : conflicts) {
		  
	  }
	  return null;
	}
	*/
	
	/*
	 * Given a Calendar object, will return of list of Tuples (start time, end time, day) that are conflicting
	 * with each other
	 */
	public static List<Tuple<String>> calendarConflict(Calendar obj){
		List<Tuple<String>> intervals = collectCourse(obj.getCourseList());
		intervals.addAll(collectEvent(obj.getEventList())); //this will work (in theory(, removed for testing
		List<Tuple<String>> result = conflict(intervals); 
		if (result.size() == 0) {
			return null;
		}
		/*
		List<Tuple<String>> resultTuple = new ArrayList<Tuple<String>>();
		for (int i = 0;i<result.size();i++) {
			resultTuple.add(stringToTuple(result.get(i)));
		}
		return resultTuple;
		*/
		return result;
	}

	/*
	 * Given two Calendar objects, will return of list of Tuples (start time, end time, day) that are conflicting
	 * between the two
	 */
	public static List<Tuple<String>> otherConflict(Calendar obj1, Calendar obj2){ //can i do this? with same name?
		List<Tuple<String>> intervals = collectCourse(obj1.getCourseList());
		intervals.addAll(collectEvent(obj1.getEventList())); //this will work (in theory(, removed for testing
		intervals.addAll(collectCourse(obj2.getCourseList()));
		intervals.addAll(collectEvent(obj2.getEventList()));
		List<Tuple<String>> result = conflict(intervals);
		/*
		List<Tuple<String>> resultTuple = new ArrayList<Tuple<String>>();
		for (int i = 0;i<result.size();i++) {
			resultTuple.add(stringToTuple(result.get(i)));
		}
		return resultTuple;
		*/
		return result;
	}

	/*
	 * Helper function for conflict functions
	 */
	public static List<Tuple<String>> collectCourse(List<Course> list){
		List<Tuple<String>> result = new ArrayList<Tuple<String>>();
		for (Course course : list) {
			//String courseName = course.getCourseCode();
			//String temp = "";
			List<Tuple<String>> interval = course.getIntervalList();
			for (Tuple<String> intval : interval) {
				//temp += intval + ", " + courseName;
				result.add(intval);
				//temp = "";
			}
		}
		return result;
	}

	/*
	 * Helper function for conflict functions
	 */
	public static List<Tuple<String>> collectEvent(List<Event> list){
		List<Tuple<String>> result = new ArrayList<Tuple<String>>();
		for (Event event : list) {
			//String eventName = Integer.toString(event.getThisEventID());
			//String temp = "";
			List<Tuple<String>> interval = event.getIntervalList();
			for (Tuple<String> intval : interval) {
				//temp += intval + ", " + eventName;
				result.add(intval);
				//temp = "";
			}
		}
		return result;
	}

	/*
	 * Helper function for conflict functions
	 */
	public static List<Tuple<String>> conflict(List<Tuple<String>> intervals){
		List<Tuple<String>> conflicts = new ArrayList<Tuple<String>>();
		sort(intervals);		
		for (int i = 0;i<(intervals.size()-1);i++) {
			String end = intervals.get(i).getItem2();
			String start = intervals.get(i+1).getItem1();
			String endDay = Integer.toString(day(intervals.get(i).getItem3()));
			String startDay = Integer.toString(day(intervals.get(i+1).getItem3()));
			int ends = start.compareTo(end); //if this item finish after next start
			int days = endDay.compareTo(startDay);
			if (ends < 0 && days == 0) {
				if (!conflicts.contains(intervals.get(i))) {
					conflicts.add(intervals.get(i));
				}
				if (!conflicts.contains(intervals.get(i+1))) {
					conflicts.add(intervals.get(i+1));
					}
			}
		}
		return conflicts;
	}

	/*
	 * Given a string in the form of: (13:00, 15:00, Monday), literally anything else
	 * returns a new Tuple<String>, formatted as expected
	 */
	/*
	public static Tuple<String> stringToTuple(String event){			
		String[] tuple = event.split(",");
		Tuple<String> result = new Tuple<String>(slice_range(tuple[0],1,6),slice_range(tuple[1],1,6),slice_range(tuple[2],1,tuple[2].length()-1));
		return result;
	}
	*/

	/*
	 * Returns the given time as a float. E.g. "16:30" -> 16.5.
	 */
	public static float timeStringToFloat(String time) {
		String[] parts = time.split(":");
		float result = Float.parseFloat(parts[0]) + (Float.parseFloat(parts[1]) / 60);
		return result;
	}

	/*
	 * Returns the given time as a string. E.g. 16.5 -> "16:30".
	 */
	public static String timeFloatToString(float time) {
		int hours = (int) time;
		int minutes = (int) ((time - hours) * 60);

		return new String(hours + ":" + minutes);
	}

	/**
	 * Splits the given interval, removing the period between start and end.
	 */
	public static List<Tuple<String>> splitInterval(Tuple<String> interval, String start, String end) {
		float intervalStart = timeStringToFloat(interval.getItem1());
		float intervalEnd = timeStringToFloat(interval.getItem2());

		float splitStart = timeStringToFloat(start);
		float splitEnd = timeStringToFloat(end);

		List<Tuple<String>> result = new ArrayList<>();
		
		if (splitEnd <= intervalStart || splitStart >= intervalEnd) {
		  result.add(interval);
		  return result;
		}

		if (intervalStart < splitStart) {
			result.add(new Tuple<String>(interval.getItem1(), start, interval.getItem3()));
		}

		if (intervalEnd > splitEnd) {
			result.add(new Tuple<String>(end, interval.getItem2(), interval.getItem3()));
		}

		return result;
	}
	
	/*
	 * Given a List<String>, will convert it to a List<Tuple<String>>
	 */
	//can remove????
	/*
	private static List<Tuple<String>> listOfStringToListOfTuple(List<String> listOfString) {
		List<Tuple<String>> resultTuple = new ArrayList<Tuple<String>>();
		for (int i = 0; i < listOfString.size(); i++) {
			resultTuple.add(stringToTuple(listOfString.get(i)));
		}
		
		return resultTuple;
	}
	*/
	
	/*
	 * Sorts a given a list of strings (representing intervals)
	 */
	private static void sort(List<Tuple<String>> intervals) {
		Collections.sort(intervals, new Comparator<Tuple<String>>() {			
			@Override
			public int compare(Tuple<String> arg0, Tuple<String> arg1) {
				// TODO Auto-generated method stub
				String day0 = Integer.toString(day(arg0.getItem3()));
				String day1 = Integer.toString(day(arg1.getItem3()));
				int day = day0.compareTo(day1);
				if (day == 0) {
					String start0 = arg0.getItem1();
					//System.out.println("start0 day " + start0);
					String start1 = arg1.getItem1();
					//System.out.println("start1 day " + start1);
					return start0.compareTo(start1);	
				}
				return day;
			}
		});
	}
}
