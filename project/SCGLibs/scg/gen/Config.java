// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;




/** Representation of Config */
public class Config{
    protected final String gamekind;
    protected final int turndur;
    protected final double mindecr;
    protected final double initacc;
    protected final Objective objective;
    protected final Predicate predicate;
    protected final int numrounds;
    protected final double profitFactor;
    protected final int otrounds;

    /** Construct a(n) Config Instance */
    public Config(String gamekind, int turndur, double mindecr, double initacc, Objective objective, Predicate predicate, int numrounds, double profitFactor, int otrounds){
        this.gamekind = gamekind;
        this.turndur = turndur;
        this.mindecr = mindecr;
        this.initacc = initacc;
        this.objective = objective;
        this.predicate = predicate;
        this.numrounds = numrounds;
        this.profitFactor = profitFactor;
        this.otrounds = otrounds;
    }
    /** Is the given object Equal to this Config? */
    public boolean equals(Object o){
        if(!(o instanceof Config))return false;
        if(o == this)return true;
        Config oo = (Config)o;
        return (((Object)gamekind).equals(oo.gamekind))&&(((Object)turndur).equals(oo.turndur))&&(((Object)mindecr).equals(oo.mindecr))&&(((Object)initacc).equals(oo.initacc))&&(((Object)objective).equals(oo.objective))&&(((Object)predicate).equals(oo.predicate))&&(((Object)numrounds).equals(oo.numrounds))&&(((Object)profitFactor).equals(oo.profitFactor))&&(((Object)otrounds).equals(oo.otrounds));
    }
    /** Parse an instance of Config from the given String */
    public static Config parse(String inpt) throws ParseException{
        return new TheParser(new java.io.StringReader(inpt)).parse_Config();
    }
    /** Parse an instance of Config from the given Stream */
    public static Config parse(java.io.InputStream inpt) throws ParseException{
        return new TheParser(inpt).parse_Config();
    }
    /** Parse an instance of Config from the given Reader */
    public static Config parse(java.io.Reader inpt) throws ParseException{
        return new TheParser(inpt).parse_Config();
    }

    /** Field Class for Config.gamekind */
    public static class gamekind extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.turndur */
    public static class turndur extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.mindecr */
    public static class mindecr extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.initacc */
    public static class initacc extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.objective */
    public static class objective extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.predicate */
    public static class predicate extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.numrounds */
    public static class numrounds extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.profitFactor */
    public static class profitFactor extends edu.neu.ccs.demeterf.control.Fields.any{}
    /** Field Class for Config.otrounds */
    public static class otrounds extends edu.neu.ccs.demeterf.control.Fields.any{}

    private final int START_PLAYER_ID = 100;
    private final int START_CHALLENGE_ID = 500;
    
    private int currentChallengeID = START_CHALLENGE_ID;
    private int currentPlayerID = START_PLAYER_ID;
    
    public int createPlayerID(){
        return currentPlayerID++;
    }
    
    public int createChallengeID(){
        return currentChallengeID++;
    }
    
    public boolean isOverTime(int round){
      return round > getNumrounds();
    }
    
    /** Get a general comparator that can compare Challenges to Transactions */
    static <CH extends Challenge, TR extends Transaction> edu.neu.ccs.demeterf.lib.List.GComp<CH, TR> getComp(){
        return new edu.neu.ccs.demeterf.lib.List.GComp<CH, TR>() {

            @Override
            public boolean comp(CH ch, TR t){
                return t.getChallengeid() == ch.getKey();
            }
        };
    }
    

    /** DGP method from Class PrintHeapToString */
    public  String toString(){ return scg.gen.PrintHeapToString.PrintHeapToStringM(this); }
    /** Getter for field Config.otrounds */
    public int getOtrounds(){ return otrounds; }
    /** Getter for field Config.profitFactor */
    public double getProfitFactor(){ return profitFactor; }
    /** Getter for field Config.numrounds */
    public int getNumrounds(){ return numrounds; }
    /** Getter for field Config.predicate */
    public Predicate getPredicate(){ return predicate; }
    /** Getter for field Config.objective */
    public Objective getObjective(){ return objective; }
    /** Getter for field Config.initacc */
    public double getInitacc(){ return initacc; }
    /** Getter for field Config.mindecr */
    public double getMindecr(){ return mindecr; }
    /** Getter for field Config.turndur */
    public int getTurndur(){ return turndur; }
    /** Getter for field Config.gamekind */
    public String getGamekind(){ return gamekind; }

}


