CFLAGS = -std=c99 -O3 -fforce-addr -funroll-loops -frerun-cse-after-loop \
         -frerun-loop-opt -falign-functions=4 -Wall -pedantic

relation.so: relation.o _relation.o poly.o

poly_test: poly.o _relation.o poly_test.c
	$(CC) $(CFLAGS) -lm -I. -o poly_test poly.o _relation.o poly_test.c

poly.o: poly.c
	$(CC) $(CFLAGS) -lm -fPIC -I. -c -o $@ poly.c

%.so: %.o
	$(CC) $(CFLAGS) -shared $< -o $@

%.o: %.c
	$(CC) $(CFLAGS) -fPIC -I. -I/usr/include/python2.6 -c -o $@ $<
	
%.c: %.pyx
	pyrexc $< -o $@

all: relation.so

test: all poly_test
	python tests.py
	./poly_test


clean:
	$(RM) relation.c *.o *.so poly_test
