/* Generated By:JavaCC: Do not edit this line. TheParserTokenManager.java */
package gen;
import edu.neu.ccs.demeterf.lib.*;
import edu.neu.ccs.demeterf.*;
import edu.neu.ccs.demeterf.control.Fields;
import edu.neu.ccs.demeterf.lib.ident;
import edu.neu.ccs.demeterf.lib.verbatim;

public class TheParserTokenManager implements TheParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0xc072L) != 0L)
         {
            jjmatchedKind = 22;
            return 43;
         }
         return -1;
      case 1:
         if ((active0 & 0x40L) != 0L)
            return 43;
         if ((active0 & 0xc032L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 1;
            return 43;
         }
         return -1;
      case 2:
         if ((active0 & 0xc032L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 2;
            return 43;
         }
         return -1;
      case 3:
         if ((active0 & 0x4000L) != 0L)
            return 43;
         if ((active0 & 0x8022L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 3;
            return 43;
         }
         return -1;
      case 4:
         if ((active0 & 0x2L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 4;
            return 43;
         }
         if ((active0 & 0x8000L) != 0L)
            return 43;
         return -1;
      case 5:
         if ((active0 & 0x2L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 5;
            return 43;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 13:
         jjmatchedKind = 10;
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 40:
         return jjStopAtPos(0, 2);
      case 41:
         return jjStopAtPos(0, 3);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 111:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 112:
         return jjMoveStringLiteralDfa1_0(0x2L);
      case 114:
         return jjMoveStringLiteralDfa1_0(0x10L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x4000L);
      case 118:
         return jjMoveStringLiteralDfa1_0(0x20L);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 10:
         if ((active0 & 0x800L) != 0L)
            return jjStopAtPos(1, 11);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x8020L);
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x10L);
      case 114:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(1, 6, 43);
         return jjMoveStringLiteralDfa2_0(active0, 0x4002L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x8010L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x2L);
      case 114:
         return jjMoveStringLiteralDfa3_0(active0, 0x20L);
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x4000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 58:
         if ((active0 & 0x10L) != 0L)
            return jjStopAtPos(3, 4);
         break;
      case 98:
         return jjMoveStringLiteralDfa4_0(active0, 0x2L);
      case 101:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(3, 14, 43);
         break;
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x8020L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 58:
         if ((active0 & 0x20L) != 0L)
            return jjStopAtPos(4, 5);
         break;
      case 101:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(4, 15, 43);
         break;
      case 108:
         return jjMoveStringLiteralDfa5_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa6_0(active0, 0x2L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 109:
         if ((active0 & 0x2L) != 0L)
            return jjStartNfaWithStates_0(6, 1, 43);
         break;
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 57;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 16)
                        kind = 16;
                     jjCheckNAddStates(0, 2);
                  }
                  else if ((0x280000000000L & l) != 0L)
                     jjCheckNAddTwoStates(1, 2);
                  else if (curChar == 47)
                     jjAddStates(3, 4);
                  else if (curChar == 36)
                  {
                     if (kind > 22)
                        kind = 22;
                     jjCheckNAdd(43);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(5, 7);
                  else if (curChar == 39)
                     jjAddStates(8, 9);
                  else if (curChar == 46)
                     jjCheckNAdd(13);
                  if (curChar == 45)
                     jjCheckNAdd(6);
                  else if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAdd(1);
                  break;
               case 2:
                  if (curChar == 48)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 4:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 5:
                  if (curChar == 45)
                     jjCheckNAdd(6);
                  break;
               case 6:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(6, 7);
                  break;
               case 7:
                  if (curChar == 46)
                     jjCheckNAdd(8);
                  break;
               case 8:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAddTwoStates(8, 9);
                  break;
               case 10:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(11);
                  break;
               case 11:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAdd(11);
                  break;
               case 12:
                  if (curChar == 46)
                     jjCheckNAdd(13);
                  break;
               case 13:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAddTwoStates(13, 14);
                  break;
               case 15:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(16);
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAdd(16);
                  break;
               case 17:
                  if (curChar == 39)
                     jjAddStates(8, 9);
                  break;
               case 18:
                  if ((0xffffff7fffffdbffL & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 19:
                  if (curChar == 39 && kind > 19)
                     kind = 19;
                  break;
               case 21:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 22:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(23, 19);
                  break;
               case 23:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 24:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 25;
                  break;
               case 25:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(23);
                  break;
               case 26:
                  if (curChar == 34)
                     jjCheckNAddStates(5, 7);
                  break;
               case 27:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddStates(5, 7);
                  break;
               case 29:
                  if ((0x8400000000L & l) != 0L)
                     jjCheckNAddStates(5, 7);
                  break;
               case 30:
                  if (curChar == 34 && kind > 20)
                     kind = 20;
                  break;
               case 31:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(10, 13);
                  break;
               case 32:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(5, 7);
                  break;
               case 33:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 34;
                  break;
               case 34:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAdd(32);
                  break;
               case 36:
                  jjCheckNAddStates(14, 16);
                  break;
               case 38:
                  jjCheckNAddStates(17, 19);
                  break;
               case 42:
                  if (curChar != 36)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAdd(43);
                  break;
               case 43:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAdd(43);
                  break;
               case 44:
                  if (curChar == 47)
                     jjAddStates(3, 4);
                  break;
               case 45:
                  if (curChar == 47)
                     jjCheckNAddStates(20, 22);
                  break;
               case 46:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(20, 22);
                  break;
               case 47:
               case 48:
                  if (curChar == 10 && kind > 12)
                     kind = 12;
                  break;
               case 49:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 48;
                  break;
               case 50:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(51, 52);
                  break;
               case 51:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(51, 52);
                  break;
               case 52:
                  if (curChar == 42)
                     jjAddStates(23, 24);
                  break;
               case 53:
                  if ((0xffff7fffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(54, 52);
                  break;
               case 54:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(54, 52);
                  break;
               case 55:
                  if (curChar == 47 && kind > 13)
                     kind = 13;
                  break;
               case 56:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAddStates(0, 2);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 22)
                        kind = 22;
                     jjCheckNAdd(43);
                  }
                  else if (curChar == 123)
                     jjstateSet[jjnewStateCnt++] = 35;
                  break;
               case 3:
                  if ((0x100000001000000L & l) != 0L)
                     jjCheckNAdd(4);
                  break;
               case 4:
                  if ((0x7e0000007eL & l) == 0L)
                     break;
                  if (kind > 16)
                     kind = 16;
                  jjCheckNAdd(4);
                  break;
               case 9:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(25, 26);
                  break;
               case 14:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(27, 28);
                  break;
               case 18:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 20:
                  if (curChar == 92)
                     jjAddStates(29, 31);
                  break;
               case 21:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 27:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(5, 7);
                  break;
               case 28:
                  if (curChar == 92)
                     jjAddStates(32, 34);
                  break;
               case 29:
                  if ((0x14404410000000L & l) != 0L)
                     jjCheckNAddStates(5, 7);
                  break;
               case 35:
                  if (curChar == 123)
                     jjCheckNAddStates(14, 16);
                  break;
               case 36:
                  if ((0xdfffffffffffffffL & l) != 0L)
                     jjCheckNAddStates(14, 16);
                  break;
               case 37:
                  if (curChar == 125)
                     jjCheckNAdd(38);
                  break;
               case 38:
                  if ((0xdfffffffffffffffL & l) != 0L)
                     jjCheckNAddStates(17, 19);
                  break;
               case 39:
                  if (curChar == 125 && kind > 21)
                     kind = 21;
                  break;
               case 40:
                  if (curChar == 125)
                     jjstateSet[jjnewStateCnt++] = 39;
                  break;
               case 41:
                  if (curChar == 123)
                     jjstateSet[jjnewStateCnt++] = 35;
                  break;
               case 42:
               case 43:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 22)
                     kind = 22;
                  jjCheckNAdd(43);
                  break;
               case 46:
                  jjAddStates(20, 22);
                  break;
               case 51:
                  jjCheckNAddTwoStates(51, 52);
                  break;
               case 53:
               case 54:
                  jjCheckNAddTwoStates(54, 52);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 18:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 27:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(5, 7);
                  break;
               case 36:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddStates(14, 16);
                  break;
               case 38:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddStates(17, 19);
                  break;
               case 46:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(20, 22);
                  break;
               case 51:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(51, 52);
                  break;
               case 53:
               case 54:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(54, 52);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 57 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   1, 6, 7, 45, 50, 27, 28, 30, 18, 20, 27, 28, 32, 30, 36, 37, 
   40, 37, 38, 40, 46, 47, 49, 53, 55, 10, 11, 15, 16, 21, 22, 24, 
   29, 31, 33, 
};
public static final String[] jjstrLiteralImages = {
"", "\160\162\157\142\154\145\155", "\50", "\51", "\162\145\154\72", 
"\166\141\162\163\72", "\157\162", null, null, null, null, null, null, null, "\164\162\165\145", 
"\146\141\154\163\145", null, null, null, null, null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x7bc07fL, 
};
static final long[] jjtoSkip = {
   0x3f80L, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[57];
private final int[] jjstateSet = new int[114];
protected char curChar;
public TheParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public TheParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 57; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100000600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}
