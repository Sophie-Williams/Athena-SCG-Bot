#include <stdint.h>
#include "relation_consts.h"
#include "_relation.h"


uint32_t
_get_mask(int rank) {
    return MASKS[rank];
}

uint32_t
_get_magic_number(int rank, int var_p, int value) {
    uint32_t mask = _get_mask(rank);
    uint32_t col = ((MAGIC_NUMBERS[var_p]) & mask);
    if (value == 1)
        return mask ^ col;
    return col;
}

int
_is_irrelevant(uint32_t rn, int rank, int var_p) {
    uint32_t m = _get_magic_number(rank, var_p, 1);

    return ((rn & m) >> (1 << var_p)) == (rn & (~m));
}

int
_num_relevant_variables(uint32_t rn, int rank) {
    int rel_vars;
    int i;

    rel_vars = rank;
    for (i = 0; i < rank; i++) {
        if (_is_irrelevant(rn, rank, i)) {
            rel_vars--;
        }
    }
    return rel_vars;
}

int
_ones(uint32_t rn, int rank) {
    return __builtin_popcountl(rn & _get_mask(rank));
}
