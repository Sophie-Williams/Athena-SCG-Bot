package player.tasks;

import logging.Logger;
import scg.gen.*;
import scg.Util;
import edu.neu.ccs.demeterf.lib.*;

/** Solve the provided Problem instances ASAP */
public class SolveTask {

    private final Logger log;
    /** Create a SolveTask with the given Logger */
    public SolveTask(Logger l){ log = l; }

    /** Do the actual solving of Problem instances */
    public SolveTrans solve(ProvidedChallenge ch){
        log.notify("Solving : #"+ch.getKey());
        return new SolveTrans(solve(ch.getInstance()), ch.getKey());
    }
    
    /** Solve the given Problem Instance */
    Solution solve(Problem p){
        return new Solution(randomAssign(p.getVars()));
    }
    
    /** Random Assignment to each of the variables */
    Map<Var, Boolean> randomAssign(List<Var> vs){
        return Map.create(vs.map(new List.Map<Var, Entry<Var, Boolean>>(){
            public Entry<Var, Boolean> map(Var v){
                return Entry.create(v, Util.coinFlip());
            }
        }));
    }
}
