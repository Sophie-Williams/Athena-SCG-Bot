#ifndef POLY_H_
#define POLY_H_

#include <stdint.h>

/* COEFF <number of ones> <number of zeros> _ <coefficient> */

/* for 111 (7) */
#define COEFF30_3 1
#define COEFF30_2 0
#define COEFF30_1 0
#define COEFF30_0 0
/* for 011, 101, 110 (3, 5, 6) */
#define COEFF21_3 -1
#define COEFF21_2 1
#define COEFF21_1 0
#define COEFF21_0 0
/* for 001, 010, 100 (1, 2, 4) */
#define COEFF12_3 1
#define COEFF12_2 -2
#define COEFF12_1 1
#define COEFF12_0 0
/* for 000 (0) */
#define COEFF03_3 -1
#define COEFF03_2 3
#define COEFF03_1 -3
#define COEFF03_0 1


/* Data defs */
typedef struct __polynomial {
    int coefficient;
    int degree;
} polynomial;

typedef struct __poly3 {
    int coeff3;
    int coeff2;
    int coeff1;
    int coeff0;
} poly3;

/* Convenience MACRO for assigning values to a poly3 */
#define POLY3(p, c3, c2, c1, c0) do {\
    (p).coeff3 = c3; (p).coeff2 = c2; (p).coeff1 = c1; (p).coeff0 = c0; }\
    while(0)

#define POLY3_EQUAL(p1, p2) \
    (((p1).coeff3 == (p2).coeff3) && \
     ((p1).coeff2 == (p2).coeff2) && \
     ((p1).coeff1 == (p2).coeff1) && \
     ((p1).coeff0 == (p2).coeff0))

#define POLY3_ADD(p, amt3, amt2, amt1, amt0) do {\
    (p).coeff3 += amt3; (p).coeff2 += amt2; \
    (p).coeff1 += amt1; (p).coeff0 += amt0; } while(0)

#define POLY3_MULTIPLY(p, factor) do {\
    (p).coeff3 *= factor; (p).coeff2 *= factor; \
    (p).coeff1 *= factor; (p).coeff0 *= factor; } while(0)

#define POLY3_ADD_30(p) POLY3_ADD(p, COEFF30_3, COEFF30_2, \
                                     COEFF30_1, COEFF30_0)
#define POLY3_ADD_21(p) POLY3_ADD(p, COEFF21_3, COEFF21_2, \
                                     COEFF21_1, COEFF21_0)
#define POLY3_ADD_12(p) POLY3_ADD(p, COEFF12_3, COEFF12_2, \
                                     COEFF12_1, COEFF12_0)
#define POLY3_ADD_03(p) POLY3_ADD(p, COEFF03_3, COEFF03_2, \
                                     COEFF03_1, COEFF03_0)
#define POLY3_SET_30(p) POLY3(p, COEFF30_3, COEFF30_2, \
                                 COEFF30_1, COEFF30_0)
#define POLY3_SET_21(p) POLY3(p, COEFF21_3, COEFF21_2, \
                                 COEFF21_1, COEFF21_0)
#define POLY3_SET_12(p) POLY3(p, COEFF12_3, COEFF12_2, \
                                 COEFF12_1, COEFF12_0)
#define POLY3_SET_03(p) POLY3(p, COEFF03_3, COEFF03_2, \
                                 COEFF03_1, COEFF03_0)

poly3 *
poly3_create(uint32_t rn);


poly3 *
poly3_add(poly3 *a, poly3 *b);

double
poly3_get_maximum(poly3 *poly);

double
poly3_eval(poly3 *poly, double x);

double
find_break_even(uint32_t rn);

#endif
