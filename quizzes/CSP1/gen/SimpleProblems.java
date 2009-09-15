// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of SimpleProblems */
public class SimpleProblems{
    protected final List<Problem> constraints;

    /** Construct a(n) SimpleProblems Instance */
    public SimpleProblems(List<Problem> constraints){
        this.constraints = constraints;
    }
    /** Is the given object Equal to this SimpleProblems? */
    public boolean equals(Object o){
        if(!(o instanceof SimpleProblems))return false;
        if(o == this)return true;
        SimpleProblems oo = (SimpleProblems)o;
        return (((Object)constraints).equals(oo.constraints));
    }
    /** Parse an instance of SimpleProblems from the given String */
    public static SimpleProblems parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_SimpleProblems();
    }
    /** Parse an instance of SimpleProblems from the given Stream */
    public static SimpleProblems parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_SimpleProblems();
    }
    /** Parse an instance of SimpleProblems from the given Reader */
    public static SimpleProblems parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_SimpleProblems();
    }

    /** Field Class for SimpleProblems.constraints */
    public static class constraints extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


