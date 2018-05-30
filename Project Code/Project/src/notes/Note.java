package notes;

import java.util.Date;

public class Note implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Note ID used to distinguish notes.
	private static int noteID = 0;
	private int thisNoteID;
	private String note;
	// Date used to fingerprint when the note was created. 
	private Date date;
	private String noteDate;
	// Path of the file.
	private String noteFilePath;
	
	public Note(String note) {
		noteID += 1;
		this.thisNoteID = noteID;
		this.note = note;
		this.date = new Date();
		this.noteDate = this.extractDate();
	}
	
	// dow mon dd hh:mm:ss zzz yyyy -> dd-mm-yyyy.
	public String extractDate() {
		String[] splitString = date.toString().split("\\s+");
		String day = splitString[2];
		if (day.length() == 1) {
			day = new String("0" + day);
		}
		String month = null;
		switch (splitString[1]) {
		case "Jan": month = "01"; break;
		case "Feb": month = "02"; break;
		case "Mar": month = "03"; break;
		case "Apr": month = "04"; break;
		case "May": month = "05"; break;
		case "Jun": month = "06"; break;
		case "Jul": month = "07"; break;
		case "Aug": month = "08"; break;
		case "Sep": month = "09"; break;
		case "Oct": month = "10"; break;
		case "Nov": month = "11"; break;
		case "Dec": month = "12"; break;
		}
		String year = splitString[5];
		return day + "-" + month + "-" + year;
	}

	public String getNote() {
		return note;
	}
	
	public int getThisNoteID() {
		return this.thisNoteID;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static int getNoteID() {
		return noteID;
	}

	public String getNoteDate() {
		return this.noteDate;
	}
	
	@Override
	public String toString() {
		return this.note;
	}
	
	public void setNoteFilePath(String path) {
	  this.noteFilePath = path;
	}
	
	public String getNoteFilePath() {
	  return this.noteFilePath;
	}
}
