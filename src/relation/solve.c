#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <time.h>
#include <math.h>

#include "solve.h"
#include "_relation.h" /* relation numbers, etc.*/
#include "relation_consts.h"

#define RAND2 (rand() % 2)

static void *
solve_it(void *problem);

static solution *
random_solve(const problem * restrict problem, solution *solution);

static solution *
solve_naive(const problem * restrict p, solution *s, int loopy);

static solution *
solve_naive_threaded(const problem * restrict p, solution *s, int num_threads);

/* START constructor/destructor */

problem *
problem_create(char **vars, int num_vars, clause *clauses, int num_clauses) {
    problem *p;
    int i;

    assert(num_vars > 0);
    assert(num_clauses > 0);
    assert(vars != NULL);
    assert(clauses != NULL);

    p = malloc(sizeof(problem));
    if (p == NULL) {
        perror("malloc");
        return NULL;
    }

    p->vars = malloc(num_vars);

    for (i = 0; i < num_vars; i++) {
        *(p->vars+i) = malloc(strlen(*(vars+i)) + 1);
        strcpy(*(p->vars+i), *(vars+i));
    }

    p->clauses = malloc(sizeof(clause) * num_clauses);
    memcpy(p->clauses, clauses, sizeof(clause) * num_clauses);

    return p;
}

/* Set values for a problem. */
void
problem_set(problem *problem, char **vars, int num_vars, clause *clauses,
            int num_clauses) {
    assert(problem != NULL);
    problem->vars = vars;
    problem->num_vars = num_vars;
    problem->clauses = clauses;
    problem->num_clauses = num_clauses;
}

void
problem_delete(problem *p) {
    int i;

    for (i = 0; i < p->num_vars; i++) {
        free(*(p->vars+i));
    }

    free(p->vars);
    free(p->clauses);
    free(p);
}

solution *
solution_create(const problem * restrict problem) {
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

    memset(s->values, '\0', sizeof(int) * problem->num_vars);

    return s;
}

void
solution_delete(solution *s) {
    free(s->values);
    free(s);
}

clause *
clause_create(uint32_t rn, int rank, int *vars) {
    clause *c;
    register size_t size;

    assert(vars != NULL);

    size = sizeof(int) * rank;

    c = malloc(sizeof(clause));
    c->rn = rn;
    c->rank = rank;
    c->vars = malloc(size);
    memcpy(c->vars, vars, size);

    return c;
}

void
clause_set(clause *clause, uint32_t rn, int rank, int *vars) {
    register size_t size;
    assert(clause != NULL);

    size = sizeof(int) * rank;

    clause->rn = rn;
    clause->rank = rank;

    /* Short-circuit if vars is not going to change. */
    if (vars == NULL)
        return;

    if ((clause->vars = malloc(size)) == NULL) {
        perror("malloc");
        return;
    }
    memcpy(clause->vars, vars, size);
}

void
clause_delete(clause *clause) {
    assert(clause != NULL);
    free(clause->vars);
    free(clause);
}

/* END constructor/destructor */

/* Returns the number of satisfied clauses. */
int
fsat(const problem * restrict problem, solution *solution) {
    int i;
    int count = 0;

    assert(problem != NULL);
    assert(solution != NULL);

    /* for c in clauses */
    for (i = 0; i < problem->num_clauses; i++) {
        /* copy the values from the clause */
        if (clause_is_satisfied(problem->clauses+i, solution)) {
            count++;
        }
    }
    return count;
}

int
clause_is_satisfied(const clause * restrict c, const solution * restrict s) {
    int i;
    int t_v[MAX_RANK];

    assert(c != NULL);
    assert(s != NULL);

    /* copy the values from the clause */
    for (i = 0; i < c->rank; i++) {
        t_v[i] = CLAUSE_GET_VAR(c, s, i);
    }

    return solve_value(c, to_row_number(t_v, c->rank));
}

/* The version below should be absolutely correct as its algorithm derives
 * from the reduce method in the edu.neu.ccs.evergreen package (Java). */
