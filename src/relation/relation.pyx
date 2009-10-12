"""
The semantics of the relation numbers are thus,

Where a binary number is represented from MSB to LSB, e.g.

        10010110 == 2^1 + 2^2 + 2^4 + 2^7

the rows represented by this (binary) number are (1, 2, 4, 7)

                  row | binary  | number
                  -----------------------
                  0   | 0 0 0 0 | 0
               -> 1   | 0 0 0 1 | 1 <-
               -> 2   | 0 0 1 0 | 1 <-
                  3   | 0 0 1 1 | 0
               -> 4   | 0 1 0 0 | 1 <-
                  5   | 0 1 0 1 | 0
                  6   | 0 1 1 0 | 0
               -> 7   | 0 1 1 1 | 1 <-
                        .
                        .
                        .

so, 10010110 => 2^1 + 2^2 + 2^4 + 2^7
              => (1, 2, 4, 7)
              => ([0 0 0 1], [0 0 1 0], [0 1 0 0], [0 1 1 1])
  for variables a, b, c, d
    (and [or !a !b !c  d]
         [or !a !b  c !d]
         [or !a  b !c !d]
         [or !a  b  c  d])


The rank of a number is the number of variables a clause can have.

"""


cdef extern from "stdlib.h":
    ctypedef unsigned long int size_t
    cdef void *malloc(size_t size)
    cdef void free(void *ptr)


cdef extern from "string.h":
    cdef char *strcpy(char *s1, char *s2)


cdef extern from "stdint.h":
    ctypedef unsigned long int uint32_t


cdef extern from "relation_consts.h":
    cdef uint32_t TRUE_VARS[][6]
    cdef uint32_t MAGIC_NUMBERS[]
    cdef uint32_t MASKS[]
    cdef int C_SOURCE "SOURCE"
    cdef int C_TARGET "TARGET"


cdef extern from "relation.h":
    int c_is_irrelevant "is_irrelevant" (uint32_t rn, int rank, int var_p)
    uint32_t c_get_mask "get_mask" (int rank)
    uint32_t c_get_magic_number "get_magic_number" (int rank, int var_p,
                                                    int value)
    int c_ones "ones" (uint32_t rn, int rank)
    int c_num_relevant_variables "num_relevant_variables" (uint32_t rn,
                                                           int rank)


cdef extern from "poly.h":
    cdef double find_break_even(uint32_t rn, int rank)


cdef extern from "solve.h":
    struct __solution:
        int *values
        int size
    ctypedef __solution solution

    struct __clause:
        uint32_t rn
        int rank
        int *vars
    ctypedef __clause clause

    struct __problem:
        char **vars
        int num_vars
        clause **clauses
        int num_clauses
    ctypedef __problem problem

    solution *solve(problem *problem, solution *solution)
    problem * problem_create(char **vars, int num_vars, clause *clauses,
                             int num_clauses)
    void problem_set(problem *problem, char **vars, int num_vars,
                     clause *clauses, int num_clauses)
    void problem_delete(problem *problem)
    solution *solution_create(problem *problem)
    void solution_delete(solution *solution)
    clause *clause_create(uint32_t rn, int rank, int *vars)
    void clause_set(void *c, uint32_t rn, int rank, int *vars)
    void clause_delete(clause *clause)
    int fsat(problem *p, solution *s)



# for the module
SOURCE = C_SOURCE
TARGET = C_TARGET


# GNU builtin
cdef extern int __builtin_popcountl(unsigned long)

# defined in relation_consts.h
# MAGIC_NUMBERS = ( 0x55555555, 0x33333333, 0x0F0F0F0F,
#                   0x00FF00FF, 0x0000FFFF )
def get_magic_number(int rank, int var_p, int value):
    return c_get_magic_number(rank, var_p, value)


# defined in relation_consts.h
# MASKS = ( 0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF )
def get_mask(int rank):
    return c_get_mask(rank)


def is_irrelevant(uint32_t rn, int rank, int var_p):
    return c_is_irrelevant(rn, rank, var_p)


# Counts the number of relevant variables in the given relation
# @param rn the relation number whose number of relevant variables is to be counted
# @param rank rank of the given relation
# @return The number of relevant variables in the given relation
def num_relevant_variables(uint32_t rn, int rank):
    return c_num_relevant_variables(rn, rank)


# Checks if the given relation forces the given var_p
# @param rn
# @param rank rank of the given relation
# @param var_p positon of the varible checked for being forced
# @return 0 if the given relation force the given variable to 0
#         1 if the given relation force the given variable to 1
#         -1 given relation doesn't force the given variable
def is_forced(uint32_t rn, int rank, int var_p):
    cdef uint32_t m
    cdef uint32_t rm

    if is_irrelevant(rn, rank, var_p):
        return -1
    else:
        m = get_magic_number(rank, var_p, 1)
        rm = rn & m;
        if rm == 0:
            return 0
        elif rm == rn:
            return 1
        else:
            return -1


