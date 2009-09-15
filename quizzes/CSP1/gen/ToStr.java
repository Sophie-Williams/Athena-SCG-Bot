// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.control.Fields;
import edu.neu.ccs.demeterf.lib.ident;
import edu.neu.ccs.demeterf.lib.verbatim;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;



/** Computes a simple String representation */
public class ToStr extends edu.neu.ccs.demeterf.ID{
   /** Static stub method for calling toStr */
   public static String ToStrM(Object o){
      return new edu.neu.ccs.demeterf.Traversal(new ToStr(),edu.neu.ccs.demeterf.Control.builtins()).<String>traverse(o);
   }

   public String combine(byte _h_)
   { return ""+_h_; }
   public String combine(short _h_)
   { return ""+_h_; }
   public String combine(int _h_)
   { return ""+_h_; }
   public String combine(long _h_)
   { return ""+_h_; }
   public String combine(float _h_)
   { return ""+_h_; }
   public String combine(double _h_)
   { return ""+_h_; }
   public String combine(char _h_)
   { return "\'"+escape(""+_h_)+"\'"; }
   public String combine(boolean _h_)
   { return ""+_h_; }
   public String combine(String _h_)
   { return "\""+escape(""+_h_)+"\""; }
   public String combine(ident _h_)
   { return ""+_h_; }
   public String combine(verbatim _h_)
   { return ""+_h_; }

   public String combine(Cons _h_, String first, String rest)
   { return "Cons("+first+","+rest+")"; }
   public String combine(Empty _h_)
   { return "Empty("+")"; }
   public String combine(SimpleProblems _h_, String constraints)
   { return "SimpleProblems("+constraints+")"; }
   public String combine(Problem _h_, String constraints)
   { return "Problem("+constraints+")"; }
   public String combine(Constraint _h_, String r, String vars)
   { return "Constraint("+r+","+vars+")"; }
   public String combine(Relation _h_, String length, String pos)
   { return "Relation("+length+","+pos+")"; }
   public String combine(Length _h_, String v)
   { return "Length("+v+")"; }
   public String combine(Positive _h_, String v)
   { return "Positive("+v+")"; }
   public String combine(Variable _h_, String v)
   { return "Variable("+v+")"; }
   public static String escape(String s){
      char str[] = s.toCharArray();
      StringBuffer ret = new StringBuffer("");
      for(char c:str)ret.append(escape(c));
      return ret.toString();
   }
   public static String escape(char c){
      switch(c){
      case '\n':return "\\n";  case '\t':return "\\t";
      case '\b':return "\\b";  case '\r':return "\\r";
      case '\f':return "\\f";  case '\\':return "\\\\";
      case '\'':return "\\'"; case '\"':return "\\\"";
      default: return ""+c;
      }
   }

}


