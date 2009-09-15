// ** This class was generated with DemFGen (vers:09/12/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of TeamFile */
public class TeamFile{
    protected final List<TeamSpec> teams;

    /** Construct a(n) TeamFile Instance */
    public TeamFile(List<TeamSpec> teams){
        this.teams = teams;
    }
    /** Is the given object Equal to this TeamFile? */
    public boolean equals(Object o){
        if(!(o instanceof TeamFile))return false;
        if(o == this)return true;
        TeamFile oo = (TeamFile)o;
        return (((Object)teams).equals(oo.teams));
    }
    /** Parse an instance of TeamFile from the given String */
    public static TeamFile parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_TeamFile();
    }
    /** Parse an instance of TeamFile from the given Stream */
    public static TeamFile parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_TeamFile();
    }
    /** Parse an instance of TeamFile from the given Reader */
    public static TeamFile parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_TeamFile();
    }

    /** Field Class for TeamFile.teams */
    public static class teams extends edu.neu.ccs.demeterf.control.Fields.any{}

    /** Write out the Teams to a File */
    public void write(String file) throws IOException{
        PrintStream out = new PrintStream(new FileOutputStream(file));
        out.println(this.toString());
        out.close();
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field TeamFile.teams */
    public List<TeamSpec> getTeams(){ return teams; }

}


