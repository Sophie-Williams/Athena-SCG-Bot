#include <stdint.h>
#include <assert.h>
#include "relation_consts.h"
#include "_relation.h"


/*
 * defined in relation_consts.h
 * MASKS = { 0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF }
 */
uint32_t
get_mask(int rank) {
    assert(0 < rank);
    assert(rank <= 5);

    return MASKS[rank];
}

uint32_t
get_magic_number(int rank, int var_p, int value) {
    uint32_t mask = get_mask(rank);
    uint32_t col = ((MAGIC_NUMBERS[var_p]) & mask);
    if (value == 1)
        return mask ^ col;
    return col;
}

int
is_irrelevant(uint32_t rn, int rank, int var_p) {
    uint32_t m = get_magic_number(rank, var_p, 1);

    return ((rn & m) >> (1 << var_p)) == (rn & (~m));
}

int
num_relevant_variables(uint32_t rn, int rank) {
    int rel_vars;
    int i;

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
    return __builtin_popcountl(rn & get_mask(rank));
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
    assert(0 < rank && rank < 6);
    assert(0 <= num_true_vars && num_true_vars < 6);
    return TRUE_VARS[rank][num_true_vars];
}

uint32_t
reduce(uint32_t rn, int rank, int var_p, int value) {
    uint32_t magic_num;
    uint32_t rn_masked;

    magic_num = get_magic_number(rank, var_p, value);
    rn_masked = (rn & get_magic_number(rank, var_p, value));

    if (!value)
        return rn_masked | (rn_masked << (1 << var_p));
    return rn_masked | (rn_masked >> (1 << var_p));
}
