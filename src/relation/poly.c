#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <math.h>

#include "poly.h"

#define HAVE_NTH(x, nth) (((x >> nth) % 2) == 1)

poly3 *
poly3_create(uint32_t rn) {
    /*
    int have8 = ((rn >> 7) % 2) == 1;
    int have7 = ((rn >> 6) % 2) == 1;
    int have6 = ((rn >> 5) % 2) == 1;
    int have5 = ((rn >> 4) % 2) == 1;
    int have4 = ((rn >> 3) % 2) == 1;
    int have3 = ((rn >> 2) % 2) == 1;
    int have2 = ((rn >> 1) % 2) == 1;
    int have1 = (rn % 2) == 1;
    */

    poly3 *poly = malloc(sizeof(poly3));
    if (poly == NULL) {
        return NULL;
    }

    POLY3(*poly, 0, 0, 0, 0);

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
    double answer1;
    double answer2;
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

    z = pow(b, 2) - (4 * a * c);
    if (z < 0) {
        return -1.0;
    }

    /* corner case, if a == 0, this is a polynomial or degree 1 
     * So, a*x^2 + b*x + c = 0
     *     b*x + c = 0
     *     x = ((-c) / b)
     */
    if (a == 0) {
        return -(((double)c) / b);
    }

    answer1 = (-b + sqrt(z)) / (2 * a);
    answer2 = (-b - sqrt(z)) / (2 * a);

    if (answer1 > 0 && answer2 > 0) {
        return answer1 > answer2 ? answer1 : answer2;
    }
    else if(answer1 > 0) {
        return answer1;
    }
    return answer2;
}