# Starting at the given starting position, get the position of the first
# variable forced by the given relation.
# @param rn relation number
# @param rank rank of the given relation number
# @param start_p
# @return if nothing is forced = -1
#         else = the position of the first forced variable
def first_forced_variable(uint32_t rn, int rank, int start_p):
    # checkRank(rank);
    # checkRelationNumber(rn, rank);
    # checkVariablePosition(startPosition, rank);
   
    for var_p in range(start_p, rank):
        if is_forced(rn, rank, var_p) != -1:
            return var_p


# NMaps one of the variables in a relation i.e. replaces it by it's complement
# for example: nMapping x in Or(x,y,z) results in: or(!x,y,z)
# @param rn 
# @param rank rank of the given relation
# @param var_p the variable to be nmapped
# @return The number of the given relation with the specified variable nmapped
def n_map(uint32_t rn, int rank, int var_p):
    # checkRank(rank);
    # checkRelationNumber(rn, rank);
    # checkVariablePosition(var_p, rank);

    cdef uint32_t m0
    cdef uint32_t m1
    cdef uint32_t s

    m0 = get_magic_number(rank, var_p, 0)
    m1 = get_magic_number(rank, var_p, 1)
    s = (1 << var_p)
    return ((rn & m0) << s) | ((rn & m1) >> s)


# Reduces a relation by assigning a value to one of its variables
# @param rn
# @param rank
# @param var_p
# @param value
# @return
def reduce(uint32_t rn, int rank, int var_p, int value):
    # checkRank(rank)
    # checkRelationNumber(rn, rank)
    # checkVariablePosition(var_p, rank)
    # checkValue(value)

    cdef uint32_t m
    cdef uint32_t rm

    m = get_magic_number(rank, var_p, value)
    rm = (rn & m)

    if value == 0:
        return rm | (rm << (1 << var_p))
    else:
        return rm | (rm >> (1 << var_p))


