import gen.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;


class Magic extends Bc {
  // magically replace each Length object
  // and keep the rest of the structure the same
      Length combine(Length a, UNKNOWN2){return new Length(i+1);}
      UNKNOWN3
      }
  public static SimpleProblems magic(SimpleProblems s){
    return new UNKNOWN4(new Magic()).<SimpleProblems> traverse(s);
  }
}


