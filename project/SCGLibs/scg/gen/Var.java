// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Var */
public class Var implements Comparable<Var>{
    protected final ident id;

    /** Construct a(n) Var Instance */
    public Var(ident id){
        this.id = id;
    }
    /** Is the given object Equal to this Var? */
    public boolean equals(Object o){
        if(!(o instanceof Var))return false;
        if(o == this)return true;
        Var oo = (Var)o;
        return (((Object)id).equals(oo.id));
    }
    /** Parse an instance of Var from the given String */
    public static Var parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Var();
    }
    /** Parse an instance of Var from the given Stream */
    public static Var parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Var();
    }
    /** Parse an instance of Var from the given Reader */
    public static Var parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Var();
    }

    /** Field Class for Var.id */
    public static class id extends edu.neu.ccs.demeterf.control.Fields.any{}

        public int compareTo(Var v)
        { return id.compareTo(v.id); }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Var.id */
    public ident getId(){ return id; }

}