# Swaps two variables in a relation. When variables are swapped, The truth
# table order gets scrambled rows of the truth table needs to be reordered
# to restore the correct truth table order. Here are two exmples showing
# how the swap method works for two relations: 
#         1in3(x,y,z), x implies z.
# We are swapping variables at positions 0,2 i.e: x ,z
# 
#      original relations            scrambled truth table      restored truth table ordering  
# Row# x y z 1in3(x,y,z)  x=>z  ||  z y x 1in3(z,y,x) x=>z  ||  z y x 1in3(z,y,x) x=>z old_Row#
# ------------------------------||--------------------------||---------------------------------
# 0    0 0 0   0           1    ||  0 0 0   0          1    ||  0 0 0    0         1      0
# 1    0 0 1   1           1    ||  1 0 0   1          1    ||  0 0 1    1         0      4
# 2    0 1 0   1           1    ||  0 1 0   1          1    ||  0 1 0    1         1      2
# 3    0 1 1   0           1    ||  1 1 0   0          1    ||  0 1 1    0         0      6
# 4    1 0 0   1           0    ||  0 0 1   1          0    ||  1 0 0    1         1      1
# 5    1 0 1   0           1    ||  1 0 1   0          1    ||  1 0 1    0         1      5
# 6    1 1 0   0           0    ||  0 1 1   0          0    ||  1 1 0    0         1      3
# 7    1 1 1   0           1    ||  1 1 1   0          1    ||  1 1 1    0         1      7
# 
# @param rn
# @param rank
# @param var_p1
# @param var_p2
# @return
def swap(uint32_t rn, int rank, int var_p1, int var_p2):
    # checkRank(rank);
    # checkRelationNumber(rn, rank);
    # checkVariablePosition(var_p1, rank);
    # checkVariablePosition(var_p1, rank);
    
    cdef int tmp
    cdef uint32_t d10
    cdef uint32_t d11
    cdef uint32_t d20
    cdef uint32_t d21
    cdef uint32_t same_filter
    cdef uint32_t up_filter
    cdef uint32_t down_filter
    cdef uint32_t shift_amt
    cdef uint32_t s
    cdef uint32_t u
    cdef uint32_t d

    # Swapping the variable with itself
    if var_p1 == var_p2:
        return rn;
    # swap Dim1,Dim2; 
    # Can't we tell java to automatically do the swap by just stating the condition that dim1>dim2
    if var_p1 > var_p2:
        tmp = var_p1
        var_p1 = var_p2
        var_p2 = tmp
    
    d10 = get_magic_number(rank, var_p1, 0)
    d11 = get_magic_number(rank, var_p1, 1)
    d20 = get_magic_number(rank, var_p2, 0)
    d21 = get_magic_number(rank, var_p2, 1) 
    
    # 1in3(x,y,z), swap x,z
    # x=>z
    #      original relations            scrambled truth table      restored truth table ordering  
    # Row# x y z 1in3(x,y,z)  x=>z  ||  z y x 1in3(z,y,x) x=>z  ||  z y x 1in3(z,y,x) x=>z old_Row#
    # ------------------------------||--------------------------||---------------------------------
    # 0    0 0 0   0           1    ||  0 0 0   0          1    ||  0 0 0    0         1      0
    # 1    0 0 1   1           1    ||  1 0 0   1          1    ||  0 0 1    1         0      4
    # 2    0 1 0   1           1    ||  0 1 0   1          1    ||  0 1 0    1         1      2
    # 3    0 1 1   0           1    ||  1 1 0   0          1    ||  0 1 1    0         0      6
    # 4    1 0 0   1           0    ||  0 0 1   1          0    ||  1 0 0    1         1      1
    # 5    1 0 1   0           1    ||  1 0 1   0          1    ||  1 0 1    0         1      5
    # 6    1 1 0   0           0    ||  0 1 1   0          0    ||  1 1 0    0         1      3
    # 7    1 1 1   0           1    ||  1 1 1   0          1    ||  1 1 1    0         1      7
    
    # Rows with either 0 or 1 in both columns stay the same
    # e.g. row 0, row 7, other rows depending on the two swapped columns
    # same_filter selects these rows based on the columns
    same_filter = d11 & d21 | d10 & d20
    # Assuming that column1 is to the right of column2 which is valid by
    # the swapping we do at the begining of this method Rows where column1
    # is 0 and column2 is 1 must be moved up because after doing the swap
    # we'll have a 0 in column 2 and 1 in column 1 simply because the
    # 0 in column2 means that the row number of column2 becomes smaller
    # than the row number of column1. to restore the proper numbering
    # we have to swap the two rows
    up_filter = d21 & d10
    # Select rows to be moved down. the reasoning is similar to up_filter
    dn_filter = d20 & d11
    # shift_amount is the difference between the row number in the before
    # we do the swap and the row number after we do the swap for example
    # if we are swapping the variable at position 2 with the variable at
    # position 0 then the rows to be swapped are of the form bbbbb1b0
    # and bbbbb0b1 where b stands for an arbitrary bits that stays the same.

    # Noting that:
    # bbbbb1b0 - bbbbb0b1 == (bbbbb1b0-bbbbb0b0) - (bbbbb0b1-bbbbb0b0)
    # by subtracting bbbbb0b0 from both numbers we get
    # 00000100 and 00000001
    # Therefore we need to shift both ways by 2^var_p2 - 2^var_p1
    
    shift_amt = (1 << var_p2) - (1 << var_p1)
    
    #  rows to stay at their locations
    s = rn & same_filter
    # rows to go up
    u = rn & up_filter
    # rows to go down
    d = rn & dn_filter
    
    # move the rows and combine the three components
    return (s | (d << shift_amt) | (u >> shift_amt))



# permute the variables in the given rn according to the given permutation
# fix the truth table order after doing the permutation. @see swap
# @param rn
# @param rank rank of the given relation
# @param perm_semantics specifies how the permutation should be applied.
#                       can be either SOURCE or TARGET
# for example:
#    for the relation: R(v2,v1,v0)
#    and the permutation {1,2,0} 
#    SOURCE semantics means that v0 goes to position1, v1 goes to position2,
#           v2 goes to position 0
#    TARGET semantics means that position0 gets v1, position1 gets v2,
#           position2 gets v0
# @param permutation an array of variable positions describing the desired
#                    location of every variables
# For example:
#    for the relation: R(v2,v1,v0)
#    and the permutation {1,2,0} means v0 goes to position1, v1 goes to
#        position2, v2 goes to position 0
# @return the modified rn
def renme(uint32_t rn, int rank, int perm_semantics, permutation):
    # checkRank(rank)
    # checkRelationNumber(rn, rank)
    # checkPermutation(rank, permutation)
    # checkPermutationSemantics(perm_semantics)

    cdef uint32_t new_rn
    new_rn = 0

    if perm_semantics == C_SOURCE:
        for x in permutation:
            new_rn = new_rn + (rn & (1 << x))
        return new_rn

    # sort dimensions
    cdef int i
    cdef int j
    cdef int min
    cdef int tmp

    for 0 <= i < rank - 1:
        min = i
        for i + 1 <= j < rank:
            if (permutation[j] < permutation[min]):
                # if min greater,
                min = j
        # swap elements at min, i
        permutation[i], permutation[min] = permutation[min], permutation[i]
        # tmp = permutation[i]
        # permutation[i] = permutation[min]
        # permutation[min] = tmp

        if perm_semantics == C_SOURCE:
            rn = swap(rn, rank, permutation[min], permutation[i])
        elif perm_semantics == C_TARGET:
            rn = swap(rn, rank, min, i)
        else:
            pass # error

    return rn


