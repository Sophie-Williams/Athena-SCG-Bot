// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of ProvidedChallenge */
public class ProvidedChallenge extends Challenge{
    protected final PlayerID challengee;
    protected final Problem instance;

    /** Construct a(n) ProvidedChallenge Instance */
    public ProvidedChallenge(PlayerID challengee, Problem instance, int key, PlayerID challenger, ProblemType pred, double price){
        super(key, challenger, pred, price);
        this.challengee = challengee;
        this.instance = instance;
    }
    /** Is the given object Equal to this ProvidedChallenge? */
    public boolean equals(Object o){
        if(!(o instanceof ProvidedChallenge))return false;
        if(o == this)return true;
        ProvidedChallenge oo = (ProvidedChallenge)o;
        return (((Object)challengee).equals(oo.challengee))&&(((Object)instance).equals(oo.instance))&&(((Object)key).equals(oo.key))&&(((Object)challenger).equals(oo.challenger))&&(((Object)pred).equals(oo.pred))&&(((Object)price).equals(oo.price));
    }
    /** Parse an instance of ProvidedChallenge from the given String */
    public static ProvidedChallenge parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_ProvidedChallenge();
    }
    /** Parse an instance of ProvidedChallenge from the given Stream */
    public static ProvidedChallenge parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_ProvidedChallenge();
    }
    /** Parse an instance of ProvidedChallenge from the given Reader */
    public static ProvidedChallenge parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_ProvidedChallenge();
    }

    /** Field Class for ProvidedChallenge.challengee */
    public static class challengee extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for ProvidedChallenge.instance */
    public static class instance extends edu.neu.ccs.demeterf.control.Fields.any{}
 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field ProvidedChallenge.key */
    public int getKey(){ return key; }
    /** Getter for field ProvidedChallenge.challenger */
    public PlayerID getChallenger(){ return challenger; }
    /** Getter for field ProvidedChallenge.pred */
    public ProblemType getPred(){ return pred; }
    /** Getter for field ProvidedChallenge.price */
    public double getPrice(){ return price; }
    /** Getter for field ProvidedChallenge.instance */
    public Problem getInstance(){ return instance; }
    /** Getter for field ProvidedChallenge.challengee */
    public PlayerID getChallengee(){ return challengee; }

}


