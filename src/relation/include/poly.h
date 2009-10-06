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

/**
 */
typedef struct __pair_double {
    /** First in a pair. */
    double first;
    /** Last in a pair.  */
    double last;
} pair_double;

/**
 * A poly nomial of degree 3.
 */
typedef struct __poly3 {
    /** The coefficient of the cubic term. */
    int coeff3;
    /** The coefficient of the square term. */
    int coeff2;
    /** The coefficient of the first degree term. */
    int coeff1;
    /** The constant term. */
    int coeff0;
} poly3;

/**
 * A generic polynomial.
 */
typedef struct __poly {
    /** The degree of the polynomial. */
    int degree;
    /** The list of coefficients. Ordered from the constant term to the nth
        degree term (where n == degree). */
    int *coeffs;
} poly;

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

/**
 * Creates a polynomial of the third degree that corresponds to the relation
 * number.
 * @param rn The relation number.
 * @param poly The pointer to the polynomial.
 * @return a polynomial of the third degree.
 */
poly3 *
poly3_create(uint32_t rn, poly3 *poly);

/**
 * Add `b' to `a' and store in `a'
 * @param a A polynomial.
 * @param b A polynomial.
 * @return a + b
 */
poly3 *
poly3_add(poly3 *a, poly3 *b);

/**
 * Get the maximum value of the polynomial within the range [0,1]
 * @param poly The polynomial to solve.
 * @param answer The address of the answer.
 * @return the values of the maxima (or minima)
 */
pair_double *
poly3_get_maximum(poly3 *poly, pair_double *answer);

/**
 * Evalutate the polynomial (of the third degree) with the given value
 * as the variable.
 * @param poly The polynomial f(x).
 * @param x The value x.
 * @return The polynomial evaluated at x.
 */
double
poly3_eval(poly3 *poly, double x);

/**
 * Compute the break-even price of the polynomial.
 * This is actually the minimum satisfiability.
 * @param rn The relation number.
 * @param rank The rank of the relation number.
 */
double
find_break_even(uint32_t rn, int rank);

/* Functions for poly */

/**
 * Create a polynomial of the given degree with variable length.
 * e.g.
 *      poly_new(3, 4, 3, 2, 1) = 4 + 3*x + 2*x^2 + x^3
 *      poly_new(2, 3, 2, 1)    = 3 + 2*x^2 + x^3
 * @param degree The degree of the polynomial.
 * @return The newly created polynomial.
 */
poly *
poly_new(int degree, ...);

/**
 * Create a polynomial from a list of coefficients.
 * @param coeffs The coefficients.
 * @param degree The degree of the polynomial.
 * @return The newly created polynomial.
 */
poly *
poly_create(int *coeffs, int degree);

/**
 * Free the polynomial.
 * @param p The polynomial to free.
 */
void
poly_delete(poly *p);

/**
 * Evaluate the polynomial.
 * @param p The polynomial.
 * @param x The value.
 * @return p(x)
 */
double
poly_eval(poly *p, double x);

/**
 * Do synthetic division on the polynomial and return the remainder.
 * @param p The polynomial.
 * @param q The quotient for the synthetic division.
 * @return the remainder
 */
int
poly_synth_div(poly *p, int q);

#endif
