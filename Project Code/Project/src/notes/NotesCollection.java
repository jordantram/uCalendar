package notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NotesCollection implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, List<Note>> notesMap = new LinkedHashMap<String, List<Note>>();
	
	public NotesCollection() {}
	
	/*
	 * Adds a Note object to the current collection of notes.
	 * Notes are separated by date of the class that they were taken on.
	 * @param date a String object that is used as a key to map to a list
	 *             of notes belonging to the date that they were taken on.
	 * @param note Note object to be recorded for the specified lecture, tutorial, or practical.
	 * @return void
	 */
	public void addNote(String date, Note note) {
		if (date == null || note == null) {
			throw new NullPointerException();
		}
		if (this.notesMap.containsKey(date)) {
			notesMap.get(date).add(note);
		}
		else {
			List<Note> noteList = new ArrayList<Note>();
			noteList.add(note);
			notesMap.put(date, noteList);
		}
	}
	
	/*
	 * Return all the notes ordered by their ID.
	 */
	public List<Note> getNotesInAplhaOrder() {
		List<Note> notes= new ArrayList<Note>();
		for (List<Note> c : this.notesMap.values()) {
			notes.addAll(c);
		}
		for (int i = 1; i < notes.size(); i++) {
			for (int j = 0; j < (notes.size() - i); j++) {
				if (notes.get(j).getThisNoteID() > notes.get(j + 1).getThisNoteID()) {
					Collections.swap(notes, j, j + 1);
				}
			}
		}
		return notes;
	}
	/*
	 * Deletes the selected note from the set of notes taken on a specific date, if possible.
	 * @param date A String object representing the date the note was taken.
	 * @param note The Note object to be deleted.
	 * @return void
	 */
	public void deleteNote(String date, Note note) {
		if (date == null || note == null) {
			throw new NullPointerException();
		}
		if (!this.notesMap.containsKey(date)) {
			throw new IllegalArgumentException();
		}
		if (!this.notesMap.get(date).contains(note)) {
			throw new IllegalArgumentException();
		}
		this.notesMap.get(date).remove(note);
		if (notesMap.get(date).isEmpty()) {
			this.notesMap.remove(date);
		}
	}
	
	/*
	 * Returns the list of Note objects corresponding to the date.
	 * @param date String object representing the date for which we want 
	 * 		  to retrieve the notes.
	 * @return a list of Note objects.
	 */
	public List<Note> getNotes(String date) {
		if (date == null) {
			throw new NullPointerException();
		}
		if (!this.notesMap.containsKey(date)) {
			return new ArrayList<Note>();
		}
		return this.notesMap.get(date);
	}
	
	/*
	 * Returns a list of all notes in the collection in increasing order of date.
	 * @return a List object containing Note objects.
	 */
	public List<Note> getAllNotesIncreasing() {
		List<Note> result = new ArrayList<Note>(); 
		for (String key : this.notesMap.keySet()) {
			for (Note note : this.notesMap.get(key)) {
				result.add(note);
			}
		}
		return result;
	}
	
	/*
	 * Returns a list of all notes in the collection in decreasing order of date.
	 * @return a List object containing Note objects.
	 */
	public List<Note> getAllNotesDecreasing() {
		List<Note> result = this.getAllNotesIncreasing();
		Collections.reverse(result);
		return result;
	}
	
	/*
	 * Returns a list of all notes in the collection between start and end (inclusive)
	 * dates in increasing order.
	 * @param startDate a string in the format dd-mm-yyyy representing the start date.
	 * @param startDate a string in the format dd-mm-yyyy representing the end date.
	 * @return a List object containing Note objects.
	 */
	public List<Note> getNotesBetweenIncreasing(String startDate, String endDate) {
		List<Note> result = new ArrayList<Note>();
		boolean startFlag = false;
		boolean endFlag = false;
		for (String key : this.notesMap.keySet()) {
			if (key.equals(startDate)) {
				startFlag = true;
			}
			if (startFlag && !endFlag) {
				for (Note note : this.notesMap.get(key)) {
					result.add(note);
				}
			}
			if (key.equals(endDate)) {
				endFlag = true;
			}
			// Have gone through all the dates between startDate and endDate.
			if (startFlag && endFlag) {
				break;
			}
		}
		return result;
	}
	
	/*
	 * Returns a list of all notes in the collection between start and end (inclusive)
	 * dates in decreasing order.
	 * @param startDate a string in the format dd-mm-yyyy representing the start date.
	 * @param startDate a string in the format dd-mm-yyyy representing the end date.
	 * @return a List object containing Note objects.
	 */
	public List<Note> getNotesBetweenDecreasing(String startDate, String endDate) {
		List<Note> result = this.getNotesBetweenIncreasing(startDate, endDate);
		Collections.reverse(result);
		return result;
	}
}
