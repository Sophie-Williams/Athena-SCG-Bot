#ifndef RELATION_CONSTS_H_
#define RELATION_CONSTS_H_

#include <stdint.h>


#define SOURCE 0
#define TARGET 1

#define MIN_RANK 1
#define MAX_RANK 5
#define VALID_RANK(x) (MIN_RANK <= (x) && (x) <= MAX_RANK)

/**
 * MAGIC_NUMBERS. bit-masks for the columns of the rows (in aspect to the
 * relation numbers).
 */
static const uint32_t MAGIC_NUMBERS[] = {
    0x55555555, 0x33333333,
    0x0F0F0F0F, 0x00FF00FF,
    0x0000FFFF
};

/**
 * TRUE_VARS
 */
static const uint32_t TRUE_VARS[][6] = {
    {0, 0, 0, 0, 0, 0},
    {1, 2, 0, 0, 0, 0},
    {1, 6, 8, 0, 0, 0},
    {1, 22, 104, 128, 0, 0},
    {1, 278, 5736, 26752, 32768, 0},
    {1, 65814, 18224744, 375941248, 1753251840, 2147483648L}
};

/**
 * MASKS. bit-masks for each rank.
 */
static const uint32_t MASKS[] = {
    0x00000001, 0x00000003, 0x0000000F,
    0x000000FF, 0x0000FFFF, 0xFFFFFFFF
};

#endif
