package edu.neu.ccs.evergreen.ir;
/**
 * @author mohsen
 * The RelationFactory class provides a set of methods for creating RelationI objects according to certain criteria.
 * Basically wraps the functionality in ReationNumberUtil and adds new functionality of bitwise operations on RelationI objects
 * Note that the created relation numbers can be manipulated using bitwise operations
 * provided that they share the same variables and same variable ordering
 * Variable positions are 0 based
 * for example:
 * int x = 0;
 * int y = 1;
 * int z = 2;
 * int w = 3;
 * RelationI r_or_xy = RelationFactory.or(4,x,y)
 * RelationI r_or_zw =  RelationFactory.or(4,z,w)
 * RelationI r_or_xyzw = RelationFactory.or(r_or_xy,r_or_zw)
 * 
 * The relation provides:
 *          or(arbitrary number of variables,
 *       and(arbitrary number of variables,
 *       imply (two vars)
 *       equiv (two vars)
 *       xor (two vars)
 *       
 */
public class RelationFactory {
    /**
     * Given a set of variable positions, return a relation that is the or of these variables
     * @param rank the rank of the created relation
     * @param variablePositions an arbitrary number of variable positions
     * @return a relationI object that represents the oring of the variables at the given positions
     */
    public static RelationI or(int rank, int ... variablePositions){
        return new Relation(rank,RelationNumberUtil.or(rank, variablePositions));
    }

    /**
     * Given a set of variable positions, return a relation that is the anding of these variables
     * @param rank the rank of the created relation
     * @param variablePositions an arbitrary number of variable positions
     * @return a RelationI object that represents the anding of the variables at the given positions
     */
    public static RelationI and(int rank, int ... variablePositions){
        return new Relation(rank,RelationNumberUtil.and(rank, variablePositions));
    }

    /**
     * nMaps a number of variables in a certain relation
     * @param relationNumber  
     * @param rank the rank of the created relation
     * @param variablePositions an arbitrary number of variable positions
     * @return a RelationI object that represents the given relation with the given set of variables renamed
     */
    public static RelationI nMap(int relationNumber,int rank, int ... variablePositions){
        return new Relation(rank,RelationNumberUtil.nMap(relationNumber,rank, variablePositions));
    }
    
    /**
     * nMaps a number of variables in a certain relation
     * @param relation
     * @param variablePositions an arbitrary number of variable positions
     * @return a RelationI object that represents the given relation with the given set of variables renamed
     */
    public static RelationI nMap(RelationI relation, int ... variablePositions){
        return new Relation(relation.getRank(),RelationNumberUtil.nMap(relation.getRelationNumber(),relation.getRank(), variablePositions));
    }
    
    /**
     * Given two variables x,y returns a relation that represents x==>y
     * @param rank the rank of the created relation
     * @param variablePositions two variable positions
     * @return a RelationI object that represents the x=>y where x,y are the given two variable positions
     */
    public static RelationI imply(int rank, int ... variablePositions){
        return new Relation(rank,RelationNumberUtil.imply(rank, variablePositions));
    }
    
    /**
     * Given two variables x,y returns a relation that represents x=y
     * @param rank the rank of the created relation
     * @param variablePositions two variable positions
     * @return a RelationI object that represents the x=y where x,y are the given two variable positions
     */
    public static RelationI equiv(int rank, int ... variablePositions){
        return new Relation(rank,RelationNumberUtil.equiv(rank, variablePositions));
    }
    
    /**
     * Given two variables x,y returns a relation that represents xor(x,y)
     * @param rank the rank of the created relation
     * @param variablePositions two variable positions
     * @return a RelationI object that represents the xor(x,y) where x,y are the given two variable positions
     */
    public static RelationI xor(int rank, int ... variablePositions){
        return new Relation(rank,RelationNumberUtil.xor(rank, variablePositions));
    }

    /**
     * for 1in3 use xTrueVars(3,1)
     * @param rank
     * @param numberOfTrueVars
     * @return a RelationI object representing the relation which is true only when the given number of vars is true
     */
    public static RelationI xTrueVars(int rank,int numberOfTrueVars){
        return new Relation(rank,RelationNumberUtil.xTrueVars(rank, numberOfTrueVars));
    }

    /**
     * @param relations
     * @return RelationI object whose relationNumber is the bitwise or of the given set of relations
     */
    public static RelationI or(RelationI ... relations){
        if(relations.length==0) throw new IllegalArgumentException("or Must have at least one argument");
        int rank = relations[0].getRank();
        int relationNumber = 0;
        for (RelationI relation : relations) {
            if(relation.getRank()!=rank) throw new IllegalArgumentException("All relations must be of the same rank");
            relationNumber|=relation.getRelationNumber();
        }
        return new Relation(rank,relationNumber);
    }
    
