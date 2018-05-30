package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	/*
	 * Parses an ACORN .ics file to produce a list of course blocks with
	 * relevant information.
	 * @param filePath the path to the .ics file.
	 * @return A list of CalendarBlock objects.
	 */
	public static List<CalendarBlock> getCalendarBlocks(String filePath) {
		List<CalendarBlock> blockList = new ArrayList<CalendarBlock>();
		String line = null;
		
		if (filePath == null) {
			throw new IllegalArgumentException();
		}
		
		String name = null;
		String type = null;
		String section = null;
		String startDate = null;
		String startTime = null;
		String endDate = null;
		String endTime = null;
		String description = null;
		String location = null;
		String day = null;
		
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            while (line != null) {
            	if (line.contains("SUMMARY")) {
            		name = line.split(":")[1].split(" ")[0];
            		section = line.split(":")[1].split(" ")[1];
            		type = section.substring(0, 3);
            	}
            	if (line.contains("DTSTART")) {
            		startDate = line.split(":")[1].split("T")[0];
            		startTime = line.split(":")[1].split("T")[1];
            	}
            	if (line.contains("DTEND")) {
            		endTime = line.split(":")[1].split("T")[1];
            	}
            	if (line.contains("RRULE:FREQ=WEEKLY;WKST=MO;UNTIL=")) {
            		String dateAndTime = line.replaceAll("[^0-9]", "");
            		endDate = dateAndTime.substring(0, 8);
            	}
            	if (line.contains("DESCRIPTION")) {
            		description = line.split("\n")[0].split(":")[1];
            	}
            	if (line.contains("LOCATION")) {
            		location = line.split(":")[1];
            	}
            	if (line.contains("END:VEVENT")) {
            		// If we are here then we have all the necessary information.
            		 CalendarBlock block = new CalendarBlock(
            				name, 
            				type,
            				section, 
            				startDate,
            				startTime,
            				description, 
            				location, 
            				endDate,  
            				endTime,
            				day);
            		 blockList.add(block);
            	}
            	line = bufferedReader.readLine();
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filePath + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + filePath + "'");                  
        }
		return blockList;
	}
}
