package calendar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializerDeserializer {
	
	/*
	 * Creates a Java .ser file that is the serialized version of the Calendar
	 * object in the specified destination.
	 * @param calendar the Calendar object to be serialized.
	 * @Param fileOutPath the destination directory for the .ser file.
	 * @return void. 
	 */
	public static void serializeCalendar(Calendar calendar, String fileOutPath) {
		if (calendar == null || fileOutPath == null) {
			throw new NullPointerException();
		}
		try {
			FileOutputStream fileOut = new FileOutputStream(fileOutPath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(calendar);
			out.close();
			fileOut.close();
//			System.out.printf("Serialized data is saved in " + fileOutPath);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/*
	 * Creates a Java .ser file that is the serialized version of the Calendar
	 * object in the specified destination.
	 * @param calendar the Calendar object to be serialized.
	 * @Param fileOutPath the destination directory for the .ser file.
	 * @return void. 
	 */
	public static Calendar deserializeCalendar(String fileInPath) {
		if (fileInPath == null) {
			throw new NullPointerException();
		}
		Calendar calendar = null;  
		try {
			FileInputStream fileIn = new FileInputStream(fileInPath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			calendar = (Calendar) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Calendar class not found");
			c.printStackTrace();
		}
		return calendar;
	}
}
