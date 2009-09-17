// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Transaction */
public abstract class Transaction{

    /** Construct a(n) Transaction Instance */
    public Transaction(){
    }
    /** Parse an instance of Transaction from the given String */
    public static Transaction parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Transaction();
    }
    /** Parse an instance of Transaction from the given Stream */
    public static Transaction parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Transaction();
    }
    /** Parse an instance of Transaction from the given Reader */
    public static Transaction parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Transaction();
    }


   public int getChallengeid(){ throw new RuntimeException("No Challenge ID"); }
   public abstract void install(int playerId, GameI game, boolean isOverTime);

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }

}


