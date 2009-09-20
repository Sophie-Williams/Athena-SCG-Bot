#ifndef _RELATION_H_
#define _RELATION_H_

#include <stdint.h>

uint32_t
_get_mask(int rank);

uint32_t
_get_magic_number(int rank, int var_p, int value);

int
_is_irrelevant(uint32_t rn, int rank, int var_p);

int
_num_relevant_variables(uint32_t rn, int rank);

#endif
