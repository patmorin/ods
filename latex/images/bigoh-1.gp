set term lua tikz size 5in,3in
set output 'bigoh-1.tex'
set xlabel '{\color{var}\texttt{n}}'
set ylabel '$f({\color{var}\mathtt{n}})$'
set key right bottom
set xrange [1:100]
#set style line 1 linecolor rgb '#0060ad' dashtype 1 linewidth 2
#set style line 2 linecolor rgb '#dd181f' dashtype 2 linewidth 2 
set linetype 1 dashtype 2                                                           
set linetype 2 dashtype '..-'
plot 15*x title '$15{\mathtt{n}}$', \
     2*x*(log(x)/log(2)) title '$2{\mathtt{n}}\log{\mathtt{n}}$' 

