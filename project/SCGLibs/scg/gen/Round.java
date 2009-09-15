// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Round */
public class Round{
    protected final int num;
    protected final List<Event> trans;

    /** Construct a(n) Round Instance */
    public Round(int num, List<Event> trans){
        this.num = num;
        this.trans = trans;
    }
    /** Is the given object Equal to this Round? */
    public boolean equals(Object o){
        if(!(o instanceof Round))return false;
        if(o == this)return true;
        Round oo = (Round)o;
        return (((Object)num).equals(oo.num))&&(((Object)trans).equals(oo.trans));
    }
    /** Parse an instance of Round from the given String */
    public static Round parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Round();
    }
    /** Parse an instance of Round from the given Stream */
    public static Round parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Round();
    }
    /** Parse an instance of Round from the given Reader */
    public static Round parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Round();
    }

    /** Field Class for Round.num */
    public static class num extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Round.trans */
    public static class trans extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Round.trans */
    public List<Event> getTrans(){ return trans; }
    /** Getter for field Round.num */
    public int getNum(){ return num; }

}


