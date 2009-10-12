#include <stdint.h>
#include <assert.h>
#include "relation_consts.h"
#include "relation.h"


/* Defined in relation_consts.h
 * MASKS = { 0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF }
 */
uint32_t
get_mask(int rank) {
    assert(VALID_RANK(rank));
    return MASKS[rank];
}

uint32_t
get_magic_number(int rank, int var_p, int value) {
    register uint32_t mask = get_mask(rank);
    register uint32_t col = ((MAGIC_NUMBERS[var_p]) & mask);
    if (value == 1)
        return mask ^ col;
    return col;
}

int
is_irrelevant(uint32_t rn, int rank, int var_p) {
    register uint32_t m = get_magic_number(rank, var_p, 1);

    return ((rn & m) >> (1 << var_p)) == (rn & (~m));
}

int
num_relevant_variables(uint32_t rn, int rank) {
    register int rel_vars;
    register int i;

    rel_vars = rank;
    for (i = 0; i < rank; i++) {
        if (is_irrelevant(rn, rank, i)) {
            rel_vars--;
        }
    }
    return rel_vars;
}

int
ones(uint32_t rn, int rank) {
#ifdef __GNUC__
    return __builtin_popcountl(rn & get_mask(rank));
#else
    /* The slower version */
    int c;
    int i;

    c = 0;
    for (i = 0; i < (1 << rank); i++) {
        if ((rn & (1 << i)) != 0)
            c++;
    }
    return c
#endif
}

int
q(uint32_t rn, int rank, int num_true_vars) {
    uint32_t m;
    
    if (num_true_vars > rank) {
        return -1;
    }

    m = x_true_vars(rank, num_true_vars);
    return ones(rn & m, rank);
}

uint32_t
x_true_vars(int rank, int num_true_vars) {
    assert(VALID_RANK(rank));
    assert(0 <= num_true_vars && num_true_vars < 6);
    return TRUE_VARS[rank][num_true_vars];
}

uint32_t
reduce(uint32_t rn, int rank, int var_p, int value) {
    register uint32_t rn_masked;

    rn_masked = (rn & get_magic_number(rank, var_p, value));

    if (!value)
        return rn_masked | (rn_masked << (1 << var_p));
    return rn_masked | (rn_masked >> (1 << var_p));
}


uint32_t
swap(uint32_t rn, int rank, int var_p1, int var_p2) {
    int tmp;
    uint32_t d10;
    uint32_t d11;
    uint32_t d20;
    uint32_t d21;
    uint32_t same_filter;
    uint32_t up_filter;
    uint32_t down_filter;
    uint32_t shift_amt;
    uint32_t s;
    uint32_t u;
    uint32_t d;

    /* Swapping the variable with itself */
    if (var_p1 == var_p2)
        return rn;

    if (var_p1 > var_p2) {
        tmp = var_p1;
        var_p1 = var_p2;
        var_p2 = tmp;
    }
    
    d10 = get_magic_number(rank, var_p1, 0);
    d11 = get_magic_number(rank, var_p1, 1);
    d20 = get_magic_number(rank, var_p2, 0);
    d21 = get_magic_number(rank, var_p2, 1);

    /* Rows with either 0 or 1 in both columns stay the same
     * e.g. row 0, row 7, other rows depending on the two swapped columns
     * same_filter selects these rows based on the columns
     */
    same_filter = (d11 & d21) | (d10 & d20);
    /* Assuming that column1 is to the right of column2 which is valid by
     * the swapping we do at the begining of this method Rows where column1
     * is 0 and column2 is 1 must be moved up because after doing the swap
     * we'll have a 0 in column 2 and 1 in column 1 simply because the
     * 0 in column2 means that the row number of column2 becomes smaller
     * than the row number of column1. to restore the proper numbering
     * we have to swap the two rows
     */
    up_filter = d21 & d10;
    /* Select rows to be moved down. the reasoning is similar to up_filter */
    down_filter = d20 & d11;
    /* shift_amount is the difference between the row number in the before
     * we do the swap and the row number after we do the swap for example
     * if we are swapping the variable at position 2 with the variable at
     * position 0 then the rows to be swapped are of the form bbbbb1b0
     * and bbbbb0b1 where b stands for an arbitrary bits that stays the same.
     */

    /* Noting that:
     * bbbbb1b0 - bbbbb0b1 == (bbbbb1b0-bbbbb0b0) - (bbbbb0b1-bbbbb0b0)
     * by subtracting bbbbb0b0 from both numbers we get
     * 00000100 and 00000001
     * Therefore we need to shift both ways by 2^var_p2 - 2^var_p1
     */
    shift_amt = (1 << var_p2) - (1 << var_p1);
    
    /* rows to stay at their locations */
    s = rn & same_filter;
    /* rows to go up */
    u = rn & up_filter;
    /* rows to go down */
    d = rn & down_filter;
    
    /* move the rows and combine the three components */
    return (s | (d << shift_amt) | (u >> shift_amt));
}



uint32_t
renme(uint32_t rn, int rank, int perm_semantics, int *permutation) {
    uint32_t new_rn;
    int i;
    int j;
    int min;
    int tmp;

    assert(VALID_RANK(rank));
    assert(perm_semantics == SOURCE || perm_semantics == TARGET);

    new_rn = 0;

    if (perm_semantics == SOURCE) {
        for (i = 0; i < rank; i++) {
            new_rn += (rn & (1 << *(permutation+i)));
        }
        return new_rn;
    }

    /* sort dimensions */

    for (i = 0; i < rank - 1; i++) {
        min = i;
        for (j = i + 1; j < rank; j++) {
            if (permutation[j] < permutation[min]) {
                /* if min greater, */
                min = j;
            }

            /* swap elements at min, i */
            tmp = permutation[i];
            permutation[i] = permutation[min];
            permutation[min] = tmp;

            if (perm_semantics == SOURCE)
                rn = swap(rn, rank, permutation[min], permutation[i]);
            else
                rn = swap(rn, rank, min, i);
        }
    }

    return rn;
}

