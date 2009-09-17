/**
 * 
 */
package edu.neu.ccs.evergreen.ir;

/**
 * @author mohsen
 * Class RelationCore provides primitive operations for managing packed truth tables
 * RelationCore is a utility class
 * 
 * Insertion Sort code reused from: http://www.java2s.com/Code/Java/Collections-Data-Structure/Selectionsort.htm
 */
public class RelationCore {
    
    /*
     * Permutation Semantics constant SOURCE @see rename
     */
    public static final int SOURCE = 0;
    /*
     * Permutation Semantics constant TARGET @see rename
     */
    public static final int TARGET = 1;
 
    /**
     * The maximum rank handled by relationCore class
     * The minimum rank is 1 
     */
    public static final int MaxRank = 5;
    /**
     * Returns a magic number associated with a certain truth table column and value
     * The magic number associated with column number 0 of the truth table and value 0
     * is basically a sequence of alternating 0 and 1 bits, packed together in one integer.
     * The magic number associated with column number 0 of the truth table and value 1
     * is a sequence of alternating 1 and 0 bits, packed together in one integer.
     * In general: (getMagicNumber(r,n,0) == ~getMagicNumber(r,n,1))
     * For column 1: magic numbers are fromed from sequences of two 0's followed by two 1's
     * For column 2: magic numbers are formed from sequences of four 0's followed by four 1's
     * For column 3: magic numbers are formed from sequeneces of eight 0's followed by eight 1's
     * For column 4:magic numbers are formed from sequeneces of sixteen 0's followed by sixteen 1's
     * There is no other possible columns as long as we are using 32 bit integers
     * @param rank The rank of the relation, used to determine the truth table height and to check
     * the position argument
     * @param variablePosition the position of the desired magic number
     * @param value the value associated with the desired magic number
     * @return
     * @throws IllegalArgumentException
     */
    public static int getMagicNumber(int rank, int variablePosition, int value) throws IllegalArgumentException{
        checkRank(rank);
        checkVariablePosition(variablePosition, rank);
        checkValue(value);
        
        //Truth Table order
        //each entry in this array represents a packed truth table column
        int[] MagicNumbers= {
                0x55555555, 
                0x33333333,
                0x0F0F0F0F,
                0x00FF00FF,
                0x0000FFFF
                };
        
        // used to cut the truth table column at a certain height
        int mask = getMask(rank);
        
        //cut the truth table column
        int maskedColumn = ((MagicNumbers[variablePosition])&mask);
        
        //for value 0 return the masked truth table column
        //for value 1 return the one's complement of the masked truth table column
        int returnval = (value==1?mask^maskedColumn:maskedColumn);
     return returnval;
    }
    
    /**
     * @param rank the rank for which a mask is seeked
     * @return 2^2^rank-1 taking care of the case where 2^2^n is out side
     * the integer range but 2^2^n-1 is not
     */
    public static int getMask(int rank){
        checkRank(rank);
        int[] mask = {
                0x1,
                0x3,
                0xF,
                0xFF,
                0xFFFF,
                0xFFFFFFFF
        };
        return mask[rank];
    }
    
    /**
     * Checks if the variable at a given position is irrelevant to the relation
     * @param relationNumber The relation number
     * @param rank rank of the relation
     * @param variablePosition the variable to be checked 
     * @return true if the variable at vairablePosition is irrelevant otherwise returns false
     */
    static public boolean isIrrelevant(int relationNumber,int rank, int variablePosition){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkVariablePosition(variablePosition, rank);
        
        //Note that the maximum value of variable position is less than rank by one
        //Therefore, right shift by (1<<variablePosition) won't reach 32 therefore, it'll always execute
        int m = getMagicNumber(rank,variablePosition, 1);
        //Must use >>> (unsigned shift right) coz relations of rank 5 might be -ve
        return ((relationNumber&m)>>>(1<<variablePosition))==(relationNumber&(~m));

/*        The old less efficient way of checking for variable irrelevance
 *         used for cross checking both methods now
 * 
         int r0 = reduce(relationNumber,variableNumber,0);
        int r1 = reduce(relationNumber,variableNumber,1);
        return (r0==r1);
*/    }

    
    /**
     * Counts the number of relevantVariables in the given relation
     * @param relationNumber the relation number whose number of relevant variables is to be counted
     * @param rank rank of the given relation
     * @return The number of relevant variables in the given relation
     */
    static public int numberOfRelevantVariables(int relationNumber, int rank){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        
        int relevantVariables = rank;
        for (int variablePosition=0;variablePosition<rank;variablePosition++){
            if (isIrrelevant(relationNumber,rank, variablePosition)) relevantVariables--;
        }
        return relevantVariables;
    }
    
