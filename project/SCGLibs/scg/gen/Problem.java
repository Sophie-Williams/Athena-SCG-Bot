// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.evergreen.ir.RelationCore;




/** Representation of Problem */
public class Problem{
    protected final List<Var> vars;
    protected final List<Clause> clauses;

    /** Construct a(n) Problem Instance */
    public Problem(List<Var> vars, List<Clause> clauses){
        this.vars = vars;
        this.clauses = clauses;
    }
    /** Is the given object Equal to this Problem? */
    public boolean equals(Object o){
        if(!(o instanceof Problem))return false;
        if(o == this)return true;
        Problem oo = (Problem)o;
        return (((Object)vars).equals(oo.vars))&&(((Object)clauses).equals(oo.clauses));
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

    /** Field Class for Problem.vars */
    public static class vars extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Problem.clauses */
    public static class clauses extends edu.neu.ccs.demeterf.control.Fields.any{}

        /** Implicit Variable List */
        public Problem(List<Clause> cs)
        { this(setOfVars(cs).toList(), cs); }
        /** Collect all the (distinct) Variables in a List of Clauses */
        private static Set<Var> setOfVars(List<Clause> cs){
            return cs.fold(new List.Fold<Clause,Set<Var>>(){
                    public Set<Var> fold(Clause c, Set<Var> r)
                    { return r.union(Set.create(c.getVars())); }
                }, Set.<Var>create());
        }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Problem.clauses */
    public List<Clause> getClauses(){ return clauses; }
    /** Getter for field Problem.vars */
    public List<Var> getVars(){ return vars; }

}


