// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of ProvideTrans */
public class ProvideTrans extends Transaction{
    protected final Problem inst;
    protected final int challengeid;

    /** Construct a(n) ProvideTrans Instance */
    public ProvideTrans(Problem inst, int challengeid){
        this.inst = inst;
        this.challengeid = challengeid;
    }
    /** Is the given object Equal to this ProvideTrans? */
    public boolean equals(Object o){
        if(!(o instanceof ProvideTrans))return false;
        if(o == this)return true;
        ProvideTrans oo = (ProvideTrans)o;
        return (((Object)inst).equals(oo.inst))&&(((Object)challengeid).equals(oo.challengeid));
    }
    /** Parse an instance of ProvideTrans from the given String */
    public static ProvideTrans parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_ProvideTrans();
    }
    /** Parse an instance of ProvideTrans from the given Stream */
    public static ProvideTrans parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_ProvideTrans();
    }
    /** Parse an instance of ProvideTrans from the given Reader */
    public static ProvideTrans parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_ProvideTrans();
    }

    /** Field Class for ProvideTrans.inst */
    public static class inst extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for ProvideTrans.challengeid */
    public static class challengeid extends edu.neu.ccs.demeterf.control.Fields.any{}
 
   public void install(int playerId, GameI game, boolean isOverTime){
        game.installTransaction(playerId, this);
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field ProvideTrans.challengeid */
    public int getChallengeid(){ return challengeid; }
    /** Getter for field ProvideTrans.inst */
    public Problem getInst(){ return inst; }

}


