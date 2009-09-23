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
              IN_RANGE(poly3_get_maximum(&p1), 1.0));
    mu_assert("The 12 case. (two 0's one 1's)",
              IN_RANGE(poly3_get_maximum(&p2), 1.0));
    mu_assert("The 21 case. (one 0's two 1's)",
              IN_RANGE(poly3_get_maximum(&p3), 0.6666666));
    mu_assert("The 30 case. (three 1's)",
              IN_RANGE(poly3_get_maximum(&p4), 0.0));
    mu_assert("10 -> 2^3 + 2^1 -> 001, 011",
              IN_RANGE(poly3_get_maximum(&p5), 0.5));

    return NULL;
}


static char *all_tests() {
    mu_run_test(test_poly3_get_maximum);
    mu_run_test(test_poly3_create);
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
