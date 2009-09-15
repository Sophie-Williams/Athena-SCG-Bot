/**
 * 
 */
package edu.neu.ccs.evergreen.ir;

/**
 * @author mohsen
 * The RelationNumberUtil class provides a set of methods for synthesizing relation numbers.
 * Note that the created relation numbers can be manipulated using bitwise operations
 * provided that they share the same variables and same variable ordering
 * Variable positions are 0 based
 * for example:
 * int x = 0;
 * int y = 1;
 * int z = 2;
 * int w = 3;
 * int r_or_xy = RelationNumberUtil.or(4,x,y)
 * int r_or_zw =  RelationNumberUtil.or(4,z,w)
 * int r_or_xyzw = r_or_xy|r_or_zw
 * 
 * The relation provides:
 * 		 or(arbitrary number of variables,
 *       and(arbitrary number of variables,
 *       imply (two vars)
 *       equiv (two vars)
 *       xor (two vars)
 *       
 * Use RelationCore.getMagicNumber(rank,x,1) to get a relation representing x
 * Use RelationCore.getMagicNumber(rank,x,0) to get a relation representing !x
 * 
 */

public class RelationNumberUtil {

	/**
	 * Given a set of variable positions, return a relation that is the or of these variables
	 * @param rank the rank of the created relation
	 * @param variablePositions an arbitrary number of variable positions
	 * @return a relation number that represents the oring of the variables at the given positions
	 */
	public static int or(int rank, int ... variablePositions){
		int relationNumber=0;
		for(int variablePosition: variablePositions){
			// Note that the getMagicNumber will check for the validity of the given variablePosition
			relationNumber|= RelationCore.getMagicNumber(rank, variablePosition, 1);
		}
		return relationNumber;
	}

	/**
	 * Given a set of variable positions, return a relation that is the anding of these variables
	 * @param rank the rank of the created relation
	 * @param variablePositions an arbitrary number of variable positions
	 * @return a relation number that represents the anding of the variables at the given positions
	 */
	public static int and(int rank, int ... variablePositions){
		int relationNumber = RelationCore.getMask(rank);
		for(int variablePosition: variablePositions){
			// Note that the getMagicNumber will check for the validity of the given variablePosition
			relationNumber&= RelationCore.getMagicNumber(rank, variablePosition, 1);
		}
		return relationNumber;
	}

	/**
	 * nMaps a number of variables in a certain relation
	 * @param relationNumber  
	 * @param rank the rank of the created relation
	 * @param variablePositions an arbitrary number of variable positions
	 * @return a relation number that represents the given relation with the given set of variables renamed
	 */
	public static int nMap(int relationNumber,int rank, int ... variablePositions){
		for(int variablePosition: variablePositions){
			// Note that the getMagicNumber will check for the validity of the given variablePosition
			relationNumber = RelationCore.nMap(relationNumber, rank, variablePosition);
			}
		return relationNumber;
	}
	
	/**
	 * Given two variables x,y returns a relation that represents x==>y
	 * @param rank the rank of the created relation
	 * @param variablePositions two variable positions
	 * @return a relation number that represents the x=>y where x,y are the given two variable positions
	 */
	public static int imply(int rank, int ... variablePositions){
		if(variablePositions.length>2) 
			throw new IllegalArgumentException("imply supports up to 2 variables. Passed "+variablePositions.length);
		
		int relationNumber = or(rank, variablePositions[0],variablePositions[1]); 
		return nMap(relationNumber, rank, variablePositions[0]);
	}
	
	/**
	 * Given two variables x,y returns a relation that represents x=y
	 * @param rank the rank of the created relation
	 * @param variablePositions two variable positions
	 * @return a relation number that represents the x=y where x,y are the given two variable positions
	 */
	public static int equiv(int rank, int ... variablePositions){
		if(variablePositions.length>2) 
			throw new IllegalArgumentException("equiv supports up to 2 variables. Passed "+variablePositions.length);
		
		int bothNeg = and(rank,variablePositions[0],variablePositions[1]);
		bothNeg = nMap(bothNeg, rank, variablePositions);
		int bothPos = and(rank,variablePositions[0],variablePositions[1]);
		return bothNeg|bothPos;
	}
	/**
	 * Given two variables x,y returns a relation that represents xor(x,y)
	 * @param rank the rank of the created relation
	 * @param variablePositions two variable positions
	 * @return a relation number that represents the xor(x,y) where x,y are the given two variable positions
	 */
	public static int xor(int rank, int ... variablePositions){
		if(variablePositions.length>2) 
			throw new IllegalArgumentException("xor supports up to 2 variables. Passed "+variablePositions.length);
		
		int posNeg = and(rank,variablePositions[0],variablePositions[1]);
		posNeg = nMap(posNeg, rank, variablePositions[1]);
		int negPos = and(rank,variablePositions[0],variablePositions[1]);
		negPos = nMap(negPos, rank, variablePositions[0]);
		return posNeg|negPos;
	}

	/**
	 * for 1in3 use xTrueVars(3,1)
	 * @param rank
	 * @param numberOfTrueVars
	 * @return an integer representing the relation number which is true only when the given number of vars is true
	 */
	public static int xTrueVars(int rank,int numberOfTrueVars){
		int relationNumber = 0;
		for (int i = 0; i < (1<<rank); i++) {
			//RelationCore.ones(i, 3) a truth table row can have up to 5 columns
			//RelationCore.ones(i, 3) will count ones up to the 8th column
			if(RelationCore.ones(i, 3)==numberOfTrueVars) 
				relationNumber|=(1<<i);
		}
		return relationNumber;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(""+(-3&0xFFFFFFFF));
		System.out.println(""+(-3&0xFFFFFFFF));
		System.out.println(xTrueVars(3, 0));
		System.out.println(xTrueVars(3, 1));
		System.out.println(xTrueVars(3, 2));
		System.out.println(xTrueVars(3, 3));
		int[] all = {0,1,2};
		System.out.println(nMap(xTrueVars(3, 1),3,all));
		System.out.println(and(3,all));
		System.out.println(or(3,all));
	}

}
