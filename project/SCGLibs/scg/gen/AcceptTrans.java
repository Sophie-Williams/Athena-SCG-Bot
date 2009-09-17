// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of AcceptTrans */
public class AcceptTrans extends Transaction{
    protected final int challengeid;

    /** Construct a(n) AcceptTrans Instance */
    public AcceptTrans(int challengeid){
        this.challengeid = challengeid;
    }
    /** Is the given object Equal to this AcceptTrans? */
    public boolean equals(Object o){
        if(!(o instanceof AcceptTrans))return false;
        if(o == this)return true;
        AcceptTrans oo = (AcceptTrans)o;
        return (((Object)challengeid).equals(oo.challengeid));
    }
    /** Parse an instance of AcceptTrans from the given String */
    public static AcceptTrans parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_AcceptTrans();
    }
    /** Parse an instance of AcceptTrans from the given Stream */
    public static AcceptTrans parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_AcceptTrans();
    }
    /** Parse an instance of AcceptTrans from the given Reader */
    public static AcceptTrans parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_AcceptTrans();
    }

    /** Field Class for AcceptTrans.challengeid */
    public static class challengeid extends edu.neu.ccs.demeterf.control.Fields.any{}
 
   public void install(int playerId, GameI game, boolean isOverTime){
           if(!isOverTime){
            game.installTransaction(playerId, this);
        }
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field AcceptTrans.challengeid */
    public int getChallengeid(){ return challengeid; }

}


