// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of History */
public class History{
    protected final Header header;
    protected final List<Round> rounds;

    /** Construct a(n) History Instance */
    public History(Header header, List<Round> rounds){
        this.header = header;
        this.rounds = rounds;
    }
    /** Is the given object Equal to this History? */
    public boolean equals(Object o){
        if(!(o instanceof History))return false;
        if(o == this)return true;
        History oo = (History)o;
        return (((Object)header).equals(oo.header))&&(((Object)rounds).equals(oo.rounds));
    }
    /** Parse an instance of History from the given String */
    public static History parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_History();
    }
    /** Parse an instance of History from the given Stream */
    public static History parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_History();
    }
    /** Parse an instance of History from the given Reader */
    public static History parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_History();
    }

    /** Field Class for History.header */
    public static class header extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for History.rounds */
    public static class rounds extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field History.rounds */
    public List<Round> getRounds(){ return rounds; }
    /** Getter for field History.header */
    public Header getHeader(){ return header; }

}


