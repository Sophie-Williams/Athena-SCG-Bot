// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.evergreen.ir.RelationCore;




/** Representation of ProblemType */
public class ProblemType{
    protected final List<Integer> type;

    /** Construct a(n) ProblemType Instance */
    public ProblemType(List<Integer> type){
        this.type = type;
    }
    /** Is the given object Equal to this ProblemType? */
    public boolean equals(Object o){
        if(!(o instanceof ProblemType))return false;
        if(o == this)return true;
        ProblemType oo = (ProblemType)o;
        return (((Object)type).equals(oo.type));
    }
    /** Parse an instance of ProblemType from the given String */
    public static ProblemType parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_ProblemType();
    }
    /** Parse an instance of ProblemType from the given Stream */
    public static ProblemType parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_ProblemType();
    }
    /** Parse an instance of ProblemType from the given Reader */
    public static ProblemType parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_ProblemType();
    }

    /** Field Class for ProblemType.type */
    public static class type extends edu.neu.ccs.demeterf.control.Fields.any{}

        /** Return a random ProblemType (single relation number) */
        public static ProblemType random(){
            return new ProblemType(randomList(1));
        }
        /** Return a random List of integers in [1..255] */
        public static List<Integer> randomList(int len){
            if(len <= 0)return List.create();
            return randomList(len-1).push(Util.random(254+1));
        }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field ProblemType.type */
    public List<Integer> getType(){ return type; }

}


