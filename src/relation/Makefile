

CC = gcc
CFLAGS = -std=c99 -O3 -fforce-addr -funroll-loops -frerun-cse-after-loop \
         -frerun-loop-opt -falign-functions=4 -Wall -pedantic -lpthread \
		 -I./include 

OBJECTS = relation_pyx.o relation.o poly.o solve.o

all: relation.so

relation.so: $(OBJECTS)
	$(CC) $(CFLAGS) -shared $^ -o $@

relation_pyx.o: relation_pyx.c
	$(CC) $(CFLAGS) -fPIC -I/usr/include/python2.6 -c -o $@ $^

%_test: %.o relation.o poly.o tests/%_test.c
	$(CC) $(CFLAGS) -lm $^ -o $@

%.o: src/%.c
	$(CC) $(CFLAGS) -c -o $@ $^

%_pyx.c: %.pyx
	pyrexc $^ -o $@

.PHONY : clean test test_full

test: all poly_test solve_test relation_test
	python tests.py
	./poly_test
	./solve_test
	./relation_test

test_full: test
	python solve_test.py

clean:
	$(RM) relation_pyx.c *.o *.so *.pyc *_test