    /**
     * Checks if the given relation forces the given variablePosition
     * @param relationNumber
     * @param rank rank of the given relation
     * @param variablePosition positon of the varible checked for being forced
     * @return 0 if the given relation forces the given variable to 0
     *         1 if the given relation forces the given variable to 1
     *         -1 given relation doesn't forces the given variable
     */
    static public int isForced(int relationNumber,int rank, int variablePosition){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkVariablePosition(variablePosition, rank);
        
        if (isIrrelevant(relationNumber, rank, variablePosition)){
            return -1;
        }else{
            int m = getMagicNumber(rank,variablePosition, 1);
            int rm = relationNumber&m;
            if(rm == 0){
                return 0;
            }else if (rm==relationNumber){
                return 1;
            }else{
                return -1;
            }
        }        
    }
    
    /**
     * starting at the given startPosition, get the position of the first variable forced by the given relation.
     * @param relationNumber relation number
     * @param rank rank of the given relation number
     * @param startPosition
     * @return -1 if nothing is forced
     *         the position of the first forced variable
     */
    static public int firstForcedVariable(int relationNumber,int rank, int startPosition){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkVariablePosition(startPosition, rank);
        
        int forcedVarPos = -1;
        for(int variablePosition=startPosition;variablePosition<rank;variablePosition++){
            if(isForced(relationNumber, rank, variablePosition)!=-1){
                forcedVarPos = variablePosition;
                break;
            }
        }
        return forcedVarPos;
    }
    
    /**
     * NMaps one of the variables in a relation i.e. replaces it by it's complement
     * for example: nMapping x in Or(x,y,z) results in: or(!x,y,z)
     * @param relationNumber 
     * @param rank rank of the given relation
     * @param variablePosition the variable to be nmapped
     * @return The number of the given relation with the specified variable nmapped
     */
    static public int nMap(int relationNumber,int rank,int variablePosition){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkVariablePosition(variablePosition, rank);
        
        int m0 = getMagicNumber(rank, variablePosition, 0);
        int m1 = getMagicNumber(rank, variablePosition, 1);
        int s = (1<<variablePosition);
        //Note: unsigned right shift
        int r = ((relationNumber&m0)<<s)|((relationNumber&m1)>>>s);
        return r;
    }

    /**
     * Reduces a relation by assigning a value to one of its variables
     * @param relationNumber
     * @param rank
     * @param variablePosition
     * @param value
     * @return
     */
    static public int reduce(int relationNumber,int rank,int variablePosition,int value){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkVariablePosition(variablePosition, rank);
        checkValue(value);
        
        int m = getMagicNumber(rank, variablePosition, value);
        int rm = (relationNumber&m);
        if (value==0){
            rm = rm|(rm<<(1<<variablePosition));
        }else{
            //unsigned right shift
            rm = rm|(rm>>>(1<<variablePosition));
        }
        return rm;          
    }
    
