// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of OfferTrans */
public class OfferTrans extends Transaction{
    protected final ProblemType pred;
    protected final double price;

    /** Construct a(n) OfferTrans Instance */
    public OfferTrans(ProblemType pred, double price){
        this.pred = pred;
        this.price = price;
    }
    /** Is the given object Equal to this OfferTrans? */
    public boolean equals(Object o){
        if(!(o instanceof OfferTrans))return false;
        if(o == this)return true;
        OfferTrans oo = (OfferTrans)o;
        return (((Object)pred).equals(oo.pred))&&(((Object)price).equals(oo.price));
    }
    /** Parse an instance of OfferTrans from the given String */
    public static OfferTrans parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_OfferTrans();
    }
    /** Parse an instance of OfferTrans from the given Stream */
    public static OfferTrans parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_OfferTrans();
    }
    /** Parse an instance of OfferTrans from the given Reader */
    public static OfferTrans parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_OfferTrans();
    }

    /** Field Class for OfferTrans.pred */
    public static class pred extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for OfferTrans.price */
    public static class price extends edu.neu.ccs.demeterf.control.Fields.any{}

   public void install(int playerId, GameI game, boolean isOverTime){
       if(!isOverTime){
           game.installTransaction(playerId, this);
       }
   }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field OfferTrans.price */
    public double getPrice(){ return price; }
    /** Getter for field OfferTrans.pred */
    public ProblemType getPred(){ return pred; }

}


