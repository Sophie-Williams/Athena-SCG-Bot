// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of OfferedChallenge */
public class OfferedChallenge extends Challenge{

    /** Construct a(n) OfferedChallenge Instance */
    public OfferedChallenge(int key, PlayerID challenger, ProblemType pred, double price){
        super(key, challenger, pred, price);
    }
    /** Is the given object Equal to this OfferedChallenge? */
    public boolean equals(Object o){
        if(!(o instanceof OfferedChallenge))return false;
        if(o == this)return true;
        OfferedChallenge oo = (OfferedChallenge)o;
        return (((Object)key).equals(oo.key))&&(((Object)challenger).equals(oo.challenger))&&(((Object)pred).equals(oo.pred))&&(((Object)price).equals(oo.price));
    }
    /** Parse an instance of OfferedChallenge from the given String */
    public static OfferedChallenge parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_OfferedChallenge();
    }
    /** Parse an instance of OfferedChallenge from the given Stream */
    public static OfferedChallenge parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_OfferedChallenge();
    }
    /** Parse an instance of OfferedChallenge from the given Reader */
    public static OfferedChallenge parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_OfferedChallenge();
    }


    public AcceptedChallenge accept(PlayerID challengee){
        return new AcceptedChallenge(challengee, this.key, this.challenger,
                this.pred, this.price);
    }
    
    public OfferedChallenge reoffer(PlayerID challenger, double newPrice){
        return new OfferedChallenge(key, challenger, pred, newPrice);
    }
    
 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field OfferedChallenge.key */
    public int getKey(){ return key; }
    /** Getter for field OfferedChallenge.challenger */
    public PlayerID getChallenger(){ return challenger; }
    /** Getter for field OfferedChallenge.pred */
    public ProblemType getPred(){ return pred; }
    /** Getter for field OfferedChallenge.price */
    public double getPrice(){ return price; }

}


