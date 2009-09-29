#ifndef SOLVE_H_
#define SOLVE_H_

#include <stdint.h>

#define TRUE 1
#define FALSE 0
#define UNKNOWN -1
#define IS_TRUE(x) ((x) == TRUE)
#define IS_FALSE(x) ((x) == FALSE)
#define IS_UNKNOWN(x) ((x) == UNKNOWN)

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

problem *
problem_create(char **vars, int num_vars, clause *clauses, int num_clauses);

void
problem_set(problem *problem, char **vars, int num_vars, clause *clauses,
            int num_clauses);

void
problem_delete(problem *problem);

solution *
solution_create(problem *problem);

void
solution_delete(solution *solution);

clause *
clause_create(uint32_t rn, int rank, int *vars);

void
clause_set(clause *clause, uint32_t rn, int rank, int *vars);

void
clause_delete(clause *clause);

int
solution_get(solution *solution, int name);

int
fsat(problem *problem, solution *solution);

solution *
solve(problem *problem, solution *solution);

int
solve_value(clause *clause, int assignment);

int
to_value(int *values, int rank);

#endif
