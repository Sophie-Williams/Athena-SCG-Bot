// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.evergreen.ir.RelationCore;




/** Representation of Clause */
public class Clause{
    protected final int relnum;
    protected final List<Var> vars;

    /** Construct a(n) Clause Instance */
    public Clause(int relnum, List<Var> vars){
        this.relnum = relnum;
        this.vars = vars;
    }
    /** Is the given object Equal to this Clause? */
    public boolean equals(Object o){
        if(!(o instanceof Clause))return false;
        if(o == this)return true;
        Clause oo = (Clause)o;
        return (((Object)relnum).equals(oo.relnum))&&(((Object)vars).equals(oo.vars));
    }
    /** Parse an instance of Clause from the given String */
    public static Clause parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Clause();
    }
    /** Parse an instance of Clause from the given Stream */
    public static Clause parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Clause();
    }
    /** Parse an instance of Clause from the given Reader */
    public static Clause parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Clause();
    }

    /** Field Class for Clause.relnum */
    public static class relnum extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Clause.vars */
    public static class vars extends edu.neu.ccs.demeterf.control.Fields.any{}


    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Clause.vars */
    public List<Var> getVars(){ return vars; }
    /** Getter for field Clause.relnum */
    public int getRelnum(){ return relnum; }

}


