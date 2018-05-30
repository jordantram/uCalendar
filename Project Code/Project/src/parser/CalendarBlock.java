package parser;

public class CalendarBlock implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String code;
	private String type;
	private String number;
	private String startDate;
	private String startTime;
	private String description;
	private String location;
	private String endDate;
	private String endTime;
	private String day;
	
	public CalendarBlock(            				
			String code, 
			String type,
			String number, 
			String startDate, 
			String startTime,
			String description, 
			String location, 
			String endDate, 
			String endTime,
			String day){
		
		this.code = code;
		this.type = type;
		this.number = number;
		this.startDate = startDate;
		this.startTime = startTime;
		this.description = description;
		this.location = location;
		this.endDate = endDate;
		this.startDate = startDate;
		this.endTime = endTime;
		this.day = this.formatDay(this.startDate);
	}
	
	@Override
	public String toString() {
		return "CalendarBlock [name=" + code + ", type=" + this.type + "]";
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getNumber() {
		return number;
	}

	public String getStartDate() {
		return startDate;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public String getDescription() {
		return description;
	}

	public String getLocation() {
		return location;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public String getDayOfTheWeek() {
		return day;
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
		int f = (k + ((13 * m - 1)/5) + d + (d/4) + (c/4) - (2 * c)) % 7;
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
