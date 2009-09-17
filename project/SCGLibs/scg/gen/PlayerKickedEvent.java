// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PlayerKickedEvent */
public class PlayerKickedEvent extends Event{
    protected final PlayerSpec spec;
    protected final String msg;
    protected final String time;

    /** Construct a(n) PlayerKickedEvent Instance */
    public PlayerKickedEvent(PlayerSpec spec, String msg, String time){
        this.spec = spec;
        this.msg = msg;
        this.time = time;
    }
    /** Is the given object Equal to this PlayerKickedEvent? */
    public boolean equals(Object o){
        if(!(o instanceof PlayerKickedEvent))return false;
        if(o == this)return true;
        PlayerKickedEvent oo = (PlayerKickedEvent)o;
        return (((Object)spec).equals(oo.spec))&&(((Object)msg).equals(oo.msg))&&(((Object)time).equals(oo.time));
    }
    /** Parse an instance of PlayerKickedEvent from the given String */
    public static PlayerKickedEvent parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PlayerKickedEvent();
    }
    /** Parse an instance of PlayerKickedEvent from the given Stream */
    public static PlayerKickedEvent parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerKickedEvent();
    }
    /** Parse an instance of PlayerKickedEvent from the given Reader */
    public static PlayerKickedEvent parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerKickedEvent();
    }

    /** Field Class for PlayerKickedEvent.spec */
    public static class spec extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerKickedEvent.msg */
    public static class msg extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerKickedEvent.time */
    public static class time extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PlayerKickedEvent.time */
    public String getTime(){ return time; }
    /** Getter for field PlayerKickedEvent.msg */
    public String getMsg(){ return msg; }
    /** Getter for field PlayerKickedEvent.spec */
    public PlayerSpec getSpec(){ return spec; }

}


