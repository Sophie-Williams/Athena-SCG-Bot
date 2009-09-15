// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of SolvedChallenge */
public class SolvedChallenge extends Challenge{
    protected final PlayerID challengee;
    protected final Problem instance;
    protected final Solution solution;

    /** Construct a(n) SolvedChallenge Instance */
    public SolvedChallenge(PlayerID challengee, Problem instance, Solution solution, int key, PlayerID challenger, ProblemType pred, double price){
        super(key, challenger, pred, price);
        this.challengee = challengee;
        this.instance = instance;
        this.solution = solution;
    }
    /** Is the given object Equal to this SolvedChallenge? */
    public boolean equals(Object o){
        if(!(o instanceof SolvedChallenge))return false;
        if(o == this)return true;
        SolvedChallenge oo = (SolvedChallenge)o;
        return (((Object)challengee).equals(oo.challengee))&&(((Object)instance).equals(oo.instance))&&(((Object)solution).equals(oo.solution))&&(((Object)key).equals(oo.key))&&(((Object)challenger).equals(oo.challenger))&&(((Object)pred).equals(oo.pred))&&(((Object)price).equals(oo.price));
    }
    /** Parse an instance of SolvedChallenge from the given String */
    public static SolvedChallenge parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_SolvedChallenge();
    }
    /** Parse an instance of SolvedChallenge from the given Stream */
    public static SolvedChallenge parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_SolvedChallenge();
    }
    /** Parse an instance of SolvedChallenge from the given Reader */
    public static SolvedChallenge parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_SolvedChallenge();
    }

    /** Field Class for SolvedChallenge.challengee */
    public static class challengee extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for SolvedChallenge.instance */
    public static class instance extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for SolvedChallenge.solution */
    public static class solution extends edu.neu.ccs.demeterf.control.Fields.any{}
 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field SolvedChallenge.key */
    public int getKey(){ return key; }
    /** Getter for field SolvedChallenge.challenger */
    public PlayerID getChallenger(){ return challenger; }
    /** Getter for field SolvedChallenge.pred */
    public ProblemType getPred(){ return pred; }
    /** Getter for field SolvedChallenge.price */
    public double getPrice(){ return price; }
    /** Getter for field SolvedChallenge.solution */
    public Solution getSolution(){ return solution; }
    /** Getter for field SolvedChallenge.instance */
    public Problem getInstance(){ return instance; }
    /** Getter for field SolvedChallenge.challengee */
    public PlayerID getChallengee(){ return challengee; }

}


