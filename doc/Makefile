all: finalproject.pdf

finalproject.dvi: finalproject.tex finalproject.bib
	latex finalproject
	bibtex finalproject
	latex finalproject
	latex finalproject

finalproject.pdf: finalproject.dvi
	dvipdf finalproject.dvi

clean:
	git clean -f -X . 