# returns the number of 1-bits in the given rn masked by the rank
# m = 2 ** (2 ** rank)
# so, the 1-bits of (rn & m)
# @param rn
# @param rank
# @return the number of 1-bits in rn limited by rank
def ones(uint32_t rn, int rank):
    return c_ones(rn, rank)
# The slower version.
#
# def ones(uint32_t rn, int rank):
#     # checkRank(rank)
#     # checkRelationNumber(rn, rank)
# 
#     cdef int c
#     cdef int i
# 
#     c = 0
#     for 0 <= i < (1 << rank):
#         if ((rn & (1 << i)) != 0):
#             c += 1
#     return c


# @param rn
# @param rank
# @param num_true_vars used to identify a set of rows in the truth table
# @return counts the number of ones corresponding to truth table rows
#         with the given number of true variables
def q(uint32_t rn, int rank, int num_true_vars):
    # checkRank(rank);
    # checkRelationNumber(rn, rank);

    cdef int m
    if num_true_vars > rank:
        return -1

    m = x_true_vars(rank, num_true_vars)
    return ones(rn & m, rank)


# for 1in3 use x_true_vars(3, 1)
# @param rank
# @param num_true_vars
# @return an integer representing the relation number which is true
#         only when the given number of vars is true
# this is in relation_consts.h
# TRUE_VARS = (
#     (),
#     (1, 2),
#     (1, 6, 8),
#     (1, 22, 104, 128),
#     (1, 278, 5736, 26752, 32768)
#     (1, 65814, 18224744, 375941248, 1753251840, 2147483648L),
# )
def x_true_vars(int rank, int num_true_vars):
    return TRUE_VARS[rank][num_true_vars]
#     cdef uint32_t rn
#     cdef int i
# 
#     rn = 0
#     
#     for 0 <= i < (1 << rank):
#         # ones(i, 3) a truth table row can have up to 5 columns
#         # ones(i, 3) will count ones up to the 8th column
#         if(ones(i, 3) == num_true_vars): 
#             rn |= (1 << i)
#     return rn


# rank should be 3 for now.
def break_even(uint32_t rn, int rank):
    return find_break_even(rn, rank)


def generate_variables(uint32_t amount):
    cdef uint32_t i
    x = list()

    for 0 < i < amount:
        x = x + 'alamo%dalamo' % i
    return x


# def create_prob(uint32_t rn, int rank):
#     vars = [v for v in generate_variables(1140)
#     return


# cdef class Clause:
#     cdef clause *c
# 
#     def __cinit__(self, rn, vars):
#         cdef int tmp[5]
# 
#         for v in vars:
#             tmp[x] = v
# 
#         self.c = clause_create(rn, len(vars), tmp)
# 
#     def __dealloc__(self):
#         clause_delete(self.c)


cdef class Problem:
    cdef problem *p

    def __cinit__(self, vars, clauses):
        cdef clause *tmp
        cdef int i
        cdef int j
        cdef int var_tmp[5]
        cdef char **c_vars

        tmp = <clause *> malloc(sizeof(clause) * len(clauses))

        # For all clauses, which is a list of the form
        # [ <relation_number>, <var>, ... ]
        # set it at the index `i'
        i = 0
        for c in clauses:
            j = 0
            for v in c[1:]:
                var_tmp[j] = vars.index(v)
                j = j + 1
            clause_set(tmp+i, c[0], j, var_tmp)
            i = i + 1

        # Allocate spaces for the pointers to the variable names.
        c_vars = <char **> malloc(sizeof(char *) * len(vars))

        j = 0
        for v in vars:
            c_vars[j] = <char *> malloc(len(v) + 1)
            strcpy(c_vars[j], v)
            j = j + 1

        self.p = <problem *> malloc(sizeof(problem))
        problem_set(self.p, c_vars, j, tmp, i)

    def solve(self):
        cdef solution *s
        cdef int i

        s = solution_create(self.p)
        solve(self.p, s)

        # copy over the solution.
        ret = []
        for 0 <= i < s[0].size:
            ret.append((s[0].values)[i])

        f = fsat(self.p, s)

        free(s)
        
        return (f, ret)

    def __dealloc__(self):
        problem_delete(self.p)
        self.p = <problem *> 0
