// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Event */
public abstract class Event{

    /** Construct a(n) Event Instance */
    public Event(){
    }
    /** Parse an instance of Event from the given String */
    public static Event parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Event();
    }
    /** Parse an instance of Event from the given Stream */
    public static Event parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Event();
    }
    /** Parse an instance of Event from the given Reader */
    public static Event parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Event();
    }


    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }

}


