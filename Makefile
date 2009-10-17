# build "binary" version


all:
	python ./setup.py install
	find ./scaffold/ -name '*.py' -perm 0644 -delete 2>/dev/null
	cp ./scripts/README ./scaffold
	$(MAKE) --directory=./src/relation clean
	$(MAKE) --directory=./src/relation
	# setup.py does not to a good job.
	cp ./src/relation/relation.so ./scaffold/relation

clean:
	$(RM) -r ./scaffold ./build
