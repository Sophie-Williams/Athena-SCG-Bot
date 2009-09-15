// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.evergreen.ir.RelationCore;




/** Representation of Predicate */
public class Predicate{

    /** Construct a(n) Predicate Instance */
    public Predicate(){
    }
    /** Is the given object Equal to this Predicate? */
    public boolean equals(Object o){
        if(!(o instanceof Predicate))return false;
        if(o == this)return true;
        Predicate oo = (Predicate)o;
        return true;
    }
    /** Parse an instance of Predicate from the given String */
    public static Predicate parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Predicate();
    }
    /** Parse an instance of Predicate from the given Stream */
    public static Predicate parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Predicate();
    }
    /** Parse an instance of Predicate from the given Reader */
    public static Predicate parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Predicate();
    }


        public boolean valid(Problem inst, ProblemType pred){
           // Only vlaid relation numbers are used
           List<Integer> validRelationNumbers = pred.type;
           for (Clause clause : inst.clauses) {
               if (!validRelationNumbers.contains(clause.relnum)) {
                   return false;
               }
           }
           // The constraint that all variables are declared should be checked
           // while parsing
           for (Clause clause : inst.clauses) {
               for (Var var : clause.vars) {
                   if (!inst.getVars().contains(var)) {
                       return false;
                   }
               }
           }
           return true;
        }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }

}


