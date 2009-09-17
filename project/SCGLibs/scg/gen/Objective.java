// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.evergreen.ir.RelationCore;




/** Representation of Objective */
public class Objective{

    /** Construct a(n) Objective Instance */
    public Objective(){
    }
    /** Is the given object Equal to this Objective? */
    public boolean equals(Object o){
        if(!(o instanceof Objective))return false;
        if(o == this)return true;
        Objective oo = (Objective)o;
        return true;
    }
    /** Parse an instance of Objective from the given String */
    public static Objective parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Objective();
    }
    /** Parse an instance of Objective from the given Stream */
    public static Objective parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Objective();
    }
    /** Parse an instance of Objective from the given Reader */
    public static Objective parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Objective();
    }


    public double calculatePayoff(double quality, double secretQuality, double price, double profitFactor){
        double win = quality - price * secretQuality;
        if (win >= 0) {
            return price + win * profitFactor;
        } else {
            return 0;
        }
    }

    public double value(Problem inst, Solution sol){
        return scg.hidden.CSP.value(inst,sol);
    }
    
    public double getDefaultQuality(Problem inst){
        return scg.hidden.CSP.getDefaultQuality(inst);
    }

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }

}


