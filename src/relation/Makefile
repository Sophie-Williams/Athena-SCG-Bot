

CC = gcc
CFLAGS = -std=c99 -O3 -fforce-addr -funroll-loops -frerun-cse-after-loop \
         -frerun-loop-opt -falign-functions=4 -Wall -pedantic -lpthread

OBJECTS = relation.o _relation.o poly.o solve.o

all: relation.so

relation.so: $(OBJECTS)
	$(CC) $(CFLAGS) -shared $(OBJECTS) -o $@

poly_test: poly.o _relation.o poly_test.c
	$(CC) $(CFLAGS) -lm -I. -o poly_test poly.o _relation.o poly_test.c

poly.o: poly.c
	$(CC) $(CFLAGS) -lm -fPIC -I. -c -o $@ poly.c

solve_test: solve.o solve_test.c
	$(CC) $(CFLAGS) -lm solve.o solve_test.c -o solve_test

solve.o: solve.c
	$(CC) $(CFLAGS) -c -o solve.o solve.c

%.o: %.c
	$(CC) $(CFLAGS) -fPIC -I. -I/usr/include/python2.6 -c -o $@ $<
	
%.c: %.pyx
	pyrexc $< -o $@

test: all poly_test solve_test
	python tests.py
	./poly_test
	./solve_test

clean:
	$(RM) relation.c *.o *.so poly_test solve_test
