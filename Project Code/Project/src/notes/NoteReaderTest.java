package notes;

public class NoteReaderTest {

	public static void main(String[] args) {
		
		String note = NoteReader.getNoteContents("C:\\Users\\user\\Desktop\\Bugs.txt");
		System.out.println(note);
	}

}
