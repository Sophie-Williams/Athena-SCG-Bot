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
    void *malloc(size_t size)
    void free(void *ptr)

cdef extern from "string.h":
    char *strcpy(char *s1, char *s2)
    void *memset(void *s, int c, size_t n)

cdef extern from "stdint.h":
    ctypedef unsigned long int uint32_t

cdef extern from "relation_consts.h":
    uint32_t TRUE_VARS[][6]
    uint32_t MAGIC_NUMBERS[]
    uint32_t MASKS[]
    int C_SOURCE "SOURCE"
    int C_TARGET "TARGET"

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
    uint32_t c_n_map "n_map" (uint32_t rn, int rank, int var_p)
    int c_implies "implies" (uint32_t a, uint32_t b)
    uint32_t c_x_true_vars "x_true_vars" (int rank, int num_true_vars)
    int c_is_forced "is_forced" (uint32_t rn, int rank, int var_p)
    int c_first_forced_variable "first_forced_variable" (uint32_t rn, int rn,
                                                         int start_p)
    uint32_t c_renme "renme" (uint32_t rn, int rank, int perm_semantics,
                              int *permutation)

cdef extern from "poly.h":
    double find_break_even(uint32_t rn, int rank)

cdef extern from "solve.h":
    struct __solution:
        int *values
        int size
    ctypedef __solution solution

    struct __clause:
        uint32_t rn
        int rank
        int *vars
        uint32_t weight
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
    void clause_set(clause *c, uint32_t rn, int rank, int *vars)
    void clause_set_weighted(clause *c, uint32_t rn, int rank, int *vars,
                             int weight)
    void clause_delete(clause *clause)
    int sat(problem *p, solution *s)
    int problem_weight(problem *p)
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
    return c_num_relevant_variables(rn, rank)

def is_forced(uint32_t rn, int rank, int var_p):
    return c_is_forced(rn, rank, var_p)

def first_forced_variable(uint32_t rn, int rank, int start_p):
    return c_first_forced_variable(rn, rank, start_p)

def n_map(uint32_t rn, int rank, int var_p):
    return c_n_map(rn, rank, var_p)

def reduce(uint32_t rn, int rank, int var_p, int value):
    return c_reduce(rn, rank, var_p, value)

def swap(uint32_t rn, int rank, int var_p1, int var_p2):
    return c_swap(rn, rank, var_p1, var_p2)

def renme(uint32_t rn, int rank, int perm_semantics, permutation):
    cdef int *c_perm
    cdef int i
    c_perm = <int *> malloc(sizeof(int) * len(permutation))
    for 0 <= i < len(permutation):
        c_perm[i] = permutation[i]
    ret = c_renme(rn, rank, perm_semantics, c_perm)
    free(c_perm)
    return ret

def ones(uint32_t rn, int rank):
    return c_ones(rn, rank)

def q(uint32_t rn, int rank, int num_true_vars):
    return c_q(rn, rank, num_true_vars)

def x_true_vars(int rank, int num_true_vars):
    return c_x_true_vars(rank, num_true_vars)

def implies(uint32_t a, uint32_t b):
    return c_implies(a, b)

def reduce_rns(rns):
    """Reduce the relation numbers in the given list.
    >>> reduce_rns([2, 10, 26, 1, 3, 8])
    [1, 2, 8]
    >>> reduce_rns([3, 7])
    [3]
    """
    rns = list(rns)
    stop = 0
    for _ in range(0, len(rns)):
        for rn in rns:
            for x in rns:
                if rn != x and implies(rn, x):
                    rns.remove(x)
                    stop = 1
                    break
            if stop:
                stop = 0
                break
    return sorted(set(rns))

# rank should be 3 for now.
def break_even(uint32_t rn, int rank):
    return find_break_even(rn, rank)

cdef rn_counts(problem *p):
    cdef int i

    counts = {}

    for 0 <= i < p[0].num_clauses:
        rn = (p[0].clauses + i)[0].rn
        if counts.has_key(rn):
            counts[rn] = counts[rn] + 1
        else:
            counts[rn] = 1

    return counts

