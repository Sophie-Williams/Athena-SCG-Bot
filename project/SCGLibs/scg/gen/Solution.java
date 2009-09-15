// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Solution */
public class Solution{
    protected final Map<Var, Boolean> assign;

    /** Construct a(n) Solution Instance */
    public Solution(Map<Var, Boolean> assign){
        this.assign = assign;
    }
    /** Is the given object Equal to this Solution? */
    public boolean equals(Object o){
        if(!(o instanceof Solution))return false;
        if(o == this)return true;
        Solution oo = (Solution)o;
        return (((Object)assign).equals(oo.assign));
    }
    /** Parse an instance of Solution from the given String */
    public static Solution parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Solution();
    }
    /** Parse an instance of Solution from the given Stream */
    public static Solution parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Solution();
    }
    /** Parse an instance of Solution from the given Reader */
    public static Solution parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Solution();
    }

    /** Field Class for Solution.assign */
    public static class assign extends edu.neu.ccs.demeterf.control.Fields.any{}
 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Solution.assign */
    public Map<Var,Boolean> getAssign(){ return assign; }

}


