// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.control.Fields;
import edu.neu.ccs.demeterf.lib.ident;
import edu.neu.ccs.demeterf.lib.verbatim;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;



/** Computes a nested String representation with field names and values */
public class Display extends edu.neu.ccs.demeterf.ID{
   /** Static stub method for calling display */
   public static String DisplayM(Object o){
      return new edu.neu.ccs.demeterf.Traversal(new Display(),edu.neu.ccs.demeterf.Control.builtins()).<String>traverse(o,"   ");
   }

   public String combine(byte _h_){ return ": byte "+""+_h_; }
   public String combine(short _h_){ return ": short "+""+_h_; }
   public String combine(int _h_){ return ": int "+""+_h_; }
   public String combine(long _h_){ return ": long "+""+_h_; }
   public String combine(float _h_){ return ": float "+""+_h_; }
   public String combine(double _h_){ return ": double "+""+_h_; }
   public String combine(char _h_){ return ": char "+"\'"+escape(""+_h_)+"\'"; }
   public String combine(boolean _h_){ return ": boolean "+""+_h_; }
   public String combine(String _h_){ return ": String "+"\""+escape(""+_h_)+"\""; }
   public String combine(ident _h_){ return ": ident "+""+_h_; }
   public String combine(verbatim _h_){ return ": verbatim "+""+_h_; }


   public String update(Object o, Fields.any f, String d){ return d+"   "; }
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


   public String combine(Cons _h_, String first, String rest, String _d_){
      return ": Cons ("+"\n"+_d_+"<first> "+first+"\n"+_d_+"<rest> "+rest+" )";
   }
   public String combine(Empty _h_, String _d_){
      return ": Empty ("+" )";
   }
   public String combine(SimpleProblems _h_, String constraints, String _d_){
      return ": SimpleProblems ("+"\n"+_d_+"<constraints> "+constraints+" )";
   }
   public String combine(Problem _h_, String constraints, String _d_){
      return ": Problem ("+"\n"+_d_+"<constraints> "+constraints+" )";
   }
   public String combine(Constraint _h_, String r, String vars, String _d_){
      return ": Constraint ("+"\n"+_d_+"<r> "+r+"\n"+_d_+"<vars> "+vars+" )";
   }
   public String combine(Relation _h_, String length, String pos, String _d_){
      return ": Relation ("+"\n"+_d_+"<length> "+length+"\n"+_d_+"<pos> "+pos+" )";
   }
   public String combine(Length _h_, String v, String _d_){
      return ": Length ("+"\n"+_d_+"<v> "+v+" )";
   }
   public String combine(Positive _h_, String v, String _d_){
      return ": Positive ("+"\n"+_d_+"<v> "+v+" )";
   }
   public String combine(Variable _h_, String v, String _d_){
      return ": Variable ("+"\n"+_d_+"<v> "+v+" )";
   }

}


