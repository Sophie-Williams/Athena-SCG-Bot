/**
 * 
 */
package edu.neu.ccs.evergreen.ir;

/**
 * @author mohsen
 *
 */
public interface RelationI {
    /*
     * Permutation Semantics constant SOURCE @see rename
     */
    public static final int SOURCE = RelationCore.SOURCE;
    /*
     * Permutation Semantics constant TARGET @see rename
     */
    public static final int TARGET = RelationCore.TARGET;
 
    
    /**
     * @return an integer representing the relation number
     */
    public int getRelationNumber();
    
    /**
     * @param relationNumber 
     * @throws IllegalArgumentException Thrwon if the relation number is invalid according to the rank
     */
    public void setRelationNumber(int relationNumber)throws IllegalArgumentException;
    
    /**
     * @return rank of the relation
     */
    public int getRank();
    
    /**
     * Currently unsupported
     * Usually converting to a higher rank is simple. however converting to a lower rank might require the introduction of auxiliary variables and more than one relation might result from the operation
     * @param rank sets the rank of the relation
     * @throws IllegalArgumentException
     */
    public void setRank(int rank) throws IllegalArgumentException;
    
    /**
     * Returns a magic number associated with a certain truth table column and value
     * The magic number associated with column number 0 of the truth table and value 0
     * is basically a sequence of alternating 0 and 1 bits, packed together in one integer.
     * The magic number associated with column number 0 of the truth table and value 1
     * is a sequence of alternating 1 and 0 bits, packed together in one integer.
     * In general: (getMagicNumber(n,0) == ~getMagicNumber(n,1))
     * For column 1: magic numbers are fromed from sequences of two 0's followed by two 1's
     * For column 2: magic numbers are formed from sequences of four 0's followed by four 1's
     * For column 3: magic numbers are formed from sequeneces of eight 0's followed by eight 1's
     * For column 4:magic numbers are formed from sequeneces of sixteen 0's followed by sixteen 1's
     * There is no other possible columns as long as we are using 32 bit integers
     * 
     * The lenght of the magic number depends on the internally stored relation rank
     * @param variablePosition the position of the desired magic number
     * @param value the value associated with the desired magic number
     * @return
     * @throws IllegalArgumentException
     */
    public int getMagicNumber(int variablePosition, int value) throws IllegalArgumentException;
        
    
    /**
     * @return An integer with all of its bits set to 1. The length of the integer depends on the internally stored rank. <br>
     * the result is: 2^2^rank-1<br>
     * Note: the method takes care of the case where 2^2^n is outside the integer range but 2^2^n-1 is not.
     */
    public int getMask();
        
    /**
     * Checks if the variable at a given position is irrelevant to the internally stored relation
     * @param variablePosition the variable to be checked 
     * @return true if the variable at vairablePosition is irrelevant otherwise returns false
     * @throws IllegalArgumentException
     */
    public boolean isIrrelevant(int variablePosition) throws IllegalArgumentException;

    
    /**
     * Counts the number of relevantVariables in the relation
     * @return The number of relevant variables in the relation
     */
    public int numberOfRelevantVariables();
    /**
     * Checks if the relation forces the given variablePosition
     * @param variablePosition positon of the varible checked for being forced
     * @return 0 if the given relation forces the given variable to 0
     *         1 if the given relation forces the given variable to 1
     *         -1 given relation doesn't forces the given variable
     * @throws IllegalArgumentException
     */
    public int isForced(int variablePosition) throws IllegalArgumentException;
    
    /**
     * starting at the given startPosition, get the position of the first variable forced by the relation.
     * @param startPosition
     * @return -1 if nothing is forced
     *         the position of the first forced variable
     * @throws IllegalArgumentException
     */
    public int firstForcedVariable(int startPosition) throws IllegalArgumentException;
    
    /**
     * NMaps one of the variables in a relation i.e. replaces it by it's complement
     * for example: nMapping x in Or(x,y,z) results in: or(!x,y,z)
     * @param relationNumber 
     * @param rank rank of the given relation
     * @param variablePosition the variable to be nmapped
     * @return The number of the given relation with the specified variable nmapped
     * @throws IllegalArgumentException
     */
    public int nMap(int variablePosition) throws IllegalArgumentException;
    /**
     * Reduces a relation by assigning a value to one of its variables
     * @param relationNumber
     * @param rank
     * @param variablePosition
     * @param value
     * @return
     * @throws IllegalArgumentException
     */
    public int reduce(int variablePosition,int value) throws IllegalArgumentException;
    
    public int swap(int variablePosition1,int variablePosition2) throws IllegalArgumentException;
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
    /**
     * @param relationNumber
     * @param rank
    
     * @param permutation
     * @return
     */
    public int renme(int permutationSemantics, int...permutation) throws IllegalArgumentException;
    
    /**
     * returns the number of ones in the given relationNumber
     * @param relationNumber
     * @param rank
     * @return
     */
    public int ones();
    
    /**
     * @param s
     * @return the number of rows in the truth table with s 1's
     * @throws IllegalArgumentException
     */
    public int q(int s) throws IllegalArgumentException;    
}
