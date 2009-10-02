import gen.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;


class Magic extends Bc {
    // magically replace each Length object
    // and keep the rest of the structure the same
    Length combine(Length a, int i){return new Length(i+1);}

    Cons<Variable> combine(List<Variable> c, Variable v) {
        if (c.isEmpty()) {
            /* Empty case (reached the end) append "dontcare" */
            Variable dontcare = new Variable(new ident("dontcare"));
            return new Cons<Variable>(dontcare, c);
        }
        else {
            /* Recursive case, turtles all the way down. */
            Cons<Variable> t = (Cons<Variable>)c;
            return new Cons<Variable>(t.getFirst(),
                    combine(t.getRest(), v));
        }
    }

    public static SimpleProblems magic(SimpleProblems s){
        return new Traversal(new Magic()).<SimpleProblems> traverse(s);
    }
}


