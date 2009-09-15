// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of Variable */
public class Variable{
    protected final ident v;

    /** Construct a(n) Variable Instance */
    public Variable(ident v){
        this.v = v;
    }
    /** Is the given object Equal to this Variable? */
    public boolean equals(Object o){
        if(!(o instanceof Variable))return false;
        if(o == this)return true;
        Variable oo = (Variable)o;
        return (((Object)v).equals(oo.v));
    }
    /** Parse an instance of Variable from the given String */
    public static Variable parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Variable();
    }
    /** Parse an instance of Variable from the given Stream */
    public static Variable parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Variable();
    }
    /** Parse an instance of Variable from the given Reader */
    public static Variable parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Variable();
    }

    /** Field Class for Variable.v */
    public static class v extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


