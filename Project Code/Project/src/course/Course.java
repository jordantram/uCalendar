package course;

import java.util.ArrayList;
import java.util.List;

import notes.Note;
import notes.NotesCollection;
import parser.CalendarBlock;
import tuple.Tuple;

public class Course implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String courseCode;
	private String startDate;
	private String endDate;
	private String courseDescription;
	private List<Tuple<String>> intervalList;
	private List<CalendarBlock> blockList;
	private NotesCollection notesCollection;
	
	public Course(List<CalendarBlock> blockList) {
		if (blockList == null) {
			throw new NullPointerException();
		}
		if (blockList.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.blockList = blockList;
		this.courseCode = this.blockList.get(0).getCode();
		this.startDate = this.formatDate(this.blockList.get(this.blockList.size() - 1).getStartDate());
		this.endDate = this.formatDate(this.blockList.get(this.blockList.size() - 1).getEndDate());
		this.intervalList = this.constructIntervals(blockList);
		this.courseDescription = this.blockList.get(0).getDescription();
		this.notesCollection = new NotesCollection();
	}
	
	/*
	 * Reformats a string in the form of YYYYMMDD to DD-MM-YYYY.
	 * @param date a string object representing a date in the format YYYYMMDD.
	 * @return a string object in the format representing date in the format DD-MM-YYYY. 
	 */
	private String formatDate(String date) {
		// "20180404" -> "04-04-2018"
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(date.substring(6, 8));
		strBuilder.append("-");
		strBuilder.append(date.substring(4, 6));
		strBuilder.append("-");
		strBuilder.append(date.substring(0, 4));
		return strBuilder.toString();
	}
	
	/*
	 * Reformats a string in the form of HHMMSS to HH:MM.
	 * @param time a string object representing time in the format HHMMSS.
	 * @return a string object in the format representing time in the format HH:MM. 
	 */
	public String formatTime(String time) {
		// "160000" -> "16:00"
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(time.substring(0, 2));
		strBuilder.append(":");
		strBuilder.append(time.substring(2, 4));
		return strBuilder.toString();
	}
	
	/*
	 * Constructs a list of Tuple objects where every Tuple contains the start 
	 * and end times of a calendar block as well as the day of the week the block is on.
	 * @param blockList a list of CalendarBlock objects belonging to this course.
	 * @return a list of Tuple objects containing start and end times of a particular block.
	 */
	private List<Tuple<String>> constructIntervals(List<CalendarBlock> blockList) {
		List<Tuple<String>> result = new ArrayList<Tuple<String>>();
		for (CalendarBlock block : blockList) {
			String startTime = this.formatTime(block.getStartTime());
			String endTime = this.formatTime(block.getEndTime());
			String day = block.getDayOfTheWeek();
			result.add(new Tuple<String>(startTime, endTime, day));
		}
		return result;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public List<Tuple<String>> getIntervalList() {
		return intervalList;
	}

	public String getCourseCode() {
		return courseCode;
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
	
	@Override
	public String toString() {
		return "Course code:" + this.courseCode;
	}
	
	public List<CalendarBlock> getBlockList() {
		return this.blockList;
	}
	
	public void addNewBlock(CalendarBlock block) {
		this.blockList.add(block);
		this.intervalList = this.constructIntervals(this.blockList);
	}
}
