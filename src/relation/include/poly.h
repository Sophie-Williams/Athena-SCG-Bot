#ifndef POLY_H_
#define POLY_H_

#include <stdint.h>

/* Data defs */

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

/**
 * Compute the break-even price of the polynomial.
 * This is actually the minimum satisfiability.
 * @param rn The relation number.
 * @param rank The rank of the relation number.
 */
double
find_break_even(uint32_t rn, int rank);

/**
 * @param rn The relation number.
 * @param rank The rank of the relation number.
 * @return The maximal point (where the polynomial would evaluate to the
 *         break even point).
 */
double
find_maximum_point(uint32_t rn, int rank);

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
 * @param p The polynomial.
 * @param left The lower bound to the range.
 * @param right The upper bound to the range.
 * @return The maximum point in the range.
 */
double
poly_maximum_point(poly *p, double left, double right);

/**
 * @param p The polynomial.
 * @param left The lower bound to the range.
 * @param right The upper bound to the range.
 * @return The maximum value in the range.
 */
double
poly_maxima(poly *p, double left, double right);

/**
 * Do synthetic division on the polynomial and return the remainder.
 * @param p The polynomial.
 * @param q The quotient for the synthetic division.
 * @return the remainder
 */
int
poly_synth_div(poly *p, int q);

/**
 * @param rn The relation number.
 * @param rank The rank of the relation number.
 * @return The polynomial that corresponds to the relation number.
 */
poly *
poly_from_relation_number(uint32_t rn, int rank);

/* OTHER */

/**
 * Find the binomial coefficient.
 * @param n
 * @param r
 * @return
 */
int
pascal(int n, int r);

#endif
