#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdarg.h>
#include <math.h>

#include "poly.h"

#define HAVE_NTH(x, nth) (((x >> nth) % 2) == 1)

/**
 * @param rn The relation number.
 * @param poly The reference to a polynomial.
 * @return The polynomial corresponding to the relation number.
 */
poly3 *
poly3_create(uint32_t rn, poly3 *poly) {
    assert(poly != NULL);

    POLY3(*poly, 0, 0, 0, 0);

    /*
     * 0 -> 000
     * 1 -> 001
     * 2 -> 010
     * 3 -> 011
     * 4 -> 100
     * 5 -> 101
     * 6 -> 110
     * 7 -> 111
     */
    if (HAVE_NTH(rn, 0)) POLY3_ADD_03(*poly);
    if (HAVE_NTH(rn, 1)) POLY3_ADD_12(*poly);
    if (HAVE_NTH(rn, 2)) POLY3_ADD_12(*poly);
    if (HAVE_NTH(rn, 3)) POLY3_ADD_21(*poly);
    if (HAVE_NTH(rn, 4)) POLY3_ADD_12(*poly);
    if (HAVE_NTH(rn, 5)) POLY3_ADD_21(*poly);
    if (HAVE_NTH(rn, 6)) POLY3_ADD_21(*poly);
    if (HAVE_NTH(rn, 7)) POLY3_ADD_30(*poly);

    return poly;
}

/**
 * poly3_add : poly3 *a, poly3 *b
 * Add the values of b into a.
 */
poly3 *
poly3_add(poly3 *a, poly3 *b) {
    assert(a != NULL);
    assert(b != NULL);

    a->coeff3 += b->coeff3;
    a->coeff2 += b->coeff2;
    a->coeff1 += b->coeff1;
    a->coeff0 += b->coeff0;

    return a;
}

pair_double *
poly3_find_critical_points(poly3 *poly, pair_double *answer) {
    double a;
    double b;
    double c;
    double z;

    assert(answer != NULL);
    assert(poly != NULL);

    /* Differentiate */
    a = poly->coeff3 * 3;
    b = poly->coeff2 * 2;
    c = poly->coeff1;

    /* If both a and b are 0, we do not have a polynomial of interest. */
    if (a == 0 && b == 0) {
        return NULL;
    }

    /* If z < 0, then there are only complex solutions. */
    z = pow(b, 2) - (4 * a * c);
    if (z < 0) {
        return NULL;
    }

    /* corner case, if a == 0, this is a polynomial or degree 1 
     * So, a*x^2 + b*x + c = 0
     *     b*x + c = 0
     *     x = ((-c) / b)
     */
    if (a == 0) {
        answer->first = -(((double)c) / b);
        answer->last = answer->first;
    }
    else {
        answer->first = (-b + sqrt(z)) / (2 * a);
        answer->last = (-b - sqrt(z)) / (2 * a);
    }

    return answer;
}

pair_double *
poly3_get_maximum(poly3 *poly, pair_double *answer) {
    pair_double possible_points;

    assert(answer != NULL);
    assert(poly != NULL);

    answer->first = 0.0;
    answer->last = 0.0;

    /* Find the critical points and evaluate them. */
    if ((poly3_find_critical_points(poly, &possible_points)) != NULL) {
        answer->first = poly3_eval(poly, possible_points.first);
        answer->last = poly3_eval(poly, possible_points.last);
    }
    return answer;
}

double
poly3_eval(poly3 *poly, double x) {
    assert(poly != NULL);

    return (pow(x, 3) * poly->coeff3 + pow(x, 2) * poly->coeff2 +
            x * poly->coeff1 + poly->coeff0);
}

/** TODO Not your best sort */
static void sort(double *list, int length) {
    int i;
    int j;
    double temp;

    for (i = 0; i < length - 1; i++) {
        for (j = i + 1; j < length; j++) {
            if (*(list+j) > *(list+i)) {
                /* SWAP */
                temp = *(list+j);
                *(list+j) = *(list+i);
                *(list+i) = temp;
            }
        }
    }
}

double
find_maximum_point(uint32_t rn, int rank) {
    poly3 poly;
    pair_double possible_points;
    double p[4];
    int i;

    /* Set all to false. */
    if (rn % 2 == 1) {
        return 0;
    }
    /* Set all to true. */
    if (rn >= 128) {
        return 1;
    }
    poly3_create(rn, &poly);
    poly3_find_critical_points(&poly, &possible_points);

    p[0] = poly3_eval(&poly, 0);
    p[1] = poly3_eval(&poly, 1);
    p[2] = poly3_eval(&poly, possible_points.first);
    p[3] = poly3_eval(&poly, possible_points.last);

    sort(p, 4);

    for (i = 0; i < 4; i++) {
        if (!(p[i] > 1) || !(p[i] < 0)) {
            if (fabs(p[i] - poly3_eval(&poly, possible_points.first)) < 0.001)
                return possible_points.first;
            if (fabs(p[i] - poly3_eval(&poly, possible_points.last)) < 0.001)
                return possible_points.last;
            if (fabs(p[i] - poly3_eval(&poly, 0)) < 0.001)
                return 0;
            if (fabs(p[i] - poly3_eval(&poly, 1)) < 0.001)
                return 1;
            return 0.0;
        }
    }

    return 0.0;
}

double
find_break_even(uint32_t rn, int rank) {
    poly3 poly;
    pair_double possible;
    double p[4];
    int i;

    /* If odd, the break even is 1.0
     *
     * The first row is 000 => (1-p)^3 = 1^3 -3p +3p^2 -p^3
     * So, when p == 0, the equation == 1
     *
     * Even when added to other rows, this is true because the other rows'
     * 0th coefficient == 0
     */
    if (rn % 2 == 1) {
        return 1.0;
    }

    /*
     * Optimized Prime!
     */
    if (rn >= 127) {
        return 1.0;
    }

    poly3_create(rn, &poly);

    poly3_get_maximum(&poly, &possible);

    /* All possible maximum values for [0,1]. It may include points from o */
    p[0] = poly3_eval(&poly, 0);
    p[1] = poly3_eval(&poly, 1);
    p[2] = possible.first;
    p[3] = possible.last;

    /* The break even is the value of the polynomial at its maximum. */

    sort(p, 4);

    for (i = 0; i < 4; i++) {
        if (!(p[i] > 1)) {
            return p[i];
        }
    }

    /* Every maximum was out of range, so normalize. */
    return 1.0;
}

/* Functions for poly */

poly *
poly_create(int *coeffs, int degree) {
    poly *p;

    assert(coeffs != NULL);

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

    assert(p != NULL);
    assert(p->coeffs != NULL);

    ret = 0.0;
    for (i = 0; i <= p->degree; i++) {
        ret += pow(value, i) * p->coeffs[i];
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

double
poly_maxima(poly *p, double left, double right) {
    double max;
    double temp;

    assert(p != NULL);
    assert(left < right);

    max = -INFINITY;

    /* */
    for (; left < right; left += 0.001) {
        if ((temp = poly_eval(p, left)) > max)
            max = temp;
    }

    /* Check the right-bound */
    return (temp = poly_eval(p, right)) > max ? temp : max;
}

int
poly_synth_div(poly *p, int q) {
    int i;
    int r;

    assert(p != NULL);

    r = *(p->coeffs+p->degree);
    for (i = p->degree - 1; i >= 0; i--) {
        r = *(p->coeffs+i) + (r * q);
    }

    return r;
}
