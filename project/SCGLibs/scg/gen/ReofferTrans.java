// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of ReofferTrans */
public class ReofferTrans extends Transaction{
    protected final int challengeid;
    protected final double price;

    /** Construct a(n) ReofferTrans Instance */
    public ReofferTrans(int challengeid, double price){
        this.challengeid = challengeid;
        this.price = price;
    }
    /** Is the given object Equal to this ReofferTrans? */
    public boolean equals(Object o){
        if(!(o instanceof ReofferTrans))return false;
        if(o == this)return true;
        ReofferTrans oo = (ReofferTrans)o;
        return (((Object)challengeid).equals(oo.challengeid))&&(((Object)price).equals(oo.price));
    }
    /** Parse an instance of ReofferTrans from the given String */
    public static ReofferTrans parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_ReofferTrans();
    }
    /** Parse an instance of ReofferTrans from the given Stream */
    public static ReofferTrans parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_ReofferTrans();
    }
    /** Parse an instance of ReofferTrans from the given Reader */
    public static ReofferTrans parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_ReofferTrans();
    }

    /** Field Class for ReofferTrans.challengeid */
    public static class challengeid extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for ReofferTrans.price */
    public static class price extends edu.neu.ccs.demeterf.control.Fields.any{}

   public void install(int playerId, GameI game, boolean isOverTime){
           if(!isOverTime){
            game.installTransaction(playerId, this);
        }
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field ReofferTrans.price */
    public double getPrice(){ return price; }
    /** Getter for field ReofferTrans.challengeid */
    public int getChallengeid(){ return challengeid; }

}