cdef mean(problem *p, int num_vars, int num_vars_true):
    sum = 0.0
    # FIXME variable rank.
    rank = 3
    for rn, count in rn_counts(p).items():
        sum = sum + SAT(p, rn, rank, num_vars, num_vars_true) * count
    return sum / p[0].num_vars

cdef SAT(problem *p, uint32_t rn, int rank, int n, int k):
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
        result = result * x
    for y in range(2, k + 1):
        result = result / y

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
        memset(tmp, 0, sizeof(clause) * len(clauses))

        # For all clauses, which is a list of the form
        # [ <relation_number>, weight, <var>, ... ]
        # set it at the index `i'
        i = 0
        for c in clauses:
            j = 0
            for v in c[2:]:
                var_tmp[j] = vars.index(v)
                j = j + 1
            clause_set_weighted(tmp+i, c[0], j, var_tmp, c[1])
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

    def get_all(self):
        cdef int i
        cdef int j
        cdef clause *c
        vars = []
        clauses = []

        for 0 <= i < self.p[0].num_vars:
            vars.append(self.p[0].vars[i])

        for 0 <= i < self.p[0].num_clauses:
            c = self.p[0].clauses + i
            tmp = [c[0].rn, c[0].weight]
            for 0 <= j < c[0].rank:
                tmp.append(vars[c[0].vars[j]])
            clauses.append(tmp)

        return vars, clauses

    def copy(self):
        vars, clauses = self.get_all()
        return Problem(vars, clauses)

    def weight(self):
        """The total weight of all clauses in the problem."""
        return problem_weight(self.p)

    def sat(self, assignment):
        """Return the number of satisfied clauses."""
        cdef solution *s

        s = <solution *> malloc(sizeof(solution))
        s[0].values = <int *> malloc(sizeof(int) * len(assignment))
        s[0].size = len(assignment)

        # copy the solution into C
        for 0 <= i < len(assignment):
            s[0].values[i] = assignment[i]

        # find the number of clauses satisfied
        satisfied = sat(self.p, s)

        solution_delete(s)
        return satisfied

    def fsat(self, assignment):
        return float(self.sat(assignment)) / problem_weight(self.p)

    def rn_counts(self):
        cdef int i

        counts = {}

        for 0 <= i < self.p[0].num_clauses:
            rn = (self.p[0].clauses + i)[0].rn
            if counts.has_key(rn):
                counts[rn] = counts[rn] + 1
            else:
                counts[rn] = 1

        return counts

    def n_map_all(self, assignment):
        cdef int i
        cdef int j
        cdef int k
        cdef int var_p
        cdef clause *c

        for 0 <= i < self.p[0].num_vars:
            # n_map only if the assignment is true
            if assignment[i]:
                for 0 <= j < self.p[0].num_clauses:
                    c = self.p[0].clauses + j
                    # search the clause for the variable.
                    var_p = -1
                    for 0 <= k < c[0].rank:
                        if c[0].vars[k] == i:
                            var_p = k
                            break
                    if var_p >= 0:
                        c[0].rn = c_n_map(c[0].rn, c[0].rank, var_p)

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

        return (self.sat(j), j)

    def evergreen_aggressive(self):
        import operator
        f = self.copy()
        A = [0] * (self.p[0].num_vars)
        oldratio = -1
        newratio = f.sat(A)
        max = [-1, None]
        count = 0
        while oldratio != newratio:
            oldratio = newratio
            newratio, m = f.evergreen()
            # Keep track of the maximum
            if max[0] < newratio:
                max = [newratio, m]
            # Search deeper
            if newratio <= oldratio:
                count = count + 1
                if count > 3:
                    break
            f.n_map_all(m)
            A = map(operator.xor, A, m)

        return max[0], max[1]

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
        f = sat(self.p, s)
        solution_delete(s)

        return (f, ret)

    def solve_iterate_range(self, start, end):
        cdef solution *s
        s = solution_create(self.p)
        __solve_iterate(self.p, s, start, end)

        ret = []
        for 0 <= i < s[0].size:
            ret.append((s[0].values)[i])

        f = sat(self.p, s)
        solution_delete(s)

        return (f, ret)

    def __dealloc__(self):
        if self.p[0].vars:
            problem_delete(self.p)
        else:
            problem_shallow_delete(self.p)
