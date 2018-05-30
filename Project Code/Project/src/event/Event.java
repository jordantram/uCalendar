package event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import notes.Note;
import notes.NotesCollection;
import tuple.Tuple;

public class Event implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// EventID is used to uniquely identify this event.
	private static int eventID = 0;
	private int thisEventID;
	private String name;
	private String description;
	private String startDate;
	private String endDate;
	// List of tuples (start time, end time, day)
	private List<Tuple<String>> intervalList;
	private List<String> membersList;
	// How to repeat the event when the Calendar is rendered.
//  {"NEVER", "DAILY", "WEEKLY", "MONTHLY"};
	private String toRepeat;
	private NotesCollection notesCollection;
	private Map<Integer, Integer> calendarDates;
	private List<String> dates;
	
	public Event(String toRepeat, String startDate, String endDate) {
		eventID += 1;
		this.thisEventID = eventID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.toRepeat = toRepeat;
		this.notesCollection = new NotesCollection();
		intervalList = new ArrayList<Tuple<String>>();
		membersList = new ArrayList<String>();
		this.calendarDates = new HashMap<Integer, Integer>();
		this.calendarDates.put(1, 31);
		this.calendarDates.put(2, 28);
		this.calendarDates.put(3, 31);
		this.calendarDates.put(4, 30);
		this.calendarDates.put(5, 31);
		this.calendarDates.put(6, 30);
		this.calendarDates.put(7, 31);
		this.calendarDates.put(8, 31);
		this.calendarDates.put(9, 30);
		this.calendarDates.put(10, 31);
		this.calendarDates.put(11, 30);
		this.calendarDates.put(12, 31);
		this.dates = new ArrayList<String>();
	}
	
	public static void setStaticID(int staticID) {
		eventID = staticID;
	}
	
	public void addDates(){
		String startDateCopy = this.startDate;
		String maxDate;
		String changeFormat;
		List<String> days = new ArrayList<String>();
		for(Tuple<String> t : this.intervalList) {
			days.add(t.getItem3());
		}
		List<String> addedMonths = new ArrayList<String>();
		while (!startDateCopy.equals(this.endDate)) {
			String[] parts = startDateCopy.split("-");
			changeFormat = parts[2] + parts[1] + parts[0];
			if (this.toRepeat.equals("DAILY")) {
				if (!this.dates.contains(startDateCopy)) {
					this.dates.add(startDateCopy);
				}
			}
			else if (this.toRepeat.equals("WEEKLY")) {
				if (days.contains(this.formatDay(changeFormat))) {
					if (!this.dates.contains(startDateCopy)) {
						this.dates.add(startDateCopy);
					}
				}
			}
			else if (this.toRepeat.equals("MONTHLY")){
				if (!addedMonths.contains(parts[1]) && this.getStartDay().equals(parts[0])){
					if (!this.dates.contains(startDateCopy)) {
						this.dates.add(startDateCopy);
						addedMonths.add(parts[1]);
					}
				}
				else if (!addedMonths.contains(parts[1]) && this.calendarDates.get(this.getNumDay(parts[1])) < this.getNumDay(this.getStartDay())) {
					maxDate = String.valueOf(this.calendarDates.get(this.getNumDay(parts[1]))) + "-" + parts[1] + "-" + parts[2];
					if (!this.dates.contains(maxDate)) {
						this.dates.add(maxDate);
						addedMonths.add(parts[1]);
					}
				}
			}
			else {
				this.dates.add(startDateCopy);
				break;
			}
			if (this.calendarDates.get(Integer.valueOf(parts[1])).equals(Integer.valueOf(parts[0]))) {
				if (Integer.valueOf(parts[1]).intValue() + 1 < 10) {
					startDateCopy = "01-" + "0" + String.valueOf(Integer.valueOf(parts[1]).intValue() + 1) + "-" + parts[2];
				}
				else {
					startDateCopy = "01-" + String.valueOf(Integer.valueOf(parts[1]).intValue() + 1) + "-" + parts[2];
				}
			}
			else {
				if (Integer.valueOf(parts[0]).intValue() + 1 < 10) {
					startDateCopy = "0" + String.valueOf(Integer.valueOf(parts[0]).intValue() + 1) + "-" + parts[1] + "-" + parts[2];
				}
				else {
					startDateCopy = String.valueOf(Integer.valueOf(parts[0]).intValue() + 1) + "-" + parts[1] + "-" + parts[2];
				}
			}
		}
	}

	public int getThisEventID() {
		return this.thisEventID;
	}
	
	public int getNumDay(String day){
		return Integer.valueOf(day).intValue();
	}

	public String getToRepeat() {
		return toRepeat;
	}

	public void setToRepeat(String toRepeat) {
		this.toRepeat = toRepeat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Tuple<String>> getIntervalList() {
		return intervalList;
	}

	public void setIntervalList(List<Tuple<String>> intervalList) {
		this.intervalList = intervalList;
	}
	
	public void addInterval(Tuple<String> interval) {
		this.intervalList.add(interval);
	}
	
	public void removeInterval(Tuple<String> interval) {
		this.intervalList.remove(interval);
	}
	
	public void setMembers(List<String> membersList) {
		this.membersList = membersList;
	}
	
	public void addMember(String member) {
		this.membersList.add(member);
	}
	
	public void removeMember(String member) {
		this.membersList.remove(member);
	}
	
	public void addNote(String date, Note note) {
		this.notesCollection.addNote(date, note);
	}
	
	public void removeNote(String date, Note note) {
		this.notesCollection.deleteNote(date, note);
	}
	
	public List<Note> getNotes(String date) {
		return this.notesCollection.getNotes(date);
	}
	
	public List<Note> getNotesIncreasing(){
		return this.notesCollection.getAllNotesIncreasing();
	}
	
	public List<Note> getNotesDecreasing(){
		return this.notesCollection.getAllNotesDecreasing();
	}
	
	public String getStartDay(){
		String[] parts = this.startDate.split("-");
		return parts[0];
	}
	
	public List<String> getDates() {
		return this.dates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + thisEventID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (thisEventID != other.thisEventID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventID: " + thisEventID;
	}
	
	/*
	 * Reformats a string in the form of YYYYMMDD to a specific day of the week.
	 * Using Zeller's rule to calculate day of the week.
	 * @param date a string object representing a date in the format YYYYMMDD.
	 * @return a string that represents the day of the week.
	 */
	private String formatDay(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6);
		
		int k = Integer.parseInt(day);
		int m = Integer.parseInt(month) - 2;
		if(m < 1) {
			m = 12 + m;
		}
		if(m == 11 || m == 12) {
			year = Integer.toString(Integer.parseInt(year) - 1);
		}
		int d = Integer.parseInt(year.substring(2));
		int c = Integer.parseInt(year.substring(0, 2));
		int f = (((k + ((13 * m - 1)/5) + d + (d/4) + (c/4) - (2 * c)) % 7) + 7) % 7;
		String result = "";
		switch(f) {
		case 1:
			result = "Monday";
			break;
		case 2:
			result = "Tuesday";
			break;
		case 3:
			result = "Wednesday";
			break;
		case 4:
			result = "Thursday";
			break;
		case 5:
			result = "Friday";
			break;
		case 6:
			result = "Saturday";
			break;
		case 0:
			result = "Sunday";
			break;
		}
		return result;
	}
}