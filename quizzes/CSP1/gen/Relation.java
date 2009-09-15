// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of Relation */
public class Relation{
    protected final Length length;
    protected final Positive pos;

    /** Construct a(n) Relation Instance */
    public Relation(Length length, Positive pos){
        this.length = length;
        this.pos = pos;
    }
    /** Is the given object Equal to this Relation? */
    public boolean equals(Object o){
        if(!(o instanceof Relation))return false;
        if(o == this)return true;
        Relation oo = (Relation)o;
        return (((Object)length).equals(oo.length))&&(((Object)pos).equals(oo.pos));
    }
    /** Parse an instance of Relation from the given String */
    public static Relation parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Relation();
    }
    /** Parse an instance of Relation from the given Stream */
    public static Relation parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Relation();
    }
    /** Parse an instance of Relation from the given Reader */
    public static Relation parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Relation();
    }

    /** Field Class for Relation.length */
    public static class length extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Relation.pos */
    public static class pos extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


