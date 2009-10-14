#ifndef SOLVE_H_
#define SOLVE_H_

#include <stdint.h>

#define TRUE 1
#define FALSE 0
#define UNKNOWN -1
#define IS_TRUE(x) ((x) == TRUE)
#define IS_FALSE(x) ((x) == FALSE)
#define IS_UNKNOWN(x) ((x) == UNKNOWN)

/**
 * CLAUSE_GET_VAR : clause *, solution *, int
 *     returns the value of the n-th variable of the clause
 *     according to the solution.
 */
#define CLAUSE_GET_VAR(c, s, n) *((*(s)).values + ((*(c)).vars)[n])

/* Data defs */

/**
 * The index of the value is corresponds to the index of the list of
 * variables.
 */
typedef struct __solution {
    /** The values corresponding to the variables in a problem. */
    int *values;
    /** The number of values. */
    int size;
} solution;

/**
 * Represents a clause in a CNF.
 */
typedef struct __clause {
    /** The relation number of the clause */
    uint32_t rn;
    /** The rank (the size) of the relation number */
    int rank;
    /** The variables in the clause. Indirectly references a problem. */
    int *vars;
} clause;

/**
 * Represents a MAX-CSP problem.
 */
typedef struct __problem {
    /** The list of variables in the problem. */
    char **vars;
    /** The number of variables in the problem. */
    int num_vars;
    /** The clauses in the problem */
    clause *clauses;
    /** The number of clauses in the problem. */
    int num_clauses;
} problem;

/* Constructor, Setters, Destructors */

/**
 * Create a problem.
 * @param vars The list of variables.
 * @param num_vars The number of variables.
 * @param clauses The clauses.
 * @param num_clauses The number of clauses.
 * @return The problem instance created from the given.
 */
problem *
problem_create(char **vars, int num_vars, clause *clauses, int num_clauses);

/**
 * Set values in a problem.
 */
void
problem_set(problem *problem, char **vars, int num_vars, clause *clauses,
            int num_clauses);

/**
 * Free a problem.
 * @param problem The problem to delete.
 */
void
problem_delete(problem *problem);

/**
 * Create a solution in reference to a problem.
 */
solution *
solution_create(const problem * restrict problem);

/**
 * Delete a solution.
 */
void
solution_delete(solution *solution);

/**
 * Create a clause.
 */
clause *
clause_create(uint32_t rn, int rank, int *vars);

/**
 * Set the values of a clause already in memory (or stack).
 * If vars == NULL, vars is not changed.
 */
void
clause_set(clause *clause, uint32_t rn, int rank, int *vars);

/**
 * Delete a clause.
 */
void
clause_delete(clause *clause);

/* More useful methods. */

/**
 * Calculate the number of satisfied clauses.
 */
int
fsat(const problem * restrict problem, const solution * restrict solution);

/**
 * @return true if the clause is satisfied by the clause.
 */
int
clause_is_satisfied(const clause * restrict c, const solution * restrict s);

/**
 * Tries to solve the problem and puts the answer in solution. Which should
 * have been created by solution_create.
 */
solution *
solve(const problem * restrict problem, solution *solution);

/**
 * Return true or false based on the assignment.
 * @param clause The clause.
 * @param assignment The assignment for the clause.
 * @return The result of the assignment, either TRUE or FALSE.
 */
int
solve_value(const clause * restrict clause, int assignment);

/**
 * Convert the values into the row number.
 * @param values The list of values.
 * @param rank The number of values.
 * @return The row number corresponding to the assignment.
 */
int
to_row_number(const int * restrict values, int rank);

#endif
