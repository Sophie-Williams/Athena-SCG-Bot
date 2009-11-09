#ifndef _RELATION_H_
#define _RELATION_H_

#include <stdint.h>

/**
 * Defined in relation_consts.h
 * MASKS = { 0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF }
 */
uint32_t
get_mask(int rank);

/**
 * Defined in relation_consts.h
 * MAGIC_NUMBERS = { 0x55555555, 0x33333333, 0x0F0F0F0F,
 *                   0x00FF00FF, 0x0000FFFF }
 */
uint32_t
get_magic_number(int rank, int var_p, int value);

/**
 * True if the variable position is irrelevant to the (rn, rank)
 */
int
is_irrelevant(uint32_t rn, int rank, int var_p);

/**
 * Counts the number of relevant variables in the given relation
 * @param rn the relation number whose number of relevant variables is to be counted
 * @param rank rank of the given relation
 * @return The number of relevant variables in the given relation
 */
int
num_relevant_variables(uint32_t rn, int rank);

/**
 * Returns the number of 1-bits in the given rn masked by the rank.
 * m = 2 ** (2 ** rank)
 * So, the 1-bits of (rn & m)
 * @param rn
 * @param rank
 * @return the number of 1-bits in rn limited by rank
 */
int
ones(uint32_t rn, int rank);

/**
 * @param rn
 * @param rank
 * @param num_true_vars used to identify a set of rows in the truth table
 * @return The number of corresponding rows in the truth table
 *         with the given number of true variables
 */
int
q(uint32_t rn, int rank, int num_true_vars);

/**
 * In relation_consts.h
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


/**
 * Reduce a given relationship number by assigning.
 * @param rn The relation number.
 * @param rank The rank of the relation number.
 * @param var_p The position (index) of the variable.
 * @param value The value (1 or 0) in which to set the variable.
 * @return The new reduced relation number.
 */
uint32_t
reduce(uint32_t rn, int rank, int var_p, int value);

/**
 * Swaps two variables in a relation. When variables are swapped, The truth
 * table order gets scrambled rows of the truth table needs to be reordered
 * to restore the correct truth table order. Here are two exmples showing
 * how the swap method works for two relations: 
 *      (22 x y z) x => z
 * We are swapping variables at positions 0, 2 i.e: x, z
 * 
 * (22 x y z), swap x, z
 * x=>z
 *   original relations   scrambled truth table   restored truth table
 * old# x y z  22  x=>z  ||  z y x  22  x=>z  ||  z y x   22  x=>z  old#
 * ----------------------||-------------------||-------------------------
 * 0    0 0 0   0   1    ||  0 0 0   0   1    ||  0 0 0    0   1      0
 * 1    0 0 1   1   1    ||  1 0 0   1   1    ||  0 0 1    1   0      4
 * 2    0 1 0   1   1    ||  0 1 0   1   1    ||  0 1 0    1   1      2
 * 3    0 1 1   0   1    ||  1 1 0   0   1    ||  0 1 1    0   0      6
 * 4    1 0 0   1   0    ||  0 0 1   1   0    ||  1 0 0    1   1      1
 * 5    1 0 1   0   1    ||  1 0 1   0   1    ||  1 0 1    0   1      5
 * 6    1 1 0   0   0    ||  0 1 1   0   0    ||  1 1 0    0   1      3
 * 7    1 1 1   0   1    ||  1 1 1   0   1    ||  1 1 1    0   1      7
 */
uint32_t
swap(uint32_t rn, int rank, int var_p1, int var_p2);

/**
 * @param rn The relation number
 * @param rank The rank of the given relation.
 * @param perm_semantics specifies how the permutation should be applied.
 *                       Either SOURCE or TARGET.
 * for example:
 *    for the relation: R(v2,v1,v0)
 *    and the permutation {1,2,0} 
 *    SOURCE semantics means that v0 goes to position1, v1 goes to position2,
 *           v2 goes to position 0
 *    TARGET semantics means that position0 gets v1, position1 gets v2,
 *           position2 gets v0
 * @param permutation an array of variable positions describing the desired
 *                    location of every variables
 * For example:
 *    for the relation: R(v2,v1,v0)
 *    and the permutation {1,2,0} means v0 goes to position1, v1 goes to
 *        position2, v2 goes to position 0
 * @return the modified rn
 */
uint32_t
renme(uint32_t rn, int rank, int perm_semantics, int *permutation);

/**
 * Replaces the relation by its complement.
 * For example, n_mapping x in Or(x,y,z) results in Or(!x,y,z).
 * @param rn The relation number. 
 * @param rank The rank of the given relation.
 * @param var_p The position of the variable to be nmapped.
 * @return The number of the given relation with the specified variable
 *         nmapped.
 */
uint32_t
n_map(uint32_t rn, int rank, int var_p);

/**
 * a implies b.
 * @param a The first relation number.
 * @param b The second relation number.
 * @return a -> b
 */
int
implies(uint32_t a, uint32_t b);

/**
 * @param rn The relation number.
 * @param rank The rank.
 * @param var_p The position of the variable.
 * @return 0 if forced to 0,
 *         1 if forced to 1.
 *        -1 if not forced.
 */
int
is_forced(uint32_t rn, int rank, int var_p);

/*
 * Starting at the given starting position, get the position of the first
 * variable forced by the given relation.
 * @param rn The relation number.
 * @param rank The rank of the given relation number.
 * @param start_p The starting position.
 * @return if nothing is forced = -1
 *         else = the position of the first forced variable
 */
int
first_forced_variable(uint32_t rn, int rank, int start_p);

#endif
