// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PasswordEntry */
public class PasswordEntry{
    protected final String name;
    protected final String passhash;

    /** Construct a(n) PasswordEntry Instance */
    public PasswordEntry(String name, String passhash){
        this.name = name;
        this.passhash = passhash;
    }
    /** Is the given object Equal to this PasswordEntry? */
    public boolean equals(Object o){
        if(!(o instanceof PasswordEntry))return false;
        if(o == this)return true;
        PasswordEntry oo = (PasswordEntry)o;
        return (((Object)name).equals(oo.name))&&(((Object)passhash).equals(oo.passhash));
    }
    /** Parse an instance of PasswordEntry from the given String */
    public static PasswordEntry parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PasswordEntry();
    }
    /** Parse an instance of PasswordEntry from the given Stream */
    public static PasswordEntry parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PasswordEntry();
    }
    /** Parse an instance of PasswordEntry from the given Reader */
    public static PasswordEntry parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PasswordEntry();
    }

    /** Field Class for PasswordEntry.name */
    public static class name extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for PasswordEntry.passhash */
    public static class passhash extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** Add to the given Map */
    public Map<String,String> addTo(Map<String,String> m){
        return m.put(name,passhash);
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PasswordEntry.passhash */
    public String getPasshash(){ return passhash; }
    /** Getter for field PasswordEntry.name */
    public String getName(){ return name; }

}


