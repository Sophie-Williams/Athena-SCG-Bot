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
    uint32_t c_swap "swap" (uint32_t rn, int rank, int var_p1, int var_p2)
    int c_q "q" (uint32_t rn, int rank, int num_true_vars)
    uint32_t c_reduce "reduce" (uint32_t rn, int rank, int var_p, int value)


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
        clause *clauses
        int num_clauses
    ctypedef __problem problem

    solution *solve(problem *problem, solution *solution)
    problem *problem_create(char **vars, int num_vars, clause *clauses,
                            int num_clauses)
    problem *problem_shallow_copy(problem *p)
    void problem_shallow_delete(problem *p)
    void problem_set(problem *problem, char **vars, int num_vars,
                     clause *clauses, int num_clauses)
    void problem_delete(problem *problem)
    solution *solution_create(problem *problem)
    void solution_delete(solution *solution)
    clause *clause_create(uint32_t rn, int rank, int *vars)
    void clause_set(void *c, uint32_t rn, int rank, int *vars)
    void clause_delete(clause *clause)
    int fsat(problem *p, solution *s)
    problem *problem_reduce_all(problem *p, int var, int value)
    solution *solve_iterate(problem *p, solution *s)
    solution *__solve_iterate(problem *p, solution *s,
                              uint32_t start, uint32_t end)


# for the module
SOURCE = C_SOURCE
TARGET = C_TARGET

def get_magic_number(int rank, int var_p, int value):
    """
    defined in relation_consts.h
    MAGIC_NUMBERS = ( 0x55555555, 0x33333333, 0x0F0F0F0F,
                      0x00FF00FF, 0x0000FFFF )
    """
    return c_get_magic_number(rank, var_p, value)

def get_mask(int rank):
    """
    defined in relation_consts.h
    MASKS = ( 0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF )
    """
    return c_get_mask(rank)

def is_irrelevant(uint32_t rn, int rank, int var_p):
    return c_is_irrelevant(rn, rank, var_p)

def num_relevant_variables(uint32_t rn, int rank):
    """
    Counts the number of relevant variables in the given relation
    @param rn the relation number whose number of relevant variables
              is to be counted
    @param rank rank of the given relation
    @return The number of relevant variables in the given relation
    """
    return c_num_relevant_variables(rn, rank)

def is_forced(uint32_t rn, int rank, int var_p):
    """
    Checks if the given relation forces the given var_p
    @param rn
    @param rank rank of the given relation
    @param var_p positon of the varible checked for being forced
    @return 0 if the given relation force the given variable to 0
            1 if the given relation force the given variable to 1
            -1 given relation doesn't force the given variable
    """
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

def first_forced_variable(uint32_t rn, int rank, int start_p):
    """
    Starting at the given starting position, get the position of the first
    variable forced by the given relation.
    @param rn The relation number.
    @param rank The rank of the given relation number.
    @param start_p
    @return if nothing is forced = -1
            else = the position of the first forced variable
    """
    for var_p in range(start_p, rank):
        if is_forced(rn, rank, var_p) != -1:
            return var_p

def n_map(uint32_t rn, int rank, int var_p):
    """
    NMaps one of the variables in a relation i.e. replaces it by it's
    complement for example: nMapping x in Or(x,y,z) results in: or(!x,y,z)
    @param rn The relation number. 
    @param rank The rank of the given relation.
    @param var_p The position of the variable to be nmapped.
    @return The number of the given relation with the specified variable
            nmapped.
    """
    cdef uint32_t m0
    cdef uint32_t m1
    cdef uint32_t s

    m0 = get_magic_number(rank, var_p, 0)
    m1 = get_magic_number(rank, var_p, 1)
    s = (1 << var_p)
    return ((rn & m0) << s) | ((rn & m1) >> s)

def reduce(uint32_t rn, int rank, int var_p, int value):
    """
    Reduces a relation by assigning a value to one of its variables.
    @param rn The relation number.
    @param rank The rank of the relation number.
    @param var_p The position of the variable.
    @param value The value at the given position.
    @return The reduced relation number.
    """
    return c_reduce(rn, rank, var_p, value)

def swap(uint32_t rn, int rank, int var_p1, int var_p2):
    """
    Swaps two variables in a relation. When variables are swapped, The truth
    table order gets scrambled rows of the truth table needs to be reordered
    to restore the correct truth table order. Here are two exmples showing
    how the swap method works for two relations: 
            1in3(x,y,z), x implies z.
    We are swapping variables at positions 0,2 i.e: x ,z

    @param rn The relation number.
    @param rank The rank of the relation number.
    @param var_p1 The first position.
    @param var_p2 The second position.
    @return The swapped relation number.
    """
    return c_swap(rn, rank, var_p1, var_p2)

def renme(uint32_t rn, int rank, int perm_semantics, permutation):
    """
    Permute the variables in the given rn according to the given permutation
    fix the truth table order after doing the permutation. @see swap
    @param rn The relation number.
    @param rank The rank of the given relation.
    @param perm_semantics Specifies how the permutation should be applied.
                          can be either SOURCE or TARGET.
    For example:
       for the relation: R(v2,v1,v0)
       and the permutation {1,2,0} 
       SOURCE semantics means that v0 goes to position1, v1 goes to position2,
              v2 goes to position 0
       TARGET semantics means that position0 gets v1, position1 gets v2,
              position2 gets v0
    @param permutation an array of variable positions describing the desired
                       location of every variables
    For example:
       for the relation: R(v2,v1,v0)
       and the permutation {1,2,0} means v0 goes to position1, v1 goes to
           position2, v2 goes to position 0
    @return The modified relation number.
    """
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

