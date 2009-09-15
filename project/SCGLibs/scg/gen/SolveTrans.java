// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of SolveTrans */
public class SolveTrans extends Transaction{
    protected final Solution sol;
    protected final int challengeid;

    /** Construct a(n) SolveTrans Instance */
    public SolveTrans(Solution sol, int challengeid){
        this.sol = sol;
        this.challengeid = challengeid;
    }
    /** Is the given object Equal to this SolveTrans? */
    public boolean equals(Object o){
        if(!(o instanceof SolveTrans))return false;
        if(o == this)return true;
        SolveTrans oo = (SolveTrans)o;
        return (((Object)sol).equals(oo.sol))&&(((Object)challengeid).equals(oo.challengeid));
    }
    /** Parse an instance of SolveTrans from the given String */
    public static SolveTrans parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_SolveTrans();
    }
    /** Parse an instance of SolveTrans from the given Stream */
    public static SolveTrans parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_SolveTrans();
    }
    /** Parse an instance of SolveTrans from the given Reader */
    public static SolveTrans parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_SolveTrans();
    }

    /** Field Class for SolveTrans.sol */
    public static class sol extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for SolveTrans.challengeid */
    public static class challengeid extends edu.neu.ccs.demeterf.control.Fields.any{}
 
   public void install(int playerId, GameI game, boolean isOverTime){
        game.installTransaction(playerId, this);
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field SolveTrans.challengeid */
    public int getChallengeid(){ return challengeid; }
    /** Getter for field SolveTrans.sol */
    public Solution getSol(){ return sol; }

}


