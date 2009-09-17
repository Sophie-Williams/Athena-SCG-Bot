// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PlayerSpec */
public class PlayerSpec{
    protected final String name;
    protected final String address;
    protected final int port;

    /** Construct a(n) PlayerSpec Instance */
    public PlayerSpec(String name, String address, int port){
        this.name = name;
        this.address = address;
        this.port = port;
    }
    /** Is the given object Equal to this PlayerSpec? */
    public boolean equals(Object o){
        if(!(o instanceof PlayerSpec))return false;
        if(o == this)return true;
        PlayerSpec oo = (PlayerSpec)o;
        return (((Object)name).equals(oo.name))&&(((Object)address).equals(oo.address))&&(((Object)port).equals(oo.port));
    }
    /** Parse an instance of PlayerSpec from the given String */
    public static PlayerSpec parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PlayerSpec();
    }
    /** Parse an instance of PlayerSpec from the given Stream */
    public static PlayerSpec parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerSpec();
    }
    /** Parse an instance of PlayerSpec from the given Reader */
    public static PlayerSpec parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PlayerSpec();
    }

    /** Field Class for PlayerSpec.name */
    public static class name extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerSpec.address */
    public static class address extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PlayerSpec.port */
    public static class port extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** Create a PlayerSpec */
    public PlayerSpec create(String n, String add, int p){
        return new PlayerSpec(n,add,p);
    }

    /** Change the Address of this Player */
    public PlayerSpec changeAddress(String add){ return create(name,add,port); }
    
    
    /** Returns a Predicate that compares another PlayerSpec by name */
    public List.Pred<PlayerSpec> sameNamePred(){ return new SameName(); }
    /** Predicate for PlayerSpecs that compares Names */
    private class SameName extends List.Pred<PlayerSpec>{
        public boolean huh(PlayerSpec that)
        { return name.equals(that.name); }
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PlayerSpec.port */
    public int getPort(){ return port; }
    /** Getter for field PlayerSpec.address */
    public String getAddress(){ return address; }
    /** Getter for field PlayerSpec.name */
    public String getName(){ return name; }

}


