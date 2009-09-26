#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <math.h>

#include "poly.h"
#include "_relation.h"

#define HAVE_NTH(x, nth) (((x >> nth) % 2) == 1)

poly3 *
poly3_create(uint32_t rn) {
    poly3 *poly;
/*    poly3 temp; */

    poly = malloc(sizeof(poly3));
    if (poly == NULL) {
        return NULL;
    }
    
    POLY3(*poly, 0, 0, 0, 0);

/* version 2
    POLY3_SET_30(temp);
    POLY3_MULTIPLY(temp, _q(rn, 3, 3));
    poly3_add(poly, &temp);

    POLY3_SET_21(temp);
    POLY3_MULTIPLY(temp, _q(rn, 3, 2));
    poly3_add(poly, &temp);

    POLY3_SET_12(temp);
    POLY3_MULTIPLY(temp, _q(rn, 3, 1));
    poly3_add(poly, &temp);

    POLY3_SET_03(temp);
    POLY3_MULTIPLY(temp, _q(rn, 3, 0));
    poly3_add(poly, &temp);
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
poly3_get_maximum(poly3 *poly, pair_double *answer) {
    double a;
    double b;
    double c;
    double z;

    assert(answer != NULL);
    assert(poly != NULL);

    answer->first = 0.0;
    answer->last = 0.0;

    a = poly->coeff3 * 3;
    b = poly->coeff2 * 2;
    c = poly->coeff1;

    /* If both a and b are 0, we do not have a polynomial. */
    /* assert(a != 0 || b != 0); */
    /* Since this is a special function designed to find the break-even
     * point for class, this needs to be dealt with.
     * XXX TODO FIXME
     */

    if (a == 0 && b == 0) {
        return answer;
    }

    z = pow(b, 2) - (4 * a * c);
    if (z < 0) {
        return answer;
    }

    /* corner case, if a == 0, this is a polynomial or degree 1 
     * So, a*x^2 + b*x + c = 0
     *     b*x + c = 0
     *     x = ((-c) / b)
     */
    if (a == 0) {
        answer->first = -(((double)c) / b);
    }
    else {
        answer->first = (-b + sqrt(z)) / (2 * a);
        answer->last = (-b - sqrt(z)) / (2 * a);
    }

    answer->first = poly3_eval(poly, answer->first);
    answer->last = poly3_eval(poly, answer->last);
    return answer;
}

double
poly3_eval(poly3 *poly, double x) {
    assert(poly != NULL);

    return (pow(x, 3) * poly->coeff3 + pow(x, 2) * poly->coeff2 +
            x * poly->coeff1 + poly->coeff0);
}

/* Not your best sort */
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
find_break_even(uint32_t rn, int rank) {
    poly3 *poly;
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

    poly = poly3_create(rn);

    /* this usually means ENOMEM : no memory */
    assert(poly != NULL);

    poly3_get_maximum(poly, &possible);

    /* All possible maximum values for [0,1]. It may include points from o */
    p[0] = poly3_eval(poly, 0);
    p[1] = poly3_eval(poly, 1);
    p[2] = possible.first;
    p[3] = possible.last;

    free(poly);

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
