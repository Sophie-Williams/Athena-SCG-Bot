#ifndef SOLVE_H_
#define SOLVE_H_

#include <stdint.h>

#define TRUE 1
#define FALSE 0
#define UNKNOWN -1
#define IS_TRUE(x) ((x) == TRUE)
#define IS_FALSE(x) ((x) == FALSE)
#define IS_UNKNOWN(x) ((x) == UNKNOWN)

/* CLAUSE_GET_VAR : clause *, solution *, int
 *     returns the value of the n-th variable of the clause
 *     according to the solution. */
#define CLAUSE_GET_VAR(c, s, n) *((*(s)).values + ((*(c)).vars)[n])

/* Data defs */

/* The index of the value is corresponds to the index of the list of
 * variables.
 */
typedef struct __solution {
    int *values;
    int size;
} solution;

typedef struct __clause {
    uint32_t rn;
    int rank;
    int *vars;
} clause;

typedef struct __problem {
    char **vars;
    int num_vars;
    clause *clauses;
    int num_clauses;
} problem;

/* Constructor, Setters, Destructors */

problem *
problem_create(char **vars, int num_vars, clause *clauses, int num_clauses);

void
problem_set(problem *problem, char **vars, int num_vars, clause *clauses,
            int num_clauses);

void
problem_delete(problem *problem);

solution *
solution_create(const problem * restrict problem);

void
solution_delete(solution *solution);

clause *
clause_create(uint32_t rn, int rank, int *vars);

/* Set the values of a clause already in memory (or stack).
 * If vars == NULL, vars is not changed.
 */
void
clause_set(clause *clause, uint32_t rn, int rank, int *vars);

void
clause_delete(clause *clause);

/* More useful methods. */

/* Returns the number of satisfied clauses. */
int
fsat(const problem * restrict problem, solution *solution);

/* Returns true if the clause is satisfied by the clause.  */
int
clause_is_satisfied(const clause * restrict c, const solution * restrict s);

/* Tries to solve the problem and puts the answer in solution. Which should
 * have been created by solution_create. */
solution *
solve(const problem * restrict problem, solution *solution);

/* Return true or false based on the assignment. */
int
solve_value(const clause * restrict clause, int assignment);

/* Convert the values into the row number. */
int
to_row_number(int *values, int rank);

#endif
