# build "binary" version


all:
	python ./setup.py install
	find ./scaffold/ -name '*.py' -perm 0644 -delete 2>/dev/null
