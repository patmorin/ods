#!/usr/bin/perl

require 5.001;
use strict;
use warnings;

MAIN: {
  print("\\begin{verbatim}\n");
  my $args = join("", @ARGV);
  my @params = split(/\./, $args);
  my $class=$params[0];
  my $basedir="../java/";
  my $javafile="${basedir}${class}.java";
  open(FP, "<",$javafile) || die("Unable to open $javafile");
  while (my $line = <FP>) {
    if ($line =~ /^\s*(public|protected|private)?\s+\w+\s+(\w+)\(.*\)/) {
      $line =~ /(\w+)\s*\(/;
      my $method = $1;
      $line =~ /(\(.*\))/;
      my $parms = $1;
      $parms =~ s/<.*?>//g;
      $parms =~ s/\s*\w+\s+(\w+)/$1/g;
      my $print = 0;
      foreach  my $i (1 .. $#params) {
        if ("$method$parms" eq $params[$i]) {
          $print = 1;
        }
      }
      if ($print) {
        my $depth = 0;
        do {
          $line =~ s/\t/  /g;
          $line =~ s/(public|protected|private|static)\s*//g;
          print("$line");
          $depth += $line =~ /\{/g;
          $depth -= $line =~ /\}/g;
        } while ($depth > 0 && ($line = <FP>));
        print("\n");
      }
    }
  }
  print("\\end{verbatim}\n");
} 

close(FP);
