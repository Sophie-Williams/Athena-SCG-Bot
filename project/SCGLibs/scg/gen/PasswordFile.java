// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of PasswordFile */
public class PasswordFile{
    protected final List<PasswordEntry> passwds;

    /** Construct a(n) PasswordFile Instance */
    public PasswordFile(List<PasswordEntry> passwds){
        this.passwds = passwds;
    }
    /** Is the given object Equal to this PasswordFile? */
    public boolean equals(Object o){
        if(!(o instanceof PasswordFile))return false;
        if(o == this)return true;
        PasswordFile oo = (PasswordFile)o;
        return (((Object)passwds).equals(oo.passwds));
    }
    /** Parse an instance of PasswordFile from the given String */
    public static PasswordFile parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_PasswordFile();
    }
    /** Parse an instance of PasswordFile from the given Stream */
    public static PasswordFile parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_PasswordFile();
    }
    /** Parse an instance of PasswordFile from the given Reader */
    public static PasswordFile parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_PasswordFile();
    }

    /** Field Class for PasswordFile.passwds */
    public static class passwds extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** Get a Map of Player-Name / Password-Hash */
    public Map<String,String> getPasswordMap(){
        return passwds.fold(new List.Fold<PasswordEntry,Map<String,String>>(){
            public Map<String,String> fold(PasswordEntry e, Map<String,String> m){
                return e.addTo(m);
            }
        }, Map.<String,String>create());
    }
    /** Write out the Passwords to a File */
    public void write(String file) throws IOException{
        PrintStream out = new PrintStream(new FileOutputStream(file));
        out.println(this.toString());
        out.close();
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field PasswordFile.passwds */
    public List<PasswordEntry> getPasswds(){ return passwds; }

}


