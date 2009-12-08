all: finalproject.pdf

finalproject.dvi: finalproject.tex
	latex finalproject
	latex finalproject
finalproject.pdf: finalproject.dvi
	dvipdf finalproject.dvi

clean:
	git clean -f -X . 
