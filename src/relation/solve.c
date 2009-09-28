#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <time.h>
#include <math.h>
#include "solve.h"

#define RAND2 (rand() % 2)

static solution *
random_solve(problem *problem, solution *solution);


solution *
solution_create(problem *problem) {
    solution *s;

    assert(problem != NULL);

    s = malloc(sizeof(solution));
    if (s == NULL) {
        perror("malloc");
        return NULL;
    }

    s->values = malloc(sizeof(int) * problem->num_vars);
    s->size = problem->num_vars;

    if (s->values == NULL) {
        perror("malloc");
        free(s);
        return NULL;
    }

    return s;
}

void
solution_delete(solution *s) {
    free(s->values);
    free(s);
}

int
solution_get(solution *solution, int name) {
    assert(solution != NULL);
    assert(name >= 0);
    assert(name < solution->size);

    return *(solution->values + name);
}

int
fsat(problem *problem, solution *solution) {
    int i, j;
    int count = 0;
    int t_v[5];
    clause *c;

    assert(problem != NULL);
    assert(solution != NULL);

    /* for c in clauses */
    for (i = 0; i < problem->num_clauses; i++) {
        c = *(problem->clauses + i);
        /*  */
        for (j = 0; j < c->rank; j++) {
            t_v[j] = *(solution->values + *(c->vars+j));
        }
        if (solve_value(c, to_value(t_v, c->rank))) {
            count++;
        }
    }
    return count;
}

solution *
solve(problem *p, solution *s) {
    return random_solve(p, s);
}

static solution *
random_solve(problem *problem, solution *solution) {
    int i;

    assert(problem != NULL);
    assert(solution != NULL);

    /* Seed the random number generator. */
    srand(time(NULL));

    for (i = 0; i < problem->num_vars; i++) {
        *(solution->values + i) = RAND2;
    }

    return solution;
}

/*
 * assignment is the row number 0-indexed == binary value.
 * e.g. rank 3
 * 000 -> 0, 001 -> 1, 010 -> 2, ..., 111 -> 7
 */
int
solve_value(clause *clause, int assignment) {
    assert(clause != NULL);
    assert(assignment >= 0);
    assert(assignment < (1 << clause->rank));
    return (clause->rn & (1 << assignment)) > 0;
}


int
to_value(int *values, int rank) {
    int i;
    int ret = 0;

    for(i = 0; i < rank; i++) {
        ret += (1 << i) * (*(values+i));
    }
    return ret;
}
