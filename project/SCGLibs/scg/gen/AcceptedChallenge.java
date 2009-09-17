// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of AcceptedChallenge */
public class AcceptedChallenge extends Challenge{
    protected final PlayerID challengee;

    /** Construct a(n) AcceptedChallenge Instance */
    public AcceptedChallenge(PlayerID challengee, int key, PlayerID challenger, ProblemType pred, double price){
        super(key, challenger, pred, price);
        this.challengee = challengee;
    }
    /** Is the given object Equal to this AcceptedChallenge? */
    public boolean equals(Object o){
        if(!(o instanceof AcceptedChallenge))return false;
        if(o == this)return true;
        AcceptedChallenge oo = (AcceptedChallenge)o;
        return (((Object)challengee).equals(oo.challengee))&&(((Object)key).equals(oo.key))&&(((Object)challenger).equals(oo.challenger))&&(((Object)pred).equals(oo.pred))&&(((Object)price).equals(oo.price));
    }
    /** Parse an instance of AcceptedChallenge from the given String */
    public static AcceptedChallenge parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_AcceptedChallenge();
    }
    /** Parse an instance of AcceptedChallenge from the given Stream */
    public static AcceptedChallenge parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_AcceptedChallenge();
    }
    /** Parse an instance of AcceptedChallenge from the given Reader */
    public static AcceptedChallenge parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_AcceptedChallenge();
    }

    /** Field Class for AcceptedChallenge.challengee */
    public static class challengee extends edu.neu.ccs.demeterf.control.Fields.any{}

    public ProvidedChallenge provide(Problem problem){
        return new ProvidedChallenge(challengee, problem, key, challenger,
                pred, price);
    }
 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field AcceptedChallenge.key */
    public int getKey(){ return key; }
    /** Getter for field AcceptedChallenge.challenger */
    public PlayerID getChallenger(){ return challenger; }
    /** Getter for field AcceptedChallenge.pred */
    public ProblemType getPred(){ return pred; }
    /** Getter for field AcceptedChallenge.price */
    public double getPrice(){ return price; }
    /** Getter for field AcceptedChallenge.challengee */
    public PlayerID getChallengee(){ return challengee; }

}


