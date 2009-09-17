package edu.neu.ccs.evergreen.ir;

/**
 * @author mohsen
 * An implementation of RelationI interface that wraps the relation core functionality
 */
public class Relation implements RelationI{

    private int rank;
    private int relationNumber;
    
    
    /**
     * @param rank
     * @param relationNumber
     */
    public Relation(int rank, int relationNumber) {
        RelationCore.checkRank(rank);
        RelationCore.checkRelationNumber(relationNumber, rank);
        this.rank = rank;
        this.relationNumber = relationNumber;
    }

    public int firstForcedVariable(int startPosition) throws IllegalArgumentException {
        return RelationCore.firstForcedVariable(relationNumber, rank, startPosition);
    }

    public int getMagicNumber(int variablePosition, int value) throws IllegalArgumentException {
        return RelationCore.getMagicNumber(rank, variablePosition, value);
    }

    public int getMask() {
        return RelationCore.getMask(rank);
    }

    public int getRank() {
        return rank;
    }

    public int getRelationNumber() {
        return relationNumber;
    }

    public int isForced(int variablePosition) throws IllegalArgumentException {
        return RelationCore.isForced(relationNumber, rank, variablePosition);
    }

    public boolean isIrrelevant(int variablePosition) throws IllegalArgumentException {
        return RelationCore.isIrrelevant(relationNumber, rank, variablePosition);
    }

    public int nMap(int variablePosition) throws IllegalArgumentException {
        return RelationCore.nMap(relationNumber, rank, variablePosition);
    }

    public int numberOfRelevantVariables() {
        return RelationCore.numberOfRelevantVariables(relationNumber, rank);
    }

    public int ones() {
        return RelationCore.ones(relationNumber, rank);
    }

    public int q(int s) throws IllegalArgumentException {
        return RelationCore.q(relationNumber, rank, s);
    }

    public int reduce(int variablePosition, int value) throws IllegalArgumentException {
        return RelationCore.reduce(relationNumber, rank, variablePosition, value);
    }

    public int renme(int permutationSemantics, int... permutation) throws IllegalArgumentException {
        return RelationCore.renme(relationNumber, rank, permutationSemantics, permutation);
    }

    public void setRank(int rank) throws IllegalArgumentException {
        RelationCore.checkRank(rank);
        //this.rank = rank;
        throw new UnsupportedOperationException();
    }

    public void setRelationNumber(int relationNumber) throws IllegalArgumentException {
        RelationCore.checkRelationNumber(relationNumber, rank);
        this.relationNumber = relationNumber;
    }

    public int swap(int variablePosition1, int variablePosition2) throws IllegalArgumentException {
        return RelationCore.swap(relationNumber, rank, variablePosition1, variablePosition2);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RelationI){
            RelationI r = (RelationI) obj;
            return(r.getRelationNumber() == relationNumber);
        }
        return false;
    }

    @Override
    public String toString() {
        return ""+relationNumber;
    }

    
}