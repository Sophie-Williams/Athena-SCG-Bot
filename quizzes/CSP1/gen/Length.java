// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of Length */
public class Length{
    protected final int v;

    /** Construct a(n) Length Instance */
    public Length(int v){
        this.v = v;
    }
    /** Is the given object Equal to this Length? */
    public boolean equals(Object o){
        if(!(o instanceof Length))return false;
        if(o == this)return true;
        Length oo = (Length)o;
        return (((Object)v).equals(oo.v));
    }
    /** Parse an instance of Length from the given String */
    public static Length parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Length();
    }
    /** Parse an instance of Length from the given Stream */
    public static Length parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Length();
    }
    /** Parse an instance of Length from the given Reader */
    public static Length parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Length();
    }

    /** Field Class for Length.v */
    public static class v extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


