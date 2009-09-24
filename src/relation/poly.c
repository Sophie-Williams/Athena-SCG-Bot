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

double
poly3_get_maximum(poly3 *poly) {
    double answer1 = 0.0;
    double answer2 = 0.0;
    double a;
    double b;
    double c;
    double z;

    assert(poly != NULL);

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
        return 0.0;
    }

    z = pow(b, 2) - (4 * a * c);
    if (z < 0) {
        return 0.0;
    }

    /* corner case, if a == 0, this is a polynomial or degree 1 
     * So, a*x^2 + b*x + c = 0
     *     b*x + c = 0
     *     x = ((-c) / b)
     */
    if (a == 0) {
        answer1 = -(((double)c) / b);
    }
    else {
        answer1 = (-b + sqrt(z)) / (2 * a);
        answer2 = (-b - sqrt(z)) / (2 * a);
    }

    if (0 < answer1 && answer1 < 1) {
        return answer1;
    }
    if (0 < answer2 && answer2 < 1) {
        return answer2;
    }

    return 0.0;
}

double
poly3_eval(poly3 *poly, double x) {
    assert(poly != NULL);

    return (pow(x, 3) * poly->coeff3 + pow(x, 2) * poly->coeff2 +
            x * poly->coeff1 + poly->coeff0);
}

double
find_break_even(uint32_t rn) {
    poly3 *poly;
    double answer;

    if(rn % 2 == 1) {
        return 0.0;
    }

    poly = poly3_create(rn);

    assert(poly != NULL);

    answer = poly3_get_maximum(poly);
    free(poly);

    return answer;
}
