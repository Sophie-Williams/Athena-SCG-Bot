#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdarg.h>
#include <math.h>

#include "relation.h"
#include "relation_consts.h"
#include "poly.h"

double
find_break_even(uint32_t rn, int rank) {
    poly *p;
    double maxima;
    p = poly_from_relation_number(rn, rank);
    maxima = poly_maxima(p, 0.0, 1.0);
    poly_delete(p);
    return maxima;
}

double
find_maximum_point(uint32_t rn, int rank) {
    poly *p;
    double max_point;
    p = poly_from_relation_number(rn, rank);
    max_point = poly_maximum_point(p, 0.0, 1.0);
    poly_delete(p);
    return max_point;
}

/* Functions for poly */

poly *
poly_create(int *coeffs, int degree) {
    poly *p;

    assert(coeffs);

    if ((p = malloc(sizeof(poly))) == NULL) {
        perror("malloc");
        return NULL;
    }

    if ((p->coeffs = malloc(sizeof(int) * (degree + 1))) == NULL) {
        perror("malloc");
        free(p);
        return NULL;
    }
    memcpy(p->coeffs, coeffs, sizeof(int) * (degree + 1));
    return p;
}

void
poly_delete(poly *p) {
    free(p->coeffs);
    free(p);
}

double
poly_eval(poly *p, double value) {
    double ret;
    int i;

    assert(p);
    assert(p->coeffs);

    /* Horner's rule */
    ret = p->coeffs[p->degree];
    for (i = p->degree - 1; i >= 0; i--) {
        ret = ret * value + p->coeffs[i];
    }
    
    return ret;
}

poly *
poly_new(int degree, ...) {
    int i;
    va_list argptr;
    poly *p;

    /* Allocate. */
    if ((p = malloc(sizeof(poly))) == NULL) {
        perror("malloc");
        return NULL;
    }

    /* A polynomial of degree n has n+1 coefficients for each term in [n,0] */
    if ((p->coeffs = malloc(sizeof(int) * (degree + 1))) == NULL) {
        perror("malloc");
        free(p);
        return NULL;
    }

    p->degree = degree; /* Set the degree of the polynomial */

    /* Copy the list. */
    va_start(argptr, degree);
    for(i = 0; i <= degree; i++) {
        *(p->coeffs+i) = va_arg(argptr, int);
    }
    va_end(argptr);

    return p;
}

poly *
poly_from_relation_number(uint32_t rn, int rank) {
    poly *p;
    int i;
    int j;
    int num_rows;

    assert(VALID_RANK(rank));

    if ((p = malloc(sizeof(poly))) == NULL) {
        perror("malloc");
        return NULL;
    }
    if ((p->coeffs = malloc(sizeof(int) * (rank + 1))) == NULL) {
        perror("malloc");
        free(p);
        return NULL;
    }

    p->degree = rank;
    /* Zero-out the coefficients. */
    memset(p->coeffs, 0, sizeof(int) * (rank + 1));

    /* For each possible number of true variables [0,rank] (the function
     * `q` does exactly that), find the number of rows. Add to the polynomial.
     * Uses the binomial theorem, since the generated polynomials are
     * of the form p^x * (1-p)^y, where x + y == rank
     */
    for (i = 0; i <= rank; i++) {
        if ((num_rows = q(rn, rank, rank - i)) > 0) {
            for (j = 0; j <= i; j++) {
                /* Assigns from the top degree. */
                *(p->coeffs + rank-j) += pascal(i, j)
                                       * num_rows
                                       * (j % 2 == i % 2 ? 1 : -1);
            }
        }
    }

    return p;
}

double
poly_maximum_point(poly *p, double left, double right) {
    double max;
    double max_point;
    double temp;

    assert(p);
    assert(left < right);

    max = -INFINITY;
    max_point = left;

    /* Brute force check. */
    for (; left < right; left += 0.0001) {
        if ((temp = poly_eval(p, left)) > max) {
            max = temp;
            max_point = left;
        }
    }

    /* Check the right-bound */
    return (poly_eval(p, right) > max) ? right : max_point;
}

double
poly_maxima(poly *p, double left, double right) {
    assert(p);
    assert(left < right);
    return poly_eval(p, poly_maximum_point(p, left, right));
}

int
poly_synth_div(poly *p, int q) {
    int i;
    int r;

    assert(p);

    r = *(p->coeffs+p->degree);
    for (i = p->degree - 1; i >= 0; i--) {
        r = *(p->coeffs+i) + (r * q);
    }

    return r;
}

int
pascal(int n, int r) {
    int i;
    int x;

    if (n < 0 || r < 0 || r > n)
        return 1;

    x = 1;
    for (i = n; i > (n - r); i--)
        x *= i;
    for (; r > 1; r--)
        x /= r;

    return x;
}
