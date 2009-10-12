

CC = gcc
CFLAGS = -std=c99 -O3 -fforce-addr -funroll-loops -frerun-cse-after-loop \
         -frerun-loop-opt -falign-functions=4 -Wall -pedantic -lpthread \
		 -I./include 

OBJECTS = relation.o _relation.o poly.o solve.o

all: relation.so

relation.so: $(OBJECTS)
	$(CC) $(CFLAGS) -shared $(OBJECTS) -o $@

relation.o: relation.c
	$(CC) $(CFLAGS) -fPIC -I/usr/include/python2.6 -c -o $@ relation.c

%_test: %.o _relation.o poly.o tests/%_test.c
	$(CC) $(CFLAGS) -lm $? -o $@

%.o: src/%.c
	$(CC) $(CFLAGS) -c -o $@ $<

%.c: %.pyx
	pyrexc $< -o $@

test: all poly_test solve_test
	python tests.py
	./poly_test
	./solve_test

test_full: test
	python solve_test.py

clean:
	$(RM) relation.c *.o *.so *.pyc poly_test solve_test
