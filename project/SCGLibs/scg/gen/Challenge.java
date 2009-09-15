// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Challenge */
public abstract class Challenge{
    protected final int key;
    protected final PlayerID challenger;
    protected final ProblemType pred;
    protected final double price;

    /** Construct a(n) Challenge Instance */
    public Challenge(int key, PlayerID challenger, ProblemType pred, double price){
        this.key = key;
        this.challenger = challenger;
        this.pred = pred;
        this.price = price;
    }
    /** Parse an instance of Challenge from the given String */
    public static Challenge parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Challenge();
    }
    /** Parse an instance of Challenge from the given Stream */
    public static Challenge parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Challenge();
    }
    /** Parse an instance of Challenge from the given Reader */
    public static Challenge parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Challenge();
    }

    /** Field Class for Challenge.key */
    public static class key extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Challenge.challenger */
    public static class challenger extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Challenge.pred */
    public static class pred extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Challenge.price */
    public static class price extends edu.neu.ccs.demeterf.control.Fields.any{}
 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Challenge.price */
    public abstract double getPrice();    /** Getter for field Challenge.pred */
    public abstract ProblemType getPred();    /** Getter for field Challenge.challenger */
    public abstract PlayerID getChallenger();    /** Getter for field Challenge.key */
    public abstract int getKey();
}


