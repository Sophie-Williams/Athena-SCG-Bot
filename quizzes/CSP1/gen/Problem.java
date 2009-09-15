// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of Problem */
public class Problem{
    protected final List<Constraint> constraints;

    /** Construct a(n) Problem Instance */
    public Problem(List<Constraint> constraints){
        this.constraints = constraints;
    }
    /** Is the given object Equal to this Problem? */
    public boolean equals(Object o){
        if(!(o instanceof Problem))return false;
        if(o == this)return true;
        Problem oo = (Problem)o;
        return (((Object)constraints).equals(oo.constraints));
    }
    /** Parse an instance of Problem from the given String */
    public static Problem parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Problem();
    }
    /** Parse an instance of Problem from the given Stream */
    public static Problem parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Problem();
    }
    /** Parse an instance of Problem from the given Reader */
    public static Problem parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Problem();
    }

    /** Field Class for Problem.constraints */
    public static class constraints extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


