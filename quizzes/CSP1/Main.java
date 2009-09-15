import gen.*;
import edu.neu.ccs.demeterf.*;

public class Main {
  public static void p(String s){ System.out.print(s); }
  public static void main(String args[]) throws Exception {
    SimpleProblems start = SimpleProblems.UNKNOWN5(System.in); 
    // some sample functionality
    p(start.display() + "\n");
    p(start.UNKNOWN6() + "\n");
    p(start.toStr() + "\n");
    System.out.println(start.equals(start));
    p(Magic.magic(start).display() + "\n");
    p(Magic.magic(start).print() + "\n");
  }
}


