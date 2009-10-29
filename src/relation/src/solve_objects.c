
/* START constructor/destructor */

problem *
problem_create(char **vars, int num_vars, clause *clauses, int num_clauses) {
    problem *p;
    int i;

    assert(num_vars > 0);
    assert(num_clauses > 0);
    assert(vars);
    assert(clauses);

    if ((p = malloc(sizeof(problem))) == NULL) {
        perror("malloc");
        return NULL;
    }

    p->vars = malloc(num_vars);

    for (i = 0; i < num_vars; i++) {
        *(p->vars+i) = malloc(strlen(*(vars+i)) + 1);
        strcpy(*(p->vars+i), *(vars+i));
    }

    p->clauses = calloc(num_clauses, sizeof(clause));
    memcpy(p->clauses, clauses, sizeof(clause) * num_clauses);

    return p;
}

problem *
problem_shallow_copy(const problem * restrict from) {
    problem *to;

    assert(from);

    if ((to = malloc(sizeof(problem))) == NULL) {
        perror("malloc");
        return NULL;
    }

    to->vars = NULL;
    to->num_vars = from->num_vars;
    to->num_clauses = from->num_clauses;

    if ((to->clauses = calloc(from->num_clauses, sizeof(clause))) == NULL) {
        perror("calloc");
        free(to);
        return NULL;
    }

    memcpy(to->clauses, from->clauses, sizeof(clause) * from->num_clauses);

    return to;
}

/* Set values for a problem. */
void
problem_set(problem *problem, char **vars, int num_vars, clause *clauses,
            int num_clauses) {
    assert(problem);
    problem->vars = vars;
    problem->num_vars = num_vars;
    problem->clauses = clauses;
    problem->num_clauses = num_clauses;
}

void
problem_delete(problem *p) {
    int i;

    for (i = 0; i < p->num_vars; i++) {
        free(*(p->vars+i));
    }

    free(p->vars);
    problem_shallow_delete(p);
}

void
problem_shallow_delete(problem *p) {
    free(p->clauses);
    free(p);
}

solution *
solution_create(const problem * restrict problem) {
    solution *s;

    assert(problem);

    s = malloc(sizeof(solution));
    if (s == NULL) {
        perror("malloc");
        return NULL;
    }

    s->values = malloc(sizeof(int) * problem->num_vars);
    s->size = problem->num_vars;

    if (s->values == NULL) {
        perror("malloc");
        free(s);
        return NULL;
    }

    memset(s->values, '\0', sizeof(int) * problem->num_vars);

    return s;
}

void
solution_delete(solution *s) {
    free(s->values);
    free(s);
}

clause *
clause_create_real(uint32_t rn, int rank, int *vars, uint32_t weight) {
    clause *c;
    register size_t size;

    assert(vars != NULL);

    size = sizeof(int) * rank;

    c = malloc(sizeof(clause));
    c->rn = rn;
    c->rank = rank;
    c->vars = malloc(size);
    memcpy(c->vars, vars, size);

    return c;
}

void
clause_set_real(clause *clause, uint32_t rn, int rank, int *vars,
                uint32_t weight) {
    register size_t size;
    assert(clause);

    size = sizeof(int) * rank;

    clause->rn = rn;
    clause->rank = rank;
    clause->weight = weight;

    /* Short-circuit if vars is not going to change. */
    if (!vars)
        return;

    if ((clause->vars = malloc(size)) == NULL) {
        perror("malloc");
        return;
    }
    memcpy(clause->vars, vars, size);
}

void
clause_delete(clause *clause) {
    assert(clause);
    free(clause->vars);
    free(clause);
}

/* END constructor/destructor */
