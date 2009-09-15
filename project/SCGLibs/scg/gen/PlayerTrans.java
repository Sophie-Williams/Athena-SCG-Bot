// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PlayerTrans */
public class PlayerTrans extends Event{
    protected final PlayerID id;
    protected final List<Transaction> ts;

    /** Construct a(n) PlayerTrans Instance */
    public PlayerTrans(PlayerID id, List<Transaction> ts){
        this.id = id;
        this.ts = ts;
    }
    /** Is the given object Equal to this PlayerTrans? */
    public boolean equals(Object o){
        if(!(o instanceof PlayerTrans))return false;
        if(o == this)return true;
        PlayerTrans oo = (PlayerTrans)o;
        return (((Object)id).equals(oo.id))&&(((Object)ts).equals(oo.ts));
    }
    /** Parse an instance of PlayerTrans from the given String */
    public static PlayerTrans parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PlayerTrans();
    }
    /** Parse an instance of PlayerTrans from the given Stream */
    public static PlayerTrans parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerTrans();
    }
    /** Parse an instance of PlayerTrans from the given Reader */
    public static PlayerTrans parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerTrans();
    }

    /** Field Class for PlayerTrans.id */
    public static class id extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerTrans.ts */
    public static class ts extends edu.neu.ccs.demeterf.control.Fields.any{}


    public void applyTransactions(GameI game, boolean isOverTime){
        final int playerID = getId().getId();
        for (Transaction trans : getTs()) {
            trans.install(playerID, game, isOverTime);
        }
    }

    public List<OfferTrans> getOfferTrans(){
        return (List)ts.filter(new List.Pred<Transaction>(){
            public boolean huh(Transaction t){
                return (t instanceof OfferTrans);
            }
        });
    }

    public List<AcceptTrans> getAcceptTrans(){
        return (List)ts.filter(new List.Pred<Transaction>(){
            public boolean huh(Transaction t){
                return (t instanceof AcceptTrans);
            }
        });
    }

    public List<ProvideTrans> getProvidedTrans(){
        return (List)ts.filter(new List.Pred<Transaction>(){
            public boolean huh(Transaction t){
                return (t instanceof ProvideTrans);
            }
        });
    }

    public List<SolveTrans> getSolvedTrans(){
        return (List)ts.filter(new List.Pred<Transaction>(){
            public boolean huh(Transaction t){
                return (t instanceof SolveTrans);
            }
        });
    }
    
    public List<ReofferTrans> getReofferTrans(){
        return (List)ts.filter(new List.Pred<Transaction>(){
            public boolean huh(Transaction t){
                return (t instanceof ReofferTrans);
            }
        });
    }

 
    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PlayerTrans.ts */
    public List<Transaction> getTs(){ return ts; }
    /** Getter for field PlayerTrans.id */
    public PlayerID getId(){ return id; }

}


