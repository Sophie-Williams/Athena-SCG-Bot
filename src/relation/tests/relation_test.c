#include <stdio.h>
#include <stdlib.h>

#include "minunit.h"
#include "relation.h"

int tests_run = 0;
int asserts = 0;

static char *test_implies() {
    int i;
    mu_assert("0 -> 22",  implies(0, 22));
    mu_assert("2 -> 22",  implies(2, 22));
    mu_assert("4 -> 22",  implies(4, 22));
    mu_assert("6 -> 22",  implies(6, 22));
    mu_assert("16 -> 22", implies(16, 22));
    mu_assert("18 -> 22", implies(18, 22));
    mu_assert("20 -> 22", implies(20, 22));
    mu_assert("22 -> 22", implies(22, 22));
    for (i = 1; i < 256; i++) {
        if (i != 2 && i != 4 && i != 6 && i != 16 && i != 18 && i != 20 &&
            i != 22) {
            mu_assert("uh oh", !implies(i, 22));
        }
    }
    return NULL;
}

char *all_tests() {
    mu_run_test(test_implies);
    return NULL;
}
