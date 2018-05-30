package utilities;

import tuple.Tuple;

public class Test {
  public static void main(String[] args) {
    System.out.println(CalendarFunctions.timeStringToFloat("6:30"));
    System.out.println((int) 6.5);
    System.out.println(CalendarFunctions.timeFloatToString((float) 6.5));
    
    Tuple<String> time = new Tuple<>("03:00", "12:30", "Mon");
    System.out.println(CalendarFunctions.splitInterval(time, "04:00", "12:30"));
  }
}
