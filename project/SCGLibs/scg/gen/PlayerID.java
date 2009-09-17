// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PlayerID */
public class PlayerID implements Comparable<PlayerID>{
    protected final int id;

    /** Construct a(n) PlayerID Instance */
    public PlayerID(int id){
        this.id = id;
    }
    /** Is the given object Equal to this PlayerID? */
    public boolean equals(Object o){
        if(!(o instanceof PlayerID))return false;
        if(o == this)return true;
        PlayerID oo = (PlayerID)o;
        return (((Object)id).equals(oo.id));
    }
    /** Parse an instance of PlayerID from the given String */
    public static PlayerID parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PlayerID();
    }
    /** Parse an instance of PlayerID from the given Stream */
    public static PlayerID parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerID();
    }
    /** Parse an instance of PlayerID from the given Reader */
    public static PlayerID parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerID();
    }

    /** Field Class for PlayerID.id */
    public static class id extends edu.neu.ccs.demeterf.control.Fields.any{}

        public int compareTo(PlayerID pid)
        { return ((Integer)id).compareTo(pid.id); }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PlayerID.id */
    public int getId(){ return id; }

}


