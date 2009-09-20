CFLAGS = -std=c99 -O3 -fforce-addr -funroll-loops -frerun-cse-after-loop \
         -frerun-loop-opt -falign-functions=4 -Wall -pedantic

relation.so: relation.o _relation.o

%.so: %.o
	$(CC) $(CFLAGS) -shared $< -o $@

%.o: %.c
	$(CC) $(CFLAGS) -fPIC -I. -I/usr/include/python2.6 -c -o $@ $<
	
%.c: %.pyx
	pyrexc $< -o $@

all: relation.so

test: all
	python tests.py

clean:
	$(RM) relation.c *.o *.so
