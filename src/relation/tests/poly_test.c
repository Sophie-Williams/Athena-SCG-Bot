#include <stdio.h>
#include <stdlib.h>
#include "minunit.h"
#include "poly.h"

int tests_run = 0;
int asserts = 0;

#define DELTA 0.001
#define IN_RANGE(actual, expected) \
    (((expected - DELTA) <= actual) && (actual <= (expected + DELTA)))

static char *test_poly3_create() {
    poly3 p;
    poly3 test;

    poly3_create(10, &p);
    POLY3(test, 0, -1, 1, 0); 
    mu_assert("RN 10 -> 001 + 011", POLY3_EQUAL(p, test));

    poly3_create(78, &p);
    POLY3(test, 0, -2, 2, 0);
    mu_assert("RN 78 -> 101 011 010 001", POLY3_EQUAL(p, test));

    poly3_create(255, &p);
    POLY3(test, 0, 0, 0, 1);
    mu_assert("RN 255 -> ALL", POLY3_EQUAL(p, test));

    return NULL;
}

static char *test_poly3_get_maximum() {
    pair_double pd;
    poly3 p1;
    poly3 p2;
    poly3 p3;
    poly3 p4;
    poly3 p5;

    POLY3(p1, -1, 3, -3, 1);
    POLY3(p2, 1, -2, 1, 0);
    POLY3(p3, -1, 1, 0, 0);
    POLY3(p4, 1, 0, 0, 0);
    POLY3(p5, 0, -1, 1, 0);

    poly3_get_maximum(&p1, &pd);
    mu_assert("The 03 case. (three 0's)",
              IN_RANGE(pd.first, 0.0) || IN_RANGE(pd.last, 0.0));

    poly3_get_maximum(&p2, &pd);
    mu_assert("The 12 case. (two 0's one 1's)",
              IN_RANGE(pd.first, 0.148148) || IN_RANGE(pd.last, 0.148148));

    poly3_get_maximum(&p3, &pd);
    mu_assert("The 21 case. (one 0's two 1's)",
              IN_RANGE(pd.first, 0.148148) || IN_RANGE(pd.last, 0.148148));

    poly3_get_maximum(&p4, &pd);
    mu_assert("The 30 case. (three 1's)",
              IN_RANGE(pd.first, 0.0) || IN_RANGE(pd.last, 0.0));

    poly3_get_maximum(&p5, &pd);
    mu_assert("10 -> 2^3 + 2^1 -> 001, 011",
              IN_RANGE(pd.first, 0.25) || IN_RANGE(pd.last, 0.25));
    return NULL;
}

static char *test_poly3_eval() {
    poly3 p;

    POLY3(p, 0, 0, 0, 1);
    mu_assert("eval 1 with 1",
              IN_RANGE(poly3_eval(&p, 1), 1.0));
    mu_assert("eval 1 with 23",
              IN_RANGE(poly3_eval(&p, 23), 1.0));

    POLY3(p, 0, 0, 3, 1);
    mu_assert("eval 3*x + 1, x = 1",
              IN_RANGE(poly3_eval(&p, 1), 4.0));
    mu_assert("eval 3*x + 1, x = 4",
              IN_RANGE(poly3_eval(&p, 4), 13.0));

    POLY3(p, 1, 2, 3, 4);
    mu_assert("eval x^3 + 2*x^2 + 3*x + 4, x = 1",
              IN_RANGE(poly3_eval(&p, 1), 10.0));
    mu_assert("eval x^3 + 2*x^2 + 3*x + 4, x = 2",
              IN_RANGE(poly3_eval(&p, 2), 26.0));

    return NULL;
}

