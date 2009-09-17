// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PlayersFile */
public class PlayersFile{
    protected final List<PlayerSpec> players;

    /** Construct a(n) PlayersFile Instance */
    public PlayersFile(List<PlayerSpec> players){
        this.players = players;
    }
    /** Is the given object Equal to this PlayersFile? */
    public boolean equals(Object o){
        if(!(o instanceof PlayersFile))return false;
        if(o == this)return true;
        PlayersFile oo = (PlayersFile)o;
        return (((Object)players).equals(oo.players));
    }
    /** Parse an instance of PlayersFile from the given String */
    public static PlayersFile parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PlayersFile();
    }
    /** Parse an instance of PlayersFile from the given Stream */
    public static PlayersFile parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayersFile();
    }
    /** Parse an instance of PlayersFile from the given Reader */
    public static PlayersFile parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayersFile();
    }

    /** Field Class for PlayersFile.players */
    public static class players extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** Get the List of PlayerSpecs */
    public List<PlayerSpec> getPlayerSpecs(){ return players; }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PlayersFile.players */
    public List<PlayerSpec> getPlayers(){ return players; }

}