    /**
     * Swaps two vairables in a relation. When variables are swapped, The truth table order gets scrambled
     * rows of the truth table needs to be reordered to restore the correct truth table order.
     * Here are two exmples showing how the swap method works for two relations:
     * 1in3(x,y,z), x implies z. We are swapping variables at positions 0,2 i.e: x ,z
     * 
     *      original relations            scrambled truth table      restored truth table ordering  
     * Row# x y z 1in3(x,y,z)  x=>z  ||  z y x 1in3(z,y,x) x=>z  ||  z y x 1in3(z,y,x) x=>z old_Row#
     * ------------------------------||--------------------------||---------------------------------
     * 0    0 0 0   0           1    ||  0 0 0   0          1    ||  0 0 0    0         1      0
     * 1    0 0 1   1           1    ||  1 0 0   1          1    ||  0 0 1    1         0      4
     * 2    0 1 0   1           1    ||  0 1 0   1          1    ||  0 1 0    1         1      2
     * 3    0 1 1   0           1    ||  1 1 0   0          1    ||  0 1 1    0         0      6
     * 4    1 0 0   1           0    ||  0 0 1   1          0    ||  1 0 0    1         1      1
     * 5    1 0 1   0           1    ||  1 0 1   0          1    ||  1 0 1    0         1      5
     * 6    1 1 0   0           0    ||  0 1 1   0          0    ||  1 1 0    0         1      3
     * 7    1 1 1   0           1    ||  1 1 1   0          1    ||  1 1 1    0         1      7
     * 
     * @param relationNumber
     * @param rank
     * @param variablePosition1
     * @param variablePosition2
     * @return
     */
    static public int swap(int relationNumber,int rank,int variablePosition1,int variablePosition2){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkVariablePosition(variablePosition1, rank);
        checkVariablePosition(variablePosition1, rank);
        
        //Swapping the variable with itself
        if (variablePosition1==variablePosition2) return relationNumber;
        //swap Dim1,Dim2; 
        //Can't we tell java to automatically do the swap by just stating the condition that dim1>dim2
        if(variablePosition1>variablePosition2) {
            int tmp=variablePosition1;
            variablePosition1=variablePosition2;
            variablePosition2=tmp;
        }
        
        int d10 = getMagicNumber(rank, variablePosition1, 0);
        int d11 = getMagicNumber(rank, variablePosition1, 1);
        int d20 = getMagicNumber(rank, variablePosition2, 0);
        int d21 = getMagicNumber(rank, variablePosition2, 1);        
        
        //1in3(x,y,z), swap x,z
        //x=>z
        /*
         *      original relations            scrambled truth table      restored truth table ordering  
         * Row# x y z 1in3(x,y,z)  x=>z  ||  z y x 1in3(z,y,x) x=>z  ||  z y x 1in3(z,y,x) x=>z old_Row#
         * ------------------------------||--------------------------||---------------------------------
         * 0    0 0 0   0           1    ||  0 0 0   0          1    ||  0 0 0    0         1      0
         * 1    0 0 1   1           1    ||  1 0 0   1          1    ||  0 0 1    1         0      4
         * 2    0 1 0   1           1    ||  0 1 0   1          1    ||  0 1 0    1         1      2
         * 3    0 1 1   0           1    ||  1 1 0   0          1    ||  0 1 1    0         0      6
         * 4    1 0 0   1           0    ||  0 0 1   1          0    ||  1 0 0    1         1      1
          * 5    1 0 1   0           1    ||  1 0 1   0          1    ||  1 0 1    0         1      5
          * 6    1 1 0   0           0    ||  0 1 1   0          0    ||  1 1 0    0         1      3
         * 7    1 1 1   0           1    ||  1 1 1   0          1    ||  1 1 1    0         1      7
         */
        //Rows with either 0 or 1 in both columns stay the same
        //e.g. row 0, row 7, other rows depending on the two swapped columns
        //same_filter selects these rows based on the columns
        int same_filter = d11&d21|d10&d20;
        //Assuming that column1 is to the right of column2 which is valid by the swapping we do at the begining of this method
        //Rows where column1 is 0 and column2 is 1 must be moved up because after doing the swap we'll have a 0 in column 2 and 1 in column 1
        //simply because the 0 in column2 means that the row number of column2 becomes smaller than the row number of column1. to restore the proper numbering
        //we have to swap the two rows
        int up_filter = d21&d10;
        //Select rows to be moved down. the reasoning is similar to up_filter
        int dn_filter = d20&d11;
        //shift_amount is the difference between the row number in the before we do the swap and the row number after we do the swap
        //for example if we are swapping the variable at position 2 with the variable at position 0 then the rows to be swapped are of the form        
        // bbbbb1b0 and bbbbb0b1 where b stands for an arbitrary bits that stays the same.
        // Noting that: bbbbb1b0 - bbbbb0b1 is the same as: (bbbbb1b0-bbbbb0b0) - (bbbbb0b1-bbbbb0b0)
        // by subtracting bbbbb0b0 from both numbers we get
        // 00000100 and 00000001
        // Therefore we need to shift both ways by 2^variablePosition2 - 2^variablePosition1
        
        int shift_amt = (1<<variablePosition2)-(1<<variablePosition1); 
        
        // rows to stay at their locations
        int s = relationNumber&same_filter;
        //rows to go up
        int u = relationNumber&up_filter;
        //rows to go down
        int d = relationNumber&dn_filter;
        
        //move the rows and combine the three components
        return (s|(d<<shift_amt)|(u>>>shift_amt));
    }
    
