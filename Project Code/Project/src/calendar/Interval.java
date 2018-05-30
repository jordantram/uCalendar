package calendar;

/***
 * A closed interval.
 * @author Mang Yau
 *
 */
public class Interval {
  
  private float start;
  
  private float end;
  
  public Interval(float start, float end) {
    this.start = start;
    this.end = end;
  }
  
  public boolean equals(Object other) {
    if (other instanceof Interval) {
      
      return (this.start == ((Interval) other).start &&
              this.end == ((Interval) other).end);
    } else {
      return false;
    }
  }
  
  public boolean intersect(Interval other) {
    return this.start <= other.end &&
           other.start <= this.end;
  }
}
