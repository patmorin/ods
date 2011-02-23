#!/usr/bin/perl

require 5.001;
use strict;
use warnings;

sub wanted($@) {
  my $sig = shift @_; 
  while (my $param = shift @_) {
    if ("$sig" eq $param) {
      return 1;
    }
  }
  return 0;
}

sub snarfit($) {
  my $w = "A-Za-z0-9<>\\[\\]";   # things that occur in type names
  my $k = "(static|public|protected|private)";  # boring keywords
  my $args = shift(@_);
  my @params = split(/\./, $args);
  my $class=$params[0];
  my $basedir="../java/";
  my $javafile="${basedir}${class}.java";
  my $d = 0;  # level of nesting
  my $print = 0;
  open(FP, "<",$javafile) || die("Unable to open $javafile");
  while (my $line = <FP>) {
    if ($d == 1) {
      if ($line =~ /^\s*($k\s+)*[$w]+\s+([$w]+)\(.*\)/) {
        # this is a method definition
        $line =~ /([$w]+)\s*\(/;
        my $method = $1;
        $line =~ /(\(.*\))/;
        my $parms = $1;
        $parms =~ s/<.*?>//g;
        $parms =~ s/\s*[$w]+\s+([$w]+)/$1/g;
        if (wanted("$method$parms", @params)) {
          $print = 1;
        }    
      } elsif ($line =~ /^\s*($k\s+)*[$w]+\s+([$w]+)\s*;/) {
        $line =~ /([$w]+)\s*;/;
        my $var = $1;
        # this is an instance variable declaration
        if (wanted($var, @params)) {
          $print = 1;
        }
      }
    }
    if ($print) {
      $line =~ s/($k)\s+//g;
      $line =~ s/Utils\.//g;
      $line =~ s/([^A-Za-z0-9])f\./\1/g;
      print($line);
    }
    while ($line =~ /\}/g) {
      $d--;
    }
    while ($line =~ /\{/g) {
      $d++;
    }
    if ($d == 1) {
      $print = 0;
    }
  }
} 

MAIN: {
  while (my $line = <STDIN>) {
    if ($line =~ /\\javaimport(withclass)?\{([^}#]+)\}/) {
      print("\\noindent\\begin{minipage}{\\textwidth}\n");
      print("\\begin{lstlisting}\n");
      snarfit($2);
      print("\\end{lstlisting}\n");
      print("\\end{minipage}\n");
    } else {
      print($line);
    }
  }
}