    /**
     * permute the variables in the given relationNumber according to the given permutation
     * fix the truth table order after doing the permutation. @see swap
     * @param relationNumber
     * @param rank rank of the given relation
     * @param permutationSemantics specifies how the permutation should be applied. can be either RelationCore.SOURCE or RelationCore.TARGET
     * for example: <p>
     *    for the relation: R(v2,v1,v0) <p>
     *    and the permutation {1,2,0} 
     *    SOURCE semantics means that v0 goes to position1, v1 goes to position2, v2 goes to position 0
     *    TARGET semantics means that position0 gets v1, position1 gets v2, position2 gets v0
     * @param permutation an array of variable positions describing the desired location of every variables
     * for example: <p>
     *    for the relation: R(v2,v1,v0) <p>
     *    and the permutation {1,2,0} means v0 goes to position1, v1 goes to position2, v2 goes to position 0
     * @return the modified relationNumber
     */
    static public int renme(int relationNumber,int rank,int permutationSemantics, int...permutation){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        checkPermutation(rank, permutation);
        checkPermutationSemantics(permutationSemantics);
        
        //sort dimensions
        int out, in, min, tmp;

        for (out = 0; out < rank - 1; out++){
          min = out; // minimum
          for (in = out + 1; in < rank; in++){
            // inner loop
            if (permutation[in] < permutation[min]) // if min greater,
              min = in; // a new min
          }
          //swap elements at min,out
          tmp = permutation[out];
          permutation[out] = permutation[min];
          permutation[min] = tmp;
          //see
          switch(permutationSemantics){
          case SOURCE:
              relationNumber = swap(relationNumber,rank,permutation[min],permutation[out]);
              break;
          case TARGET:
              relationNumber = swap(relationNumber,rank,min,out);
              break;
              default: throw new IllegalArgumentException("Internal Error: Unsupported semantics"+permutationSemantics);
          }
        }        
        return relationNumber;
    }

    /**
     * returns the number of ones in the given relationNumber
     * @param relationNumber
     * @param rank
     * @return
     */
    public static int ones(int relationNumber,int rank){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        
        int c=0;
        for(int i=0;i<(1<<rank);i++){
            if ((relationNumber&(1<<i))!=0) c++;
        }
        return c;
    }
    /**
     * @param relationNumber
     * @param rank
     * @param numberOfTrueVars used to identify a set of rows in the truth table
     * @return counts the number of ones corresponding to truth table rows with the given number of true variables
     */
    public static int q(int relationNumber, int rank, int numberOfTrueVars){
        checkRank(rank);
        checkRelationNumber(relationNumber, rank);
        if(numberOfTrueVars>rank) throw new IllegalArgumentException("A relation of rank "+rank+" can have up to "+rank+" true vars");
        
        int m = RelationNumberUtil.xTrueVars(rank, numberOfTrueVars);
        return ones(relationNumber&m, rank);
    }
    
