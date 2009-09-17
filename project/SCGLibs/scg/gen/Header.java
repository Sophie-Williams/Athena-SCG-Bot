// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Header */
public class Header{
    protected final Map<PlayerID, PlayerSpec> players;

    /** Construct a(n) Header Instance */
    public Header(Map<PlayerID, PlayerSpec> players){
        this.players = players;
    }
    /** Is the given object Equal to this Header? */
    public boolean equals(Object o){
        if(!(o instanceof Header))return false;
        if(o == this)return true;
        Header oo = (Header)o;
        return (((Object)players).equals(oo.players));
    }
    /** Parse an instance of Header from the given String */
    public static Header parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Header();
    }
    /** Parse an instance of Header from the given Stream */
    public static Header parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Header();
    }
    /** Parse an instance of Header from the given Reader */
    public static Header parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Header();
    }

    /** Field Class for Header.players */
    public static class players extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Header.players */
    public Map<PlayerID,PlayerSpec> getPlayers(){ return players; }

}


