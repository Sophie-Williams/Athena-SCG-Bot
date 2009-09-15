// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;




/** Representation of Constraint */
public class Constraint{
    protected final Relation r;
    protected final List<Variable> vars;

    /** Construct a(n) Constraint Instance */
    public Constraint(Relation r, List<Variable> vars){
        this.r = r;
        this.vars = vars;
    }
    /** Is the given object Equal to this Constraint? */
    public boolean equals(Object o){
        if(!(o instanceof Constraint))return false;
        if(o == this)return true;
        Constraint oo = (Constraint)o;
        return (((Object)r).equals(oo.r))&&(((Object)vars).equals(oo.vars));
    }
    /** Parse an instance of Constraint from the given String */
    public static Constraint parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Constraint();
    }
    /** Parse an instance of Constraint from the given Stream */
    public static Constraint parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Constraint();
    }
    /** Parse an instance of Constraint from the given Reader */
    public static Constraint parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Constraint();
    }

    /** Field Class for Constraint.r */
    public static class r extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Constraint.vars */
    public static class vars extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class ToStr */
    public String toStr(){ return gen.ToStr.ToStrM(this); }
    /** DGP method from Class Print */
    public String print(){ return gen.Print.PrintM(this); }
    /** DGP method from Class Display */
    public String display(){ return gen.Display.DisplayM(this); }

}