static char *test_find_break_even() {
    uint32_t rn;

    /* Odd relationship numbers all have the same deal.
     * Set everything to false. */
    for (rn = 1; rn < 256; rn += 2) {
        mu_assert("broke, damn it",
                  IN_RANGE(find_break_even(rn, 3), 1.0));
    }
    /* Relationship numbers above 128 all have the same deal.
     * Set everything to true. */
    for (rn = 128; rn < 256; rn++) {
        mu_assert("broke, damn it",
                  IN_RANGE(find_break_even(rn, 3), 1.0));
    }
    /* http://www.ccs.neu.edu/home/lieber/courses/cs4500/f09/preparation/break-even-table
    0 : 0.0 : 0.0
    1 : 0.0 : 1.0
    2 : 0.3333333333333333 : 0.14814814814814814 = 4/27
    3 : 0.0 : 1.0
    4 : 0.3333333333333333 : 0.14814814814814814 = 4/27
    5 : 0.0 : 1.0
    6 : 0.3333333333333333 : 0.2962962962962963 = 8/27
    7 : 0.0 : 1.0
    8 : 0.6666666666666666 : 0.14814814814814814
    9 : 0.0 : 1.0
    10 : 0.5 : 0.25
    11 : 0.0 : 1.0
    12 : 0.5 : 0.25
    13 : 0.0 : 1.0
    14 : 0.42264973081037427 : 0.38490017945975047
    15 : 0.0 : 1.0
    16 : 0.3333333333333333 : 0.14814814814814814
    17 : 0.0 : 1.0
    18 : 0.3333333333333333 : 0.2962962962962963
    19 : 0.0 : 1.0
    20 : 0.3333333333333333 : 0.2962962962962963
    21 : 0.0 : 1.0
    22 : 0.3333333333333333 : 0.4444444444444445
    */

    mu_assert("RN 2", IN_RANGE(find_break_even(2, 3), 0.148148148148));
    mu_assert("RN 4", IN_RANGE(find_break_even(4, 3), 0.148148148148));
    mu_assert("RN 6", IN_RANGE(find_break_even(6, 3), 0.296296296296));
    mu_assert("RN 8", IN_RANGE(find_break_even(8, 3), 0.148148148148));
    mu_assert("RN 10", IN_RANGE(find_break_even(10, 3), 0.25));
    mu_assert("RN 12", IN_RANGE(find_break_even(12, 3), 0.25));
    mu_assert("RN 14", IN_RANGE(find_break_even(14, 3), 0.384900179));
    mu_assert("RN 16", IN_RANGE(find_break_even(16, 3), 0.148148148148));
    mu_assert("RN 18", IN_RANGE(find_break_even(18, 3), 0.296296296296));
    mu_assert("RN 20", IN_RANGE(find_break_even(20, 3), 0.296296296296));
    mu_assert("RN 22", IN_RANGE(find_break_even(22, 3), 0.444444444444));

    return NULL;
}

/* poly tests */

static char *test_poly_new() {
    poly *p;

    p = poly_new(3, 17, 3, 2, 1);
    mu_assert("p == NULL", p != NULL);

    mu_assert("p(0.0) != 17.0", IN_RANGE(poly_eval(p, 0), 17));
    mu_assert("p(1.0) != 23.0", IN_RANGE(poly_eval(p, 1), 23));
    mu_assert("p(2.0) != 39.0", IN_RANGE(poly_eval(p, 2), 39)); 

    poly_delete(p);
    return NULL;
}

static char *test_poly_synth_div() {
    poly *p;

    p = poly_new(5, -1, 0, 25, 0, 3, 2);
    mu_assert("2x^5 + 3x^4 + 25x^2 - 1", poly_synth_div(p, -3) == -19);
    poly_delete(p);

    p = poly_new(3, 2, 1, -8, 1);
    mu_assert("x^3 - 8x^2 + x + 2", poly_synth_div(p, 7) == -40);
    poly_delete(p);

    p = poly_new(2, 1, 2, 1);
    mu_assert("(x+1)(x+1)", poly_synth_div(p, -1) == 0);
    poly_delete(p);

    return NULL;
}

char *all_tests() {
    mu_run_test(test_poly3_get_maximum);
    mu_run_test(test_poly3_create);
    mu_run_test(test_poly3_eval);
    mu_run_test(test_find_break_even);
    mu_run_test(test_poly_new);
    mu_run_test(test_poly_synth_div);
    return NULL;
}
