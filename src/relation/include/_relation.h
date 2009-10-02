#ifndef _RELATION_H_
#define _RELATION_H_

#include <stdint.h>

/* Defined in relation_consts.h
 * MASKS = { 0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF }
 */
uint32_t
get_mask(int rank);

/* Defined in relation_consts.h
 * MAGIC_NUMBERS = { 0x55555555, 0x33333333, 0x0F0F0F0F,
 *                   0x00FF00FF, 0x0000FFFF }
 */
uint32_t
get_magic_number(int rank, int var_p, int value);

/* True if the variable position is irrelevant to the (rn, rank)
 */
int
is_irrelevant(uint32_t rn, int rank, int var_p);

/* Counts the number of relevant variables in the given relation
 * @param rn the relation number whose number of relevant variables is to be counted
 * @param rank rank of the given relation
 * @return The number of relevant variables in the given relation
 */
int
num_relevant_variables(uint32_t rn, int rank);

/* Returns the number of 1-bits in the given rn masked by the rank.
 * m = 2 ** (2 ** rank)
 * So, the 1-bits of (rn & m)
 * @param rn
 * @param rank
 * @return the number of 1-bits in rn limited by rank
 */
int
ones(uint32_t rn, int rank);

/*
 * @param rn
 * @param rank
 * @param num_true_vars used to identify a set of rows in the truth table
 * @return counts the number of ones corresponding to truth table rows
 *         with the given number of true variables
 */
int
q(uint32_t rn, int rank, int num_true_vars);

/* In relation_consts.h
 * TRUE_VARS = {
 *     {},
 *     {1, 2},
 *     {1, 6, 8},
 *     {1, 22, 104, 128},
 *     {1, 278, 5736, 26752, 32768}
 *     {1, 65814, 18224744, 375941248, 1753251840, 2147483648L},
 * }
 */
uint32_t
x_true_vars(int rank, int num_true_vars);


/* Reduce a given relationship number by assigning.
 */
uint32_t
reduce(uint32_t rn, int rank, int var_p, int value);

#endif