    /**
     * Checks if the given Relation number argument is within the range of relations of the given rank
     * for example: relations of rank 3 must be within the range [0,255]
     * in general a relation of rank n must be within the range [0,2^2^n-1]
     * The method is used to check the arguments of a method. Therefore, it works by throwing an instance of 
     * IllegalArgumentException if the relationNumber is invalid otherwise nothing is thrown
     * @param RelationNumber relation number to be checked for validity
     * @param Rank rank of the given relation number to check against
     * @throws IllegalArgumentException thrown in case the relation number is outside the
     * range of valid relation numbers for the given rank
     */
    public static void checkRelationNumber(int relationNumber, int rank) throws IllegalArgumentException{
        int upperbound = getMask(rank);
        if((relationNumber&(~upperbound))!=0)
            throw new IllegalArgumentException("RelationNumber "+relationNumber+" out of range for rank "+rank);
    //  Sometimes the relation number uses the bit sign
    // Therefore it is incorrect to check the relation number to be greater than 0
    //    if((relationNumber<0)||(relationNumber>=upperbound)) 
    //        throw new IllegalArgumentException("RelationNumber "+relationNumber+" out of range for rank "+rank); 
    }

    /**
     * Checks if the given variablePosition argument is valid for the given rank
     * for example: relations of rank 3 have variables only at positions 2,1,0
     * in general a relation of rank n can have variables at positions in range [0,n-1]
     * The method is used to check the arguments of a method. Therefore, it works by throwing an instance of 
     * IllegalArgumentException if the variable position is invalid otherwise nothing is thrown
     * @param variablePosition the checked position
     * @param rank the rank of the relation to check the position agaiest
     * @throws IllegalArgumentException thrown in case of invalid variable position
     */    
    public static void checkVariablePosition(int variablePosition, int rank) throws IllegalArgumentException{
        if((variablePosition<0)||(variablePosition>=rank))
                throw new IllegalArgumentException("Variable Position "+variablePosition+" is invalid for relations of rank "+rank);
    }
    
    /**
     * Checks if the given value is either 0 or 1. An IllegalArgumentException is thrown if value is not 0 nor 1.
     * @param value the value that needs to be checked
     * @throws IllegalArgumentException
     */
    public static void checkValue(int value) throws IllegalArgumentException{
        if((value<0)||(value>1))
            throw new IllegalArgumentException("Variables can only be either 0 or 1. recieved "+value);
    }
    
    /**
     * Checks if the given rank is valid. 32 Bit integers allow for up to rank 5
     * Rank has to be >=1.
     * If the rank is outside the range an IllegalArgumentExcepton is thrown
     * @param rank the rank to be checked
     * @throws IllegalArgumentException
     */
    public static void checkRank(int rank) throws IllegalArgumentException{
        if((rank<1)||(rank>MaxRank))
            throw new IllegalArgumentException("Supported ranks are within [1,5]. Sent rank "+rank);
    }
    
    /**
     * For the given permutation check the following:<p>
     * 1. The number of elements of the permutation must be equal to the given rank
     * 2. every element in the permutation must be a valid variable position for the given rank
     * 3. Non of the elements is repeated.
     * 
     * @param rank 
     * @param permutation
     * @throws IllegalArgumentException thown if any of the above checks fail
     */
    public static void checkPermutation(int rank,int...permutation) throws IllegalArgumentException{
        if(permutation.length!=rank)
            throw new IllegalArgumentException("Invalid Permutation: incorrect number of variable positions");

        //initialize a counter array
        int[] positions = new int[rank];
        for(int i = 0;i<rank;i++){
            positions[i] = 0;
        }
        
        for(int variablePosition: permutation){
            checkVariablePosition(variablePosition, rank);
            if (positions[variablePosition]!=0)
                throw new IllegalArgumentException("Invalid Permutation: position "+variablePosition+" repeated");
            positions[variablePosition]++;
        }
    }
    /**
     * Checks if the given value is either 0 or 1. An IllegalArgumentException is thrown if value is not 0 nor 1.
     * @param value the value that needs to be checked
     * @throws IllegalArgumentException
     */
    public static void checkPermutationSemantics(int permutationSemantics) throws IllegalArgumentException{
        if(!((permutationSemantics==SOURCE)||(permutationSemantics==TARGET)))
            throw new IllegalArgumentException("Illegal permutation semantics. Should be either RelationCore.SOURCE or RelationCore.TARGET");
    }
}
