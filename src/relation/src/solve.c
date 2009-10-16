#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <time.h>
#include <math.h>

#include "solve.h"
#include "relation.h" /* relation numbers, etc.*/
#include "relation_consts.h"
#include "poly.h"

static solution *
solve_iterate(const problem * restrict p, solution *s);

static void *
__solve_helper(void *t_d);

static solution *
__solve_iterate(const problem * restrict p, solution *s,
                uint32_t start, uint32_t end);

/* For threaded iterate. */
typedef struct __thread_data {
    problem *p;
    uint32_t start;
    uint32_t end;
} thread_data;

/* Code related to the "object" layer is in a different file.
 * It is not linked for performance reasons. */
#include "solve_objects.c"

/* Returns the number of satisfied clauses. */
int
fsat(const problem * restrict problem, const solution * restrict solution) {
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

/* assignment is the row number 0-indexed == binary value.
 * e.g. rank 3
 * 000 -> 0, 001 -> 1, 010 -> 2, ..., 111 -> 7
 */
int
solve_value(const clause * restrict clause, int assignment) {
    assert(clause != NULL);
    assert(assignment >= 0);
    assert(assignment < (1 << clause->rank));
    /* The assignment is represented as a binary number whose length is the
     * rank. So, the assignment number represents a row contained by the
     * relation numbers. Thus, to check if the relation number is satisfied
     * is the same as checking if the row is represented in the relation
     * number.
     *
     * A binary (or logical) AND of the number that represents the row and the
     * relation number should not be 0 if the clause is satisfied.
     */
    return (clause->rn & (1 << assignment)) != 0;
}

inline int
to_row_number(const int * restrict values, int rank) {
    register int i;
    register int ret = 0;

    assert(values != NULL);

    for(i = 0; i < rank; i++) {
        ret += (1 << i) * (*(values+i) ? TRUE : FALSE);
    }

    return ret;
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
    /* FIXME filter unused variables */
    return solve_iterate(p, s);
}


/** FIXME TEST ME
 * @param p The problem
 * @return The occurences of variables.
 */
int *
variable_count(const problem * restrict p) {
    int *var_count;
    int i;
    int j;
    clause *c;

    assert(p != NULL);

    if ((var_count = malloc(sizeof(int) * p->num_vars)) == NULL)
        return NULL;

    /* Initialize all to 0. */
    memset(var_count, 0, sizeof(int) * p->num_vars);

    for (i = 0; i < p->num_clauses; i++) {
        c = p->clauses + i;
        for (j = 0; j < c->rank; j++) {
            *(var_count + *(c->vars+j)) += 1;
        }
    }

    return var_count;
}

solution *
solve_iterate(const problem * restrict p, solution *s) {
#define NTHREADS 16
    pthread_t tids[NTHREADS];
    pthread_attr_t attrs[NTHREADS];
    solution *x;
    solution *best;
    int max;
    int clauses_satisfied;
    int i;
    int rc;
    uint32_t search_space;
    thread_data tds[NTHREADS];
    problem p_copy;

    assert(p != NULL);
    assert(s != NULL);

    search_space = (1 << p->num_vars);

    /* soft copy */
    p_copy.num_vars = p->num_vars;
    p_copy.num_clauses = p->num_clauses;
    p_copy.vars = p->vars;
    p_copy.clauses = p->clauses;

    for (i = 0; i < NTHREADS; i++) {
        pthread_attr_init(attrs+i);
        pthread_attr_setdetachstate(attrs+i, PTHREAD_CREATE_JOINABLE);

        /* Create thread data. Copy the problem and give out the range. */
        tds[i].p = &p_copy;
        tds[i].start = (search_space / NTHREADS) * i;
        tds[i].end   = (search_space / NTHREADS) * (i + 1);

        if ((rc = pthread_create(tids+i, attrs+i,
                                 __solve_helper, (void *)(tds+i))) != 0) {
            printf("ERROR return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    max = -1;
    best = NULL;

    for (i = 0; i < NTHREADS; i++) {
        /* Wait for the threads */
        if ((rc = pthread_join(tids[i], (void *)&x) != 0)) {
            printf("ERROR return code from pthread_join() is %d\n", rc);
            exit(-1);
        }
        pthread_attr_destroy(attrs+i);

        /* Keep track of the best one. */
        if ((clauses_satisfied = fsat(p, x)) > max) {
            if (best != NULL) {
                solution_delete(best);
            }
            max = clauses_satisfied;
            best = x;
        }
        else {
            solution_delete(x);
        }
    }

    memcpy(s->values, best->values, sizeof(int) * s->size);
    solution_delete(best);

    return s;
}

/**
 * Helper method for solve_iterate. Calls __solve_iterate on the
 * range provided by the thread_data.
 * @param t_d The thread data.
 * @return The solution for the given range.
 */
static void *
__solve_helper(void *t_d) {
    thread_data *t;
    solution *s;

    assert(t_d != NULL);

    t = (thread_data *)t_d;

    /* Create space for the solution for this thread. */
    s = solution_create(t->p);
    /* The actual work. */
    __solve_iterate(t->p, s, t->start, t->end);

    pthread_exit((void *) s);

    /* NOT REACHED. */
    return (void *) s;
}

/**
 * @param p The problem.
 * @param s The solution.
 * @param start The starting assignment (inclusive).
 * @param end The ending assignment (not inclusive).
 * @return The best solution for the given range.
 */
#define OPTIMIZE 1
static solution *
__solve_iterate(const problem * restrict p, solution *s,
                 uint32_t start, uint32_t end) {
    register uint32_t bit_mask;
    register uint32_t i;
    register uint32_t k;
    register int max;
    register int temp;
    solution *max_solution;
#if OPTIMIZE
    register int num_ones;
    register int num_zeros;
    register int max_ones;
    register int max_zeros;
    double max_p;
#endif

    assert(p != NULL);
    assert(s != NULL);

#if OPTIMIZE
    max_p = find_maximum_point(p->clauses->rn, 3);
    max_ones = max_p * p->num_vars;
    max_zeros = p->num_vars - max_ones;

    max_ones += 3;
    max_zeros += 3;
#endif

    max = -1;
    max_solution = solution_create(p);


    for (i = start; i < end; i++) {
#if OPTIMIZE
    #if __GNUC__
        num_ones = __builtin_popcountl(i);
    #else
        num_ones = ones(i, 5); /* 5 as in 2^5 == 32 */
    #endif
        num_zeros = p->num_vars - num_ones;
        /* Depending on the number of ones and zeros,
         * skip certain assignments. */
        if (num_ones > max_ones || num_zeros > max_zeros)
            continue;
#endif
        bit_mask = 0x00000001;
        for (k = 0; k < s->size; k++) {
            *(s->values+k) = i & bit_mask ? TRUE : FALSE;
            bit_mask <<= 1;
        }

        /* Keep track of the maximum. */
        temp = fsat(p, s);
        if (temp > max) {
            max = temp;
            memcpy(max_solution->values, s->values, sizeof(int) * s->size);
        }

        if (max == p->num_clauses)
            break;
    }

    /* Copy the correct solution. */
    memcpy(s->values, max_solution->values, sizeof(int) * s->size);
    solution_delete(max_solution);

    return s;
}
