#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "minunit.h"
#include "solve.h"

int tests_run = 0;
int asserts = 0;

static char *test_solve() {
    /* XXX much more sane to test in Python */
    return NULL;
}

static char *test_solve_value() {
    clause c;
    c.rn = 22;
    c.rank = 3;
    c.vars = NULL;

    mu_assert("RN 22, assign 0 (000)", solve_value(&c, 0) == 0);
    mu_assert("RN 22, assign 1 (001)", solve_value(&c, 1) == 1);
    mu_assert("RN 22, assign 2 (010)", solve_value(&c, 2) == 1);
    mu_assert("RN 22, assign 3 (011)", solve_value(&c, 3) == 0);
    mu_assert("RN 22, assign 4 (100)", solve_value(&c, 4) == 1);
    mu_assert("RN 22, assign 5 (101)", solve_value(&c, 5) == 0);
    mu_assert("RN 22, assign 6 (110)", solve_value(&c, 6) == 0);
    mu_assert("RN 22, assign 7 (111)", solve_value(&c, 7) == 0);

    c.rn = 60;
    c.rank = 3;
    
    mu_assert("RN 60, assign 0 (000)", solve_value(&c, 0) == 0);
    mu_assert("RN 60, assign 1 (001)", solve_value(&c, 1) == 0);
    mu_assert("RN 60, assign 2 (010)", solve_value(&c, 2) == 1);
    mu_assert("RN 60, assign 3 (011)", solve_value(&c, 3) == 1);
    mu_assert("RN 60, assign 4 (100)", solve_value(&c, 4) == 1);
    mu_assert("RN 60, assign 5 (101)", solve_value(&c, 5) == 1);
    mu_assert("RN 60, assign 6 (110)", solve_value(&c, 6) == 0);
    mu_assert("RN 60, assign 7 (111)", solve_value(&c, 7) == 0);

    c.rn = 1;
    c.rank = 3;
    
    mu_assert("RN 1, assign 0 (000)", solve_value(&c, 0) == 1);
    mu_assert("RN 1, assign 1 (001)", solve_value(&c, 1) == 0);
    mu_assert("RN 1, assign 2 (010)", solve_value(&c, 2) == 0);
    mu_assert("RN 1, assign 3 (011)", solve_value(&c, 3) == 0);
    mu_assert("RN 1, assign 4 (100)", solve_value(&c, 4) == 0);
    mu_assert("RN 1, assign 5 (101)", solve_value(&c, 5) == 0);
    mu_assert("RN 1, assign 6 (110)", solve_value(&c, 6) == 0);
    mu_assert("RN 1, assign 7 (111)", solve_value(&c, 7) == 0);

    return NULL;
}

static char *test_clause_is_satisfied() {
    clause *c;
    solution *s;

    int vars[3] = { 0, 1, 2 };
    c = clause_create(22, 3, vars);
    s = malloc(sizeof(solution));
    s->values = malloc(sizeof(int) * 3);
    *(s->values+0) = TRUE;
    *(s->values+1) = FALSE;
    *(s->values+2) = FALSE;
    /* Relation Number is 22 (1in3) */
    mu_assert("(22 v0 v1 v2) with ((v0 true) (v1 false) (v2 false) ...)",
              clause_is_satisfied(c, s));
    *(s->values+0) = TRUE;
    *(s->values+1) = TRUE;
    *(s->values+2) = FALSE;
    mu_assert("(22 v0 v1 v2) with ((v0 true) (v1 true) (v2 false) ...)",
              !clause_is_satisfied(c, s));

    /* Relation Number is 2 (first variable is true in 3) */
    c->rn = 2;
    *(s->values+0) = TRUE;
    *(s->values+1) = FALSE;
    *(s->values+2) = FALSE;
    mu_assert("(2 v0 v1 v2) with ((v0 true) (v1 false) (v2 false) ...)",
              clause_is_satisfied(c, s));
    *(s->values+0) = FALSE;
    *(s->values+1) = TRUE;
    *(s->values+2) = FALSE;
    mu_assert("(2 v0 v1 v2) with ((v0 false) (v1 true) (v2 false) ...)",
              !clause_is_satisfied(c, s));
    
    /* Relation Number is 16 (last variable is true in 3) */
    c->rn = 16;
    *(s->values+0) = TRUE;
    *(s->values+1) = TRUE;
    *(s->values+2) = FALSE;
    mu_assert("(8 v0 v1 v2) with ((v0 true) (v1 true) (v2 false) ...)",
              !clause_is_satisfied(c, s));
    *(s->values+0) = FALSE;
    *(s->values+1) = FALSE;
    *(s->values+2) = TRUE;
    mu_assert("(8 v0 v1 v2) with ((v0 false) (v1 false) (v2 true) ...)",
              clause_is_satisfied(c, s));

    clause_delete(c);
    solution_delete(s);
    
    return NULL;
}

char *all_tests() {
    mu_run_test(test_solve_value);
    mu_run_test(test_solve);
    mu_run_test(test_clause_is_satisfied);
    return NULL;
}
