#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "minunit.h"
#include "solve.h"

int tests_run = 0;

static char *test_solve() {
    problem *p;
    solution *s;

    p = malloc(sizeof(problem));
    p->vars = malloc(sizeof(char *) * 5);
    *(p->vars+0) = malloc(sizeof(char) * 10);
    sprintf(*(p->vars+0), "v1");
    *(p->vars+1) = malloc(sizeof(char) * 10);
    sprintf(*(p->vars+1), "v2");
    *(p->vars+2) = malloc(sizeof(char) * 10);
    sprintf(*(p->vars+2), "v3");
    *(p->vars+3) = malloc(sizeof(char) * 10);
    sprintf(*(p->vars+3), "v4");
    *(p->vars+4) = malloc(sizeof(char) * 10);
    sprintf(*(p->vars+4), "v5");
    p->num_vars = 5;
    p->clauses = malloc(sizeof(clause) * 4);
    /*
    *(p->clauses+1) = malloc(sizeof(clause));
    *(p->clauses+2) = malloc(sizeof(clause));
    *(p->clauses+3) = malloc(sizeof(clause));
     */
    (p->clauses+0)->rn = 22;
    (p->clauses+0)->rank = 3;
    (p->clauses+0)->vars = malloc(sizeof(int) * 3);
    *((p->clauses+0)->vars+0) = 0;
    *((p->clauses+0)->vars+1) = 1;
    *((p->clauses+0)->vars+2) = 2;

    p->num_clauses = 1;

    s = solution_create(p);
    solve(p, s);

    solution_delete(s);
    problem_delete(p);

    return NULL;
}


static char *test_solution_get() {
    solution s;
    s.values = malloc(sizeof(int) * 5);
    s.size = 5;

    *(s.values+0) = 2;
    *(s.values+1) = 3;
    *(s.values+2) = 5;
    *(s.values+3) = 7;
    *(s.values+4) = 11;

    mu_assert("Checking index 0", (solution_get(&s, 0) == 2));
    mu_assert("Checking index 1", (solution_get(&s, 1) == 3));
    mu_assert("Checking index 2", (solution_get(&s, 2) == 5));
    mu_assert("Checking index 3", (solution_get(&s, 3) == 7));
    mu_assert("Checking index 4", (solution_get(&s, 4) == 11));
    free(s.values);

    return NULL;
}


static char *test_solve_value() {
    clause c;
    c.rn = 22;
    c.rank = 3;
    c.vars = NULL;

    mu_assert("RN 22, assign 0 (000)",
              solve_value(&c, 0) == 0);
    mu_assert("RN 22, assign 1 (001)",
              solve_value(&c, 1) == 1);
    mu_assert("RN 22, assign 2 (010)",
              solve_value(&c, 2) == 1);
    mu_assert("RN 22, assign 3 (011)",
              solve_value(&c, 3) == 0);
    mu_assert("RN 22, assign 4 (100)",
              solve_value(&c, 4) == 1);
    mu_assert("RN 22, assign 5 (101)",
              solve_value(&c, 5) == 0);
    mu_assert("RN 22, assign 6 (110)",
              solve_value(&c, 6) == 0);
    mu_assert("RN 22, assign 7 (111)",
              solve_value(&c, 7) == 0);

    c.rn = 60;
    c.rank = 3;
    
    mu_assert("RN 60, assign 0 (000)",
              solve_value(&c, 0) == 0);
    mu_assert("RN 60, assign 1 (001)",
              solve_value(&c, 1) == 0);
    mu_assert("RN 60, assign 2 (010)",
              solve_value(&c, 2) == 1);
    mu_assert("RN 60, assign 3 (011)",
              solve_value(&c, 3) == 1);
    mu_assert("RN 60, assign 4 (100)",
              solve_value(&c, 4) == 1);
    mu_assert("RN 60, assign 5 (101)",
              solve_value(&c, 5) == 1);
    mu_assert("RN 60, assign 6 (110)",
              solve_value(&c, 6) == 0);
    mu_assert("RN 60, assign 7 (111)",
              solve_value(&c, 7) == 0);

    c.rn = 1;
    c.rank = 3;
    
    mu_assert("RN 1, assign 0 (000)",
              solve_value(&c, 0) == 1);

    return NULL;
}


static char *all_tests() {
    mu_run_test(test_solve_value);
    mu_run_test(test_solution_get);
    mu_run_test(test_solve);
    return NULL;
}

int main(int argc, char **argv) {
    char *result = all_tests();

    if (result != 0) {
        printf("%s\n", result);
    }
    else {
        printf("ALL TESTS PASSED\n");
    }
    printf("Tests run: %d\n", tests_run);

    return result != 0;
}
