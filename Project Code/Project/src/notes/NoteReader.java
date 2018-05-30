package notes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NoteReader {
	
	/*
	 * Returns a String objects representing the contents of the note at notePath.
	 * @param norePath the path to the note file.
	 * @return String a String object representing the contents of the file. 
	 */
	public static String getNoteContents(String notePath) {
		if (notePath == null) {
			throw new NullPointerException();
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		
        try {
            FileReader fileReader = new FileReader(notePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }   
            
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + notePath + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + notePath + "'");                  

        }
		return stringBuilder.toString();
	}
}