#if 0
int
clause_is_satisfied_v0(const clause * restrict c, const solution * restrict s) {
    int i;
    uint32_t rn_temp;

    assert(c != NULL);
    assert(s != NULL);

    /* 0 is not valid. */
    if (c->rn == 0) {
        return 0;
    }

    rn_temp = c->rn;

    for (i = 0; i < c->rank; i++) {
        rn_temp = reduce(rn_temp, c->rank, i, *(s->values+*(c->vars+i)));

        if (rn_temp == get_mask(c->rank)) {
            return 1;
        }
    }

    return 0;
}
#endif

solution *
solve(const problem * restrict p, solution *s) {
    return solve_naive(p, s, 5000000);
    /* Proof of concept threaded solver thing. Slower than the above.
    return solve_naive_threaded(p, s, 2);
     */
}

static solution *
solve_naive_threaded(const problem * restrict p, solution *s, int num_threads) {
    int rc;
    int i;
    int clauses_satisfied;
    int max;
    pthread_t *tids;
    pthread_attr_t *attrs;
    solution *x;
    solution *best;

    assert(p != NULL);
    assert(s != NULL);
    assert(num_threads > 1);
    assert(num_threads <= 8);

    if ((tids = malloc(sizeof(pthread_t) * num_threads)) == NULL) {
        perror("malloc");
        return NULL;
    }

    if ((attrs = malloc(sizeof(pthread_attr_t) * num_threads)) == NULL) {
        perror("malloc");
        free(tids);
        return NULL;
    }

    for (i = 0; i < num_threads; i++) {
        pthread_attr_init(attrs+i);
        pthread_attr_setdetachstate(attrs+i, PTHREAD_CREATE_JOINABLE);
        
        if ((rc = pthread_create(tids+i, attrs+i, solve_it, (void *)p)) != 0) {
            printf("ERROR return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    /* Keep track of the best solution. */
    max = 0;
    best = NULL;

    for (i = 0; i < num_threads; i++) {
        /* Wait for the threads */
        if ((rc = pthread_join(tids[i], (void *) &x) != 0)) {
            printf("ERROR return code from pthread_join() is %d\n", rc);
            exit(-1);
        }
        pthread_attr_destroy(attrs+i);
        clauses_satisfied = fsat(p, x);
        if (clauses_satisfied > max) {
            if (best != NULL) {
                solution_delete(best);
            }
            best = x;
        }
    }

    free(tids);
    free(attrs);

    memcpy(s->values, best->values, sizeof(int) * s->size);
    
/* XXX   pthread_exit((void *) best); */

    return best;
}

static void *
solve_it(void *p) {
    solution *s;

    assert(p != NULL);
    
    s = solution_create((problem *) p);
    solve_naive((problem *)p, s, 2500000);

    pthread_exit((void *) s);
    return NULL; /* Not reached? */
}

/* The naive solver. */
static solution *
solve_naive(const problem * restrict p, solution *s, int loopy) {
    int i;
    int max;
    int temp;
    size_t size;
    solution *final;

    assert(loopy > 0);
    
    /* Seed the random number generator. */
    srand(time(NULL));

    assert(s != NULL);

    max = 0;
    size = sizeof(int) * s->size;

    if ((final = solution_create(p)) == NULL) {
        perror("malloc");
        return NULL;
    }

    for (i = 0; i < loopy; i++) {
        random_solve(p, s);
        temp = fsat(p, s);
        if (temp > max) {
            max = temp;
            memcpy(final->values, s->values, size);
        }
    }

    memcpy(s->values, final->values, size);
    solution_delete(final);

    return s;
}

static solution *
random_solve(const problem * restrict problem, solution *solution) {
    int i;

    assert(problem != NULL);
    assert(solution != NULL);

    for (i = 0; i < problem->num_vars; i++) {
        *(solution->values+i) = RAND2;
    }

    return solution;
}

/* assignment is the row number 0-indexed == binary value.
 * e.g. rank 3
 * 000 -> 0, 001 -> 1, 010 -> 2, ..., 111 -> 7
 */
int
solve_value(const clause * restrict clause, int assignment) {
    assert(clause != NULL);
    assert(assignment >= 0);
    assert(assignment < (1 << clause->rank));
    return (clause->rn & (1 << assignment)) > 0;
}

int
to_row_number(int *values, int rank) {
    int i;
    int ret = 0;

    assert(values != NULL);

    for(i = 0; i < rank; i++) {
        ret += (1 << i) * (*(values+i) ? 1 : 0);
    }

    return ret;
}
