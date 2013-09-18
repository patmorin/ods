set term tikz color size 5in,3in
set output 'bigoh-1.tex'
set xlabel '{\color{var}\texttt{n}}'
set ylabel '$f({\color{var}\mathtt{n}})$'
set key right bottom
set xrange [1:100]
set style line 1 linecolor rgb '#0060ad' linetype 1 linewidth 2
set style line 2 linecolor rgb '#dd181f' linetype 2 linewidth 2
plot 15*x title '$15{\mathtt{n}}$' with lines linestyle 1, \
     2*x*(log(x)/log(2)) title '$2{\mathtt{n}}\log{\mathtt{n}}$' with lines linestyle 2

