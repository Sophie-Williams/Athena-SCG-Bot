package player.tasks;

import logging.Logger;
import scg.gen.*;
import scg.Util;
import edu.neu.ccs.demeterf.lib.*;

/** Provide Problem instances for accepted Challenges (offered by you) */
public class ProvideTask {
    /** Number of Vars and Clauses to use... */
    private static final int NUM_VARS = 5;
    private static final int NUM_CLAUSES = 10;
    
    private final Logger log;
    /** Create a ProvideTask with the given Logger */
    public ProvideTask(Logger l){ log = l; }

    /** Do the actual providing of Problem instances */
    public ProvideTrans provide(AcceptedChallenge ch){
        log.notify("Providing : #"+ch.getKey());
        return new ProvideTrans(createProb(ch.getPred()), ch.getKey());
    }
    
    /** Static list of variables to draw from... */
    static List<Var> vars = List.buildlist(new List.Build<Var>(){
        public Var build(int i){
            return new Var(new ident("v"+i));
        }
    }, NUM_VARS);
    
    /** Create a random problem instance of the given ProblemType */
    Problem createProb(ProblemType t){
        final List<Integer> type = t.getType();
        return new Problem(vars, 
                List.buildlist(new List.Build<Clause>(){
                    public Clause build(int i){
                        int v = Util.random(vars.length());
                        return new Clause(type.lookup(Util.random(type.length())),
                                List.create(
                                        vars.lookup(v),
                                        vars.lookup((v+1)%vars.length()),
                                        vars.lookup((v+2)%vars.length())));
                    }
                }, NUM_CLAUSES));
    }
}
;