def ones(uint32_t rn, int rank):
    """
    Return the number of 1-bits in the given rn masked by the rank.
    m = 2 ** (2 ** rank)
    so, the 1-bits of (rn & m)
    @param rn The relation number.
    @param rank The rank of the relation number.
    @return The number of 1-bits in rn limited by rank.
    """
    return c_ones(rn, rank)

def q(uint32_t rn, int rank, int num_true_vars):
    """
    @param rn The relation number.
    @param rank The rank of the relation number.
    @param num_true_vars Used to identify a set of rows in the truth table.
    @return Number of ones corresponding to truth table rows
            with the given number of true variables.
    """
    return c_q(rn, rank, num_true_vars)

def x_true_vars(int rank, int num_true_vars):
    """
    @param rank The rank.
    @param num_true_vars The number of true variables.
    @return An integer representing the relation number which is true
            only when the given number of vars is true.

    This is in relation_consts.h
    TRUE_VARS = (
        (),
        (1, 2),
        (1, 6, 8),
        (1, 22, 104, 128),
        (1, 278, 5736, 26752, 32768)
        (1, 65814, 18224744, 375941248, 1753251840, 2147483648L),
    )
    """
    return TRUE_VARS[rank][num_true_vars]


# rank should be 3 for now.
def break_even(uint32_t rn, int rank):
    return find_break_even(rn, rank)


cdef rn_counts(problem *p):
    cdef int i

    counts = {}

    for 0 <= i < p[0].num_clauses:
        rn = (p[0].clauses + i)[0].rn
        if counts.has_key(rn):
            counts[rn] += 1
        else:
            counts[rn] = 1

    return counts


cdef mean(problem *p, int num_vars, int num_vars_true):
    sum = 0.0
    # FIXME variable rank.
    rank = 3
    for rn, count in rn_counts(p).items():
        sum = sum + sat(p, rn, rank, num_vars, num_vars_true) * count
    return sum / p[0].num_vars


cdef sat(problem *p, uint32_t rn, int rank, int n, int k):
    sum = 0.0
    for j in range(0, rank + 1):
        sum = sum + ((float(c_q(rn, rank, j)) / pascal(rank, j))
                    * pascal(k, j) * pascal(n - k, rank - j))

    return sum / pascal(n, rank)


def pascal(n, k):
    if (n <= 0 or k <= 0 or n == k):
        return 1

    result = 1
    for x in range(n - k + 1, n + 1):
        result *= x
    for y in range(2, k + 1):
        result /= y

    return result


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

    def rn_counts(self):
        cdef int i
        
        counts = {}

        for 0 <= i < self.p[0].num_clauses:
            rn = (self.p[0].clauses + i)[0].rn
            if counts.has_key(rn):
                counts[rn] += 1
            else:
                counts[rn] = 1

        return counts

    def evergreen(self):
        cdef problem *f_true
        cdef problem *f_false
        cdef problem *f
        cdef problem *tmp
        cdef solution *s
        cdef int var
        cdef int i

        n = self.p[0].num_vars
        max_num_true = 0
        max_mean = mean(self.p, n, 0)

        for t in range(1, self.p[0].num_vars):
            x = mean(self.p, n, t)
            if x > max_mean:
                max_mean = x
                max_num_true = t

        f = problem_shallow_copy(self.p)

        k = max_num_true
        j = []
        for 0 <= var < self.p[0].num_vars:
            f_true  = problem_shallow_copy(f)
            f_false = problem_shallow_copy(f)
            problem_reduce_all(f_true,  var, 1)
            problem_reduce_all(f_false, var, 0)

            mean_true  = mean(f_true, n - 1, k - 1)
            mean_false = mean(f_false, n - 1, k)
            tmp = f

            if mean_true > mean_false:
                j.append(1)
                k = k - 1
                f = f_true
                problem_shallow_delete(f_false)
            else:
                j.append(0)
                f = f_false
                problem_shallow_delete(f_true)

            # free the old `f`
            problem_shallow_delete(tmp)

        # free the last `f` we don't need it.
        problem_shallow_delete(f)

        s = <solution *> malloc(sizeof(solution))
        s[0].values = <int *> malloc(sizeof(int) * len(j))
        s[0].size = len(j)

        # copy the solution into C
        for 0 <= i < len(j):
            s[0].values[i] = j[i]

        # find the number of clauses satisfied
        satisfied = fsat(self.p, s)

        solution_delete(s)

        return (satisfied, j)

    def solve(self):
        cdef solution *s
        cdef int i

        s = solution_create(self.p)
        solve(self.p, s)

        # Copy over the solution.
        ret = []
        for 0 <= i < s[0].size:
            ret.append((s[0].values)[i])

        # Record the number of satisified clauses.
        f = fsat(self.p, s)
        solution_delete(s)

        return (f, ret)

    def solve_iterate_range(self, start, end):
        cdef solution *s
        s = solution_create(self.p)
        __solve_iterate(self.p, s, start, end)

        ret = []
        for 0 <= i < s[0].size:
            ret.append((s[0].values)[i])

        f = fsat(self.p, s)
        solution_delete(s)

        return (f, ret)

    def __dealloc__(self):
        problem_delete(self.p)
