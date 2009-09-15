// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of Positive */
public class Positive{
    protected final int v;

    /** Construct a(n) Positive Instance */
    public Positive(int v){
        this.v = v;
    }
    /** Is the given object Equal to this Positive? */
    public boolean equals(Object o){
        if(!(o instanceof Positive))return false;
        if(o == this)return true;
        Positive oo = (Positive)o;
        return (((Object)v).equals(oo.v));
    }
    /** Parse an instance of Positive from the given String */
    public static Positive parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Positive();
    }
    /** Parse an instance of Positive from the given Stream */
    public static Positive parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Positive();
    }
    /** Parse an instance of Positive from the given Reader */
    public static Positive parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Positive();
    }

    /** Field Class for Positive.v */
    public static class v extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


