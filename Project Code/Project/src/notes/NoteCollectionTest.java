package notes;

import java.util.Arrays;

public class NoteCollectionTest {
	public static void main(String args[]) {
		NotesCollection noteCollection = new NotesCollection();
		Note note1 = new Note("CS1");
		Note note2 = new Note("CS2");
		Note note3 = new Note("CS3");
		Note note4 = new Note("CS4");
		Note note5 = new Note("CS5");
		noteCollection.addNote("06-03-2018", note1);
		noteCollection.addNote("06-04-2018", note2);
		noteCollection.addNote("06-05-2018", note3);
		noteCollection.addNote("06-06-2018", note4);
		noteCollection.addNote("06-07-2018", note5);
		System.out.println(Arrays.toString(noteCollection.getAllNotesIncreasing().toArray()));
		System.out.println(Arrays.toString(noteCollection.getAllNotesDecreasing().toArray()));
		System.out.println(Arrays.toString(noteCollection.getNotesBetweenIncreasing("06-04-2018", "06-06-2018").toArray()));
		System.out.println(Arrays.toString(noteCollection.getNotesBetweenDecreasing("06-04-2018", "06-06-2018").toArray()));
	}
}
