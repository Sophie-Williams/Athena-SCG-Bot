/* Generated By:JavaCC: Do not edit this line. TheParser.java */
package gen;

import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.control.Fields;
import edu.neu.ccs.demeterf.lib.ident;
import edu.neu.ccs.demeterf.lib.verbatim;

  class TheParser implements TheParserConstants {

   public static String unescape(String str){
      String retval = "";
      int index = 0;
      char ch, ch1;
      int ordinal = 0;
      while (index < str.length()) {
         if(str.charAt(index) != '\\') { retval += str.charAt(index++); continue; }
         ch = str.charAt(++index);
         if(ch == 'b') { retval += '\b'; index++; continue; }
         if(ch == 't') { retval += '\t'; index++; continue; }
         if(ch == 'n') { retval += '\n'; index++; continue; }
         if(ch == 'f') { retval += '\f'; index++; continue; }
         if(ch == 'r') { retval += '\r'; index++; continue; }
         if(ch == '"') { retval += '\"'; index++; continue; }
         if(ch == '\'') { retval += '\''; index++; continue; }
         if(ch == '\\') { retval += '\\'; index++; continue; }
         if(ch >= '0' && ch <= '7'){
            ordinal = ((int)ch) - ((int)'0'); index++;
            ch1 = str.charAt(index);
            if(ch1 >= '0' && ch1 <= '7'){
               ordinal = ordinal*8 + ((int)ch1) - ((int)'0'); index++;
               ch1 = str.charAt(index);
               if(ch <= '3' && ch1 >= '0' && ch1 <= '7'){
                  ordinal = ordinal*8 + ((int)ch1) - ((int)'0'); index++;
               }
            }
            retval += (char)ordinal;
            continue;
         }
         if(ch == 'u'){
            ordinal = 0;
            for(int i = 0; i < 4; i++){
               index++; ch = str.charAt(index);
               ordinal = ordinal*16+hexval(ch);
            }
            index++;
            retval += (char)ordinal;
            continue;
         }
      }
      return retval;
   }
   static int hexval(char c){
      int r = "0123456789ABCDEF".indexOf(Character.toUpperCase(c));
      if(r >= 0)return r;
      throw new RuntimeException(" ** Bad Escaped Character");
   }

  final public byte parse_byte() throws ParseException {
                    int i;
    i = parse_int();
                      {if (true) return (byte)i;}
    throw new Error("Missing return statement in function");
  }

  final public Byte parse_Byte() throws ParseException {
                    byte b;
    b = parse_byte();
                       {if (true) return b;}
    throw new Error("Missing return statement in function");
  }

  final public short parse_short() throws ParseException {
                      int i;
    i = parse_int();
                      {if (true) return (short)i;}
    throw new Error("Missing return statement in function");
  }

  final public Short parse_Short() throws ParseException {
                      short s;
    s = parse_short();
                        {if (true) return s;}
    throw new Error("Missing return statement in function");
  }

  final public int parse_int() throws ParseException {
                  Token t;
    t = jj_consume_token(INT);
      if(t.image.length() > 1 && Character.toLowerCase(t.image.charAt(1)) == 'x')
          {if (true) return Integer.parseInt(t.image.substring(2), 16);}
      {if (true) return Integer.parseInt(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public Integer parse_Integer() throws ParseException {
                          int i;
    i = parse_int();
                      {if (true) return i;}
    throw new Error("Missing return statement in function");
  }

  final public long parse_long() throws ParseException {
                    Token t;
    t = jj_consume_token(INT);
      if(t.image.length() > 1 && Character.toLowerCase(t.image.charAt(1)) == 'x')
          {if (true) return Long.parseLong(t.image.substring(2), 16);}
    {if (true) return Long.parseLong(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public Long parse_Long() throws ParseException {
                    long l;
    l = parse_long();
                       {if (true) return l;}
    throw new Error("Missing return statement in function");
  }

  final public double parse_double() throws ParseException {
                        Token t;
    t = jj_consume_token(DOUBLE);
      {if (true) return Double.parseDouble(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public Double parse_Double() throws ParseException {
                        double d;
    d = parse_double();
                         {if (true) return d;}
    throw new Error("Missing return statement in function");
  }

  final public float parse_float() throws ParseException {
                      Token t;
    t = jj_consume_token(DOUBLE);
      {if (true) return Float.parseFloat(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public Float parse_Float() throws ParseException {
                      float f;
    f = parse_float();
                        {if (true) return f;}
    throw new Error("Missing return statement in function");
  }

  final public String parse_String() throws ParseException {
                        Token t;
    t = jj_consume_token(STRING);
      {if (true) return unescape(t.image.substring(1,t.image.length()-1));}
    throw new Error("Missing return statement in function");
  }

  final public boolean parse_boolean() throws ParseException {
                          Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      t = jj_consume_token(TRUE);
                 {if (true) return true;}
      break;
    case FALSE:
      t = jj_consume_token(FALSE);
                  {if (true) return false;}
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Boolean parse_Boolean() throws ParseException {
                          boolean b;
    b = parse_boolean();
                          {if (true) return b;}
    throw new Error("Missing return statement in function");
  }

  final public char parse_char() throws ParseException {
                    Token t;
    t = jj_consume_token(CHAR);
      {if (true) return unescape(t.image.substring(1,t.image.length()-1)).charAt(0);}
    throw new Error("Missing return statement in function");
  }

  final public Character parse_Character() throws ParseException {
                              char c;
    c = parse_char();
                       {if (true) return c;}
    throw new Error("Missing return statement in function");
  }

  final public ident parse_ident() throws ParseException {
                      Token t;
    t = jj_consume_token(IDENT);
      {if (true) return new ident(t.image);}
    throw new Error("Missing return statement in function");
  }

  final public verbatim parse_verbatim() throws ParseException {
                            Token t;
    t = jj_consume_token(TEXT);
      {if (true) return new verbatim(t.image.substring(2,t.image.length()-2));}
    throw new Error("Missing return statement in function");
  }

  final public SimpleProblems parse_SimpleProblems() throws ParseException {
     List<Problem> constraints;
    constraints = parse_List$Problem$();
    jj_consume_token(0);
      {if (true) return new SimpleProblems(constraints);}
    throw new Error("Missing return statement in function");
  }

  final public Problem parse_Problem() throws ParseException {
     List<Constraint> constraints;
    jj_consume_token(1);
    jj_consume_token(2);
    constraints = parse_List$Constraint$();
    jj_consume_token(3);
      {if (true) return new Problem(constraints);}
    throw new Error("Missing return statement in function");
  }

  final public Constraint parse_Constraint() throws ParseException {
     Relation r;
     List<Variable> vars;
    jj_consume_token(4);
    r = parse_Relation();
    jj_consume_token(5);
    vars = parse_List$Variable$();
      {if (true) return new Constraint(r,vars);}
    throw new Error("Missing return statement in function");
  }

  final public Relation parse_Relation() throws ParseException {
     Length length;
     Positive pos;
    jj_consume_token(6);
    length = parse_Length();
    pos = parse_Positive();
      {if (true) return new Relation(length,pos);}
    throw new Error("Missing return statement in function");
  }

  final public Length parse_Length() throws ParseException {
     int v;
    v = parse_int();
      {if (true) return new Length(v);}
    throw new Error("Missing return statement in function");
  }

  final public Positive parse_Positive() throws ParseException {
     int v;
    v = parse_int();
      {if (true) return new Positive(v);}
    throw new Error("Missing return statement in function");
  }

  final public Variable parse_Variable() throws ParseException {
     ident v;
    v = parse_ident();
      {if (true) return new Variable(v);}
    throw new Error("Missing return statement in function");
  }

  final public List<Variable> parse_List$Variable$() throws ParseException {
    List<Variable> sup = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENT:
      sup = parse_Cons$Variable$();
                                   {if (true) return sup;}
      break;
    default:
      jj_la1[1] = jj_gen;
      sup = parse_Empty$Variable$();
                                    {if (true) return sup;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Empty<Variable> parse_Empty$Variable$() throws ParseException {
      {if (true) return new Empty<Variable>();}
    throw new Error("Missing return statement in function");
  }

  final public Cons<Variable> parse_Cons$Variable$() throws ParseException {
     Variable first;
     List<Variable> rest;
    first = parse_Variable();
    rest = parse_List$Variable$();
      {if (true) return new Cons<Variable>(first,rest);}
    throw new Error("Missing return statement in function");
  }

  final public List<Constraint> parse_List$Constraint$() throws ParseException {
    List<Constraint> sup = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 4:
      sup = parse_Cons$Constraint$();
                                     {if (true) return sup;}
      break;
    default:
      jj_la1[2] = jj_gen;
      sup = parse_Empty$Constraint$();
                                      {if (true) return sup;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Empty<Constraint> parse_Empty$Constraint$() throws ParseException {
      {if (true) return new Empty<Constraint>();}
    throw new Error("Missing return statement in function");
  }

  final public Cons<Constraint> parse_Cons$Constraint$() throws ParseException {
     Constraint first;
     List<Constraint> rest;
    first = parse_Constraint();
    rest = parse_List$Constraint$();
      {if (true) return new Cons<Constraint>(first,rest);}
    throw new Error("Missing return statement in function");
  }

  final public List<Problem> parse_List$Problem$() throws ParseException {
    List<Problem> sup = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 1:
      sup = parse_Cons$Problem$();
                                  {if (true) return sup;}
      break;
    default:
      jj_la1[3] = jj_gen;
      sup = parse_Empty$Problem$();
                                   {if (true) return sup;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Empty<Problem> parse_Empty$Problem$() throws ParseException {
      {if (true) return new Empty<Problem>();}
    throw new Error("Missing return statement in function");
  }

  final public Cons<Problem> parse_Cons$Problem$() throws ParseException {
     Problem first;
     List<Problem> rest;
    first = parse_Problem();
    rest = parse_List$Problem$();
      {if (true) return new Cons<Problem>(first,rest);}
    throw new Error("Missing return statement in function");
  }

  public TheParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[4];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0xc000,0x400000,0x10,0x2,};
   }

  public TheParser(java.io.InputStream stream) {
     this(stream, null);
  }
  public TheParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new TheParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 4; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 4; i++) jj_la1[i] = -1;
  }

  public TheParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TheParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 4; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 4; i++) jj_la1[i] = -1;
  }

  public TheParser(TheParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 4; i++) jj_la1[i] = -1;
  }

  public void ReInit(TheParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 4; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[23];
    for (int i = 0; i < 23; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 4; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 23; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  }
