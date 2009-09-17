// ** This class was generated with DemFGen (vers:09/15/2009)

package scg.gen;

import edu.neu.ccs.demeterf.control.Fields;
import edu.neu.ccs.demeterf.lib.ident;
import edu.neu.ccs.demeterf.lib.verbatim;
import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import java.io.*;
import edu.neu.ccs.demeterf.lib.*;
import scg.Util;
import scg.game.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.evergreen.ir.RelationCore;



/** Computes a String representation in CD Syntax, named as a ToString method */
public class PrintHeapToString extends edu.neu.ccs.demeterf.ID{
   /** Static stub method for calling toString */
   public static String PrintHeapToStringM(Object o){
      return new edu.neu.ccs.demeterf.stackless.HeapTrav(new PrintHeapToString(),edu.neu.ccs.demeterf.Control.builtins()).<StringBuffer>traverse(o).toString();
   }

   public StringBuffer combine(byte _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(short _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(int _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(long _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(float _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(double _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(char _h_){ return new StringBuffer("\'"+escape(""+_h_)+"\'"); }
   public StringBuffer combine(boolean _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(String _h_){ return new StringBuffer("\""+escape(""+_h_)+"\""); }
   public StringBuffer combine(ident _h_){ return new StringBuffer(""+_h_); }
   public StringBuffer combine(verbatim _h_){ return new StringBuffer(""+_h_); }

   public StringBuffer combine(Problem _h_, StringBuffer vars, StringBuffer clauses){
      return new StringBuffer("").append(vars).append(" ").append(clauses);
   }
   public StringBuffer combine(Clause _h_, StringBuffer relnum, StringBuffer vars){
      return new StringBuffer("").append("(").append(relnum).append(" ").append(vars).append(")");
   }
   public StringBuffer combine(ProblemType _h_, StringBuffer type){
      return new StringBuffer("").append("(").append(type).append(")");
   }
   public StringBuffer combine(Objective _h_){
      return new StringBuffer("").append("[]");
   }
   public StringBuffer combine(Predicate _h_){
      return new StringBuffer("").append("[]");
   }
   public StringBuffer combine(List _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(Cons _h_, StringBuffer first, StringBuffer rest){
      return new StringBuffer("").append(first).append(" ").append(rest);
   }
   public StringBuffer combine(Empty _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(Option _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(Some _h_, StringBuffer just){
      return new StringBuffer("").append(just);
   }
   public StringBuffer combine(None _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(RBColor _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(RED _h_){
      return new StringBuffer("").append("red");
   }
   public StringBuffer combine(BLACK _h_){
      return new StringBuffer("").append("black");
   }
   public StringBuffer combine(RBTree _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(RBLeaf _h_){
      return new StringBuffer("");
   }
   public StringBuffer combine(RBNode _h_, StringBuffer color, StringBuffer data, StringBuffer left, StringBuffer right){
      return new StringBuffer("").append("(node ").append(color).append(" ").append(data).append(" ").append(left).append(" ").append(right).append(")");
   }
   public StringBuffer combine(Entry _h_, StringBuffer key, StringBuffer val){
      return new StringBuffer("").append("(").append(key).append(" -> ").append(val).append(")");
   }
   public StringBuffer combine(Map _h_, StringBuffer tree){
      return new StringBuffer("").append("[ ").append(tree).append(" ]");
   }
   public StringBuffer combine(Wrap _h_, StringBuffer x){
      return new StringBuffer("").append(x);
   }
   public StringBuffer combine(Set _h_, StringBuffer tree){
      return new StringBuffer("").append("{ ").append(tree).append(" }");
   }
   public StringBuffer combine(PlayerID _h_, StringBuffer id){
      return new StringBuffer("").append(id);
   }
   public StringBuffer combine(Var _h_, StringBuffer id){
      return new StringBuffer("").append(id);
   }
   public StringBuffer combine(Solution _h_, StringBuffer assign){
      return new StringBuffer("").append(assign);
   }
   public StringBuffer combine(Challenge _h_, StringBuffer key, StringBuffer challenger, StringBuffer pred, StringBuffer price){
      return new StringBuffer("").append(" ").append(key).append(" ").append(challenger).append(" ").append(pred).append(" ").append(price).append("]");
   }
   public StringBuffer combine(OfferedChallenge _h_, StringBuffer key, StringBuffer challenger, StringBuffer pred, StringBuffer price){
      return new StringBuffer("").append("offered[ ").append(key).append(" ").append(challenger).append(" ").append(pred).append(" ").append(price).append("]");
   }
   public StringBuffer combine(AcceptedChallenge _h_, StringBuffer challengee, StringBuffer key, StringBuffer challenger, StringBuffer pred, StringBuffer price){
      return new StringBuffer("").append("accepted[").append(challengee).append(" ").append(key).append(" ").append(challenger).append(" ").append(pred).append(" ").append(price).append("]");
   }
   public StringBuffer combine(ProvidedChallenge _h_, StringBuffer challengee, StringBuffer instance, StringBuffer key, StringBuffer challenger, StringBuffer pred, StringBuffer price){
      return new StringBuffer("").append("provided[").append(challengee).append(" ").append(instance).append(" ").append(key).append(" ").append(challenger).append(" ").append(pred).append(" ").append(price).append("]");
   }
   public StringBuffer combine(SolvedChallenge _h_, StringBuffer challengee, StringBuffer instance, StringBuffer solution, StringBuffer key, StringBuffer challenger, StringBuffer pred, StringBuffer price){
      return new StringBuffer("").append("solved[").append(challengee).append(" ").append(instance).append(" ").append(solution).append(" ").append(key).append(" ").append(challenger).append(" ").append(pred).append(" ").append(price).append("]");
   }
   public StringBuffer combine(Transaction _h_){
      return new StringBuffer("").append("]");
   }
   public StringBuffer combine(OfferTrans _h_, StringBuffer pred, StringBuffer price){
      return new StringBuffer("").append("\n    offer[").append(pred).append(" ").append(price).append("]");
   }
   public StringBuffer combine(AcceptTrans _h_, StringBuffer challengeid){
      return new StringBuffer("").append("\n    accept[").append(challengeid).append("]");
   }
   public StringBuffer combine(ProvideTrans _h_, StringBuffer inst, StringBuffer challengeid){
      return new StringBuffer("").append("\n    provide[").append(inst).append(" ").append(challengeid).append("]");
   }
   public StringBuffer combine(SolveTrans _h_, StringBuffer sol, StringBuffer challengeid){
      return new StringBuffer("").append("\n    solve[").append(sol).append(" ").append(challengeid).append("]");
   }
   public StringBuffer combine(ReofferTrans _h_, StringBuffer challengeid, StringBuffer price){
      return new StringBuffer("").append("\n    reoffer[").append(challengeid).append(" ").append(price).append("]");
   }
   public StringBuffer combine(PlayerTrans _h_, StringBuffer id, StringBuffer ts){
      return new StringBuffer("").append("\nplayertrans[\n    ").append(id).append(ts).append("\n]\n");
   }
   public StringBuffer combine(Config _h_, StringBuffer gamekind, StringBuffer turndur, StringBuffer mindecr, StringBuffer initacc, StringBuffer objective, StringBuffer predicate, StringBuffer numrounds, StringBuffer profitFactor, StringBuffer otrounds){
      return new StringBuffer("").append("config[\n\n    gamekind: ").append(gamekind).append("\n    turnduration: ").append(turndur).append("\n    mindecrement: ").append(mindecr).append("\n    initacc: ").append(initacc).append("\n    objective: ").append(objective).append("\n    predicate: ").append(predicate).append("\n    numrounds: ").append(numrounds).append("\n    profitfactor: ").append(profitFactor).append("\n\n    otrounds: ").append(otrounds).append("]");
   }
   public StringBuffer combine(PlayerContext _h_, StringBuffer config, StringBuffer id, StringBuffer balance, StringBuffer currentRound, StringBuffer ourOffered, StringBuffer theirOffered, StringBuffer accepted, StringBuffer provided){
      return new StringBuffer("").append("context[\n    ").append(config).append("\n    ").append(id).append("\n    ").append(balance).append("\n    ").append(currentRound).append("\n    (").append(ourOffered).append(")\n    (").append(theirOffered).append(")\n    (").append(accepted).append(")\n    (").append(provided).append(")\n]");
   }
   public StringBuffer combine(PlayerSpec _h_, StringBuffer name, StringBuffer address, StringBuffer port){
      return new StringBuffer("").append("playerspec[").append(name).append(" ").append(address).append(" ").append(port).append("]");
   }
   public StringBuffer combine(PlayersFile _h_, StringBuffer players){
      return new StringBuffer("").append(" ").append(players);
   }
   public StringBuffer combine(PasswordEntry _h_, StringBuffer name, StringBuffer passhash){
      return new StringBuffer("").append("passwd[ ").append(name).append(" ").append(passhash).append(" ]\n");
   }
   public StringBuffer combine(PasswordFile _h_, StringBuffer passwds){
      return new StringBuffer("").append(passwds);
   }
   public StringBuffer combine(TeamSpec _h_, StringBuffer name, StringBuffer passhash, StringBuffer players){
      return new StringBuffer("").append("team[").append(name).append(" ").append(passhash).append(" ").append(players).append("]\n");
   }
   public StringBuffer combine(TeamFile _h_, StringBuffer teams){
      return new StringBuffer("").append(" ").append(teams);
   }
   public StringBuffer combine(History _h_, StringBuffer header, StringBuffer rounds){
      return new StringBuffer("").append(header).append("\n\n").append(rounds);
   }
   public StringBuffer combine(Header _h_, StringBuffer players){
      return new StringBuffer("").append(players);
   }
   public StringBuffer combine(Round _h_, StringBuffer num, StringBuffer trans){
      return new StringBuffer("").append("round[").append(num).append("\n    ").append(trans).append("]");
   }
   public StringBuffer combine(Event _h_){
      return new StringBuffer("").append("\n");
   }
   public StringBuffer combine(PlayerKickedEvent _h_, StringBuffer spec, StringBuffer msg, StringBuffer time){
      return new StringBuffer("").append("\nkicked[").append(spec).append(" ").append(msg).append(" ").append(time).append("]\n");
   }
   public static String escape(String s){
      char str[] = s.toCharArray();
      StringBuffer ret = new StringBuffer("");
      for(char c:str)ret.append(escape(c));
      return ret.toString();
   }
   public static String escape(char c){
      switch(c){
      case '\n':return "\\n";  case '\t':return "\\t";
      case '\b':return "\\b";  case '\r':return "\\r";
      case '\f':return "\\f";  case '\\':return "\\\\";
      case '\'':return "\\'"; case '\"':return "\\\"";
      default: return ""+c;
      }
   }

}


