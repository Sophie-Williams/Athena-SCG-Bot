// ** This class was generated with DemFGen (vers:09/12/2009)

package gen;

import edu.neu.ccs.demeterf.control.Fields;
import edu.neu.ccs.demeterf.lib.ident;
import edu.neu.ccs.demeterf.lib.verbatim;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;



/** Computes a String representation in CD Syntax */
public class Print extends edu.neu.ccs.demeterf.ID{
   /** Static stub method for calling print */
   public static String PrintM(Object o){
      return new edu.neu.ccs.demeterf.Traversal(new Print(),edu.neu.ccs.demeterf.Control.builtins()).<Print._LT>traverse(o).toString();
   }

   public _LT combine(byte _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(short _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(int _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(long _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(float _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(double _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(char _h_){ return empty.append(new _S("\'"+escape(""+_h_)+"\'")); }
   public _LT combine(boolean _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(String _h_){ return empty.append(new _S("\""+escape(""+_h_)+"\"")); }
   public _LT combine(ident _h_){ return empty.append(new _S(""+_h_)); }
   public _LT combine(verbatim _h_){ return empty.append(new _S(""+_h_)); }

   static _LT empty = new _LT();
   public  static class _LT{
     edu.neu.ccs.demeterf.lib.List<_T> l;
     public _LT(){ l = new edu.neu.ccs.demeterf.lib.Empty<_T>(); }
     public _LT(edu.neu.ccs.demeterf.lib.List<_T> ll){ l = ll; }
     public _LT push(_T t){ return new _LT(l.push(t)); }
     public _LT append(_T t){ return new _LT(l.append(t)); }
     public _LT append(_LT t){ return new _LT(l.append(t.l)); }
     public String toString(){ return l.fold(new _F(),""); }
     public _LT compress(){ return l.isEmpty()?this:compress(new _E(), l); }
     public static _LT compress(_S s, edu.neu.ccs.demeterf.lib.List<_T> r){
        if(r.isEmpty())return (s.isE())?empty:empty.push(s);
          _T top = r.top();
          if(top.isS())return compress(s.append((_S)top), r.pop());
          if(s.isE())return compress(s, r.pop()).push(top);
          return compress(new _E(), r.pop()).push(top).push(s);
     }
   }
   public static class _T{
     public boolean isS(){ return false; }
     public boolean isE(){ return false; }
   }
   public static class _P extends _T{ public static _P p = new _P(); }
   public static class _M extends _T{ public static _M m = new _M(); }
   public static class _N extends _T{ public static _N n = new _N(); }
   public static class _S extends _T{
     public StringBuffer s;
     public _S(String ss){ s = new StringBuffer(ss); }
     public _S append(_S ss){ s.append(ss.s); return this; }
     public  boolean isS(){ return true; }
   }
   public static class _E extends _S{
     public _E(){ super("");}
     public  boolean isS(){ return true; }
   }
   public static class _F extends edu.neu.ccs.demeterf.lib.List.Fold<_T,String>{
     int idt = 0;
     public  String fold(_T t, String s){
        if(t == _P.p)plus();
        else if(t == _M.m)minus();
        else if(t == _N.n)s += "\n"+indent();
        else s += ((_S)t).s;
        return s;
     }
     void plus(){ idt++; }
     void minus(){ idt--; }
     String indent(){ return indent(idt); }
     static String indent(int i){ return (i <= 0)?"":"   "+indent(i-1); }
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
   public _LT combine(Cons _h_, _LT first, _LT rest){
      return empty.append(first).append(rest).compress();
   }
   public _LT combine(Empty _h_){
      return empty;
   }
   public _LT combine(SimpleProblems _h_, _LT constraints){
      return empty.append(constraints).compress();
   }
   public _LT combine(Problem _h_, _LT constraints){
      return empty.append(new _S("problem")).append(new _S("(")).append(constraints).append(new _S(")")).compress();
   }
   public _LT combine(Constraint _h_, _LT r, _LT vars){
      return empty.append(new _S("rel:")).append(r).append(new _S("vars:")).append(vars).compress();
   }
   public _LT combine(Relation _h_, _LT length, _LT pos){
      return empty.append(new _S("or")).append(length).append(pos).compress();
   }
   public _LT combine(Length _h_, _LT v){
      return empty.append(v).compress();
   }
   public _LT combine(Positive _h_, _LT v){
      return empty.append(v).compress();
   }
   public _LT combine(Variable _h_, _LT v){
      return empty.append(v).compress();
   }

}


