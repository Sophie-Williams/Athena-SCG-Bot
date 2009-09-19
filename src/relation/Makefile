CFLAGS = -O3

%.so: %.o
	$(CC) $(CFLAGS) -shared $< -o $@

%.o: %.c
	$(CC) $(CFLAGS) -fPIC -I/usr/include/python2.6 -c -o $@ $<
	
%.c: %.pyx
	pyrexc $< -o $@

all: relation.so

clean:
	$(RM) relation.c *.o *.so
