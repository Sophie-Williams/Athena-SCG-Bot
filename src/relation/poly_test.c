#include <stdio.h>
#include <stdlib.h>
#include "minunit.h"
#include "poly.h"

int tests_run = 0;

#define DELTA 0.001
#define IN_RANGE(actual, expected) \
    (((expected - DELTA) <= actual) && (actual <= (expected + DELTA)))

static char *test_poly3_create() {
    poly3 *p;
    poly3 test;

    p = poly3_create(10);
    POLY3(test, 0, -1, 1, 0); 
    mu_assert("RN 10 -> 001 + 011",
              POLY3_EQUAL(*p, test));
    free(p);

    p = poly3_create(78);
    POLY3(test, 0, -2, 2, 0);
    mu_assert("RN 78 -> 101 011 010 001",
              POLY3_EQUAL(*p, test));

    p = poly3_create(255);
    POLY3(test, 0, 0, 0, 1);
    mu_assert("RN 255 -> ALL",
              POLY3_EQUAL(*p, test));
    free(p);

    return NULL;
}


static char *test_poly3_get_maximum() {
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

    mu_assert("The 03 case. (three 0's)",
              IN_RANGE(poly3_get_maximum(&p1), 0.0));
    mu_assert("The 12 case. (two 0's one 1's)",
              IN_RANGE(poly3_get_maximum(&p2), 0.3333333));
    mu_assert("The 21 case. (one 0's two 1's)",
              IN_RANGE(poly3_get_maximum(&p3), 0.6666666));
    mu_assert("The 30 case. (three 1's)",
              IN_RANGE(poly3_get_maximum(&p4), 0.0));
    mu_assert("10 -> 2^3 + 2^1 -> 001, 011",
              IN_RANGE(poly3_get_maximum(&p5), 0.5));

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
     * XXX is this true? */
    for (rn = 1; rn < 256; rn += 2) {
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

static char *all_tests() {
    mu_run_test(test_poly3_get_maximum);
    mu_run_test(test_poly3_create);
    mu_run_test(test_poly3_eval);
    mu_run_test(test_find_break_even);
    return NULL;
}

void print_all_p3();
void print_all_p3() {
    int i;
    poly3 *p;

    for (i = 1; i < 256; i++) {
        p = poly3_create(i);
        printf("%03d %12lf\n", i, find_break_even(i, 3));
        free(p);
    }
}

int main(int argc, char **argv) {
    char *result = all_tests();
/*
    print_all_p3();
 */
    if (result != 0) {
        printf("%s\n", result);
    }
    else {
        printf("ALL TESTS PASSED\n");
    }
    printf("Tests run: %d\n", tests_run);

    return result != 0;
}