    /**
     * @param relations
     * @return RelationI object whose relationNumber is the bitwise and of the given set of relations
     */
    public static RelationI and(RelationI ... relations){
        if(relations.length==0) throw new IllegalArgumentException("and Must have at least one argument");
        int rank = relations[0].getRank();
        int relationNumber = RelationCore.getMask(rank);
        for (RelationI relation : relations) {
            if(relation.getRank()!=rank) throw new IllegalArgumentException("All relations must be of the same rank");
            relationNumber&=relation.getRelationNumber();
        }
        return new Relation(rank,relationNumber);
    }
    
    /**
     * @param relation
     * @return RelationI object whose relationNumber is the bitwise not of the given relation
     */
    public static RelationI not(RelationI relation){
        if(null==relation) throw new IllegalArgumentException("argument relation cannot be null");
        int r = relation.getRelationNumber();
        int rank = relation.getRank();
        int relationNumber = (~r)&RelationCore.getMask(rank);
        return new Relation(rank,relationNumber);        
    }

    /**
     * @param relation1
     * @param relation2
     * @return RelationI object whose relationNumber is the bitwise xor of the given two relations
     */
    public static RelationI xor(RelationI relation1,RelationI relation2){
        if((null==relation1)||(null==relation2)) throw new IllegalArgumentException("argument relation cannot be null");
        if(relation1.getRank()!=relation2.getRank()) throw new IllegalArgumentException("All relations must be of the same rank");
        int r1 = relation1.getRelationNumber();
        int r2 = relation2.getRelationNumber();
        int rank = relation1.getRank();
        int relationNumber = (r1^r2)&RelationCore.getMask(rank);
        return new Relation(rank,relationNumber);        
    }

    /**
     * @param relation1
     * @param relation2
     * @return RelationI object whose relationNumber is the bitwise equiv. of the given two relations
     */
    public static RelationI equiv(RelationI relation1,RelationI relation2){
        if((null==relation1)||(null==relation2)) throw new IllegalArgumentException("argument relation cannot be null");
        if(relation1.getRank()!=relation2.getRank()) throw new IllegalArgumentException("All relations must be of the same rank");
        int r1 = relation1.getRelationNumber();
        int r2 = relation2.getRelationNumber();
        int rank = relation1.getRank();
        int relationNumber = (~(r1^r2))&RelationCore.getMask(rank);
        return new Relation(rank,relationNumber);        
    }

    /**
     * @param relation1
     * @param relation2
     * @return RelationI object whose relationNumber is the bitwise implication of the given two relations
     */
    public static RelationI imply(RelationI relation1,RelationI relation2){
        if((null==relation1)||(null==relation2)) throw new IllegalArgumentException("argument relation cannot be null");
        if(relation1.getRank()!=relation2.getRank()) throw new IllegalArgumentException("All relations must be of the same rank");
        int r1 = relation1.getRelationNumber();
        int r2 = relation2.getRelationNumber();
        int rank = relation1.getRank();
        int relationNumber = (~r1|r2)&RelationCore.getMask(rank);
        return new Relation(rank,relationNumber);
    }    
    
    public static void main(String[] args){
        RelationI r1 = RelationFactory.xTrueVars(3, 1);
        RelationI r2 = RelationFactory.xTrueVars(3, 1);
        RelationI rand = RelationFactory.and(r1,r2);
        RelationI ror = RelationFactory.or(r1,r2);
        RelationI rxor = RelationFactory.xor(r1,r2);
        RelationI requiv = RelationFactory.equiv(r1,r2);
        RelationI rimply = RelationFactory.imply(r1,r2);
        RelationI rnot = RelationFactory.not(r1);
        RelationI rnmap = RelationFactory.nMap(r1, 0,1,2);
        RelationI rnmap2 = RelationFactory.nMap(r1.getRelationNumber(),r1.getRank(), 0,1,2);
        System.out.println("r1: "+r1);
        System.out.println("r2: "+r2);
        System.out.println("rand: "+rand);
        System.out.println("ror: "+ror);
        System.out.println("rxor: "+rxor);
        System.out.println("requiv: "+requiv);
        System.out.println("rimply: "+rimply);
        System.out.println("rnot: "+rnot);
        System.out.println("rnmap:"+rnmap);
        System.out.println("=============");
        
        RelationI andr = RelationFactory.and(3, 0,1,2);
        RelationI orr = RelationFactory.or(3, 0,1,2);
        RelationI implyr = RelationFactory.imply(3,2,1);
        RelationI equivr = RelationFactory.equiv(3,2,1);
        RelationI xorr = RelationFactory.xor(3,2,1);
        System.out.println("andr: "+andr);
        System.out.println("orr: "+orr);
        System.out.println("xorr: "+xorr);
        System.out.println("equivr: "+equivr);
        System.out.println("implyr: "+implyr);
        
    }
}
