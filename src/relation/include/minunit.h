/* http://www.jera.com/techinfo/jtns/jtn002.html */
/* file: minunit.h */

#include <stdio.h>
#include <sys/time.h>

#define mu_assert(message, test) do { if (!(test)) { return message; } \
                                 asserts++; printf("."); } while (0)
#define mu_run_test(test) do { char *message = test(); tests_run++; \
                               if (message) return message; } while (0)

#define NUM_MICROSECONDS_IN_SECOND 1000000

extern int tests_run;
extern int asserts;
extern char *all_tests();

int main(int argc, char **argv) {
    char *result;
    struct timeval tv_start, tv_end;
    int sec, usec;
    double real;

    gettimeofday(&tv_start, NULL); /* MARK start */

    result = all_tests();

    gettimeofday(&tv_end, NULL);   /* MARK end */

    /* second, microsecond */
    sec = tv_end.tv_sec - tv_start.tv_sec;
    usec = tv_end.tv_usec - tv_start.tv_usec;

    if (usec < 0) {
        sec -= 1;
        usec += NUM_MICROSECONDS_IN_SECOND;
    }

    real = sec + (((double) usec) / NUM_MICROSECONDS_IN_SECOND);

    if (result != 0)
        printf("%s\n", result);
    else
        printf("\n-----------------------------------"
               "-----------------------------------\n"
               "Ran %d tests in %fs\n\nOK\n", asserts, real);
    return result != 0;
}
