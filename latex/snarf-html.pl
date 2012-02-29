#!/usr/bin/perl

require 5.001;
use strict;
use warnings;

sub wanted($@) {
  my $sig = shift(@_); 
  while (my $param = shift @_) {
    if ("$sig" eq $param) {
      return 1;
    }
  }
  return 0;
}

sub color($) {
  my $line = shift(@_);
  $line =~ s/([{}])/\\$1/g;       # escape { and } until printVerbatim()
  (my $comment) = $line =~ /(\/\/.*)$/; # save comment
  $line =~ s/(\/\/.*)$//; # strip comment
  my $p = "CFXMDFJERKDFHBDFSUMK"; # treat method names like class names
  $line =~ s/\b([a-z]\w*)(\s*\()/$p$1$2/g;
  $line =~ s/\b([a-z]\w*)\b/{\\color{var}$1}/g; # color variables
  $line =~ s/$p//g;
  my @keywords = ("null", "int", "long", "double", "float", "char", "byte", "public", "protected", "private", "static", "if", "while", "else", "for", "do", "T", "K", "V", "extends", "implements", "throw", "new", "class");
 foreach my $k (@keywords) {
    $line =~ s/\{\\color\{\w+\}($k)\}/$1/g;
    $line =~ s/\b($k)\b/{\\color{keyword}$1}/g;
  }
  if ($comment) {  # put comment back
    $line = $line."{\\color{comment}$comment}";
  }
  return $line;
}

sub printVerbatim($) {
  my $line = shift(@_);
  $line =~ s/\t/    /g;
  #$line = color($line);
  #while ($line =~ s/(^|[^\\])\{/$1\@/) {} # change { to @
  #while ($line =~ s/(^|[^\\])\}/$1\$/) {} # change } to $
  #$line =~ s/\\([{}])/$1/g;               # unescape \{
  print("$line\n");
}

sub snarfit($$) {
  my $w = "A-Za-z0-9<>\\[\\]";   # things that occur in type names
  my $k = "(static|public|protected|private|final)";  # boring keywords
  my $args = shift(@_);
  my @params = split(/\./, $args);
  my $wc = shift(@_);
  my $class=$params[0];
  my $basedir="../java/";
  my $javafile="${basedir}${class}.java";
  my $d = 0;  # level of nesting
  my $print = 0;
  open(FP, "<",$javafile) || die("Unable to open $javafile");
  while (my $line = <FP>) {
    chomp($line);
    $line =~ s/ö/o/g;
    if ($wc && $d == 0 && $line =~ /^($k\s)*class\s/) {
        $line =~ s/($k\s+)//g;
        $line =~ s/extends\s+([$w]+\s*,\s*)*([$w]+)//g;
  	printVerbatim($line); 
    } elsif ($d == 1) {
      if ($line =~ /^\s*($k\s+)*(\<[^>]\>)?\s*[$w]+\s+([$w]+)\(.*\)/) {
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
      } elsif ($line =~ /^\s*($k\s+)*class\s+([$w]+)/) {
        # this is an internal class definition
        $line =~ /class\s+([$w]+)/;
        if (wanted("$1", @params)) {
          $print = 1;
        }    
      } elsif ($line =~ /^\s*($k\s+)*[$w]+\s+([$w]+)\s*;/) {
        # this is an instance variable declaration
        $line =~ /([$w]+)\s*;/;
        my $var = $1;
        if (wanted($var, @params)) {
          $print = 1;
        }
      }
    }
    if ($print) {
      $line =~ s/($k)\s+//g;    # strip keywords
      $line =~ s/\bUtils\.//g;    # get rid of Util.
      $line =~ s/\bc\.compare/compare/g;    # get rid of Util.
      $line =~ s/([^A-Za-z0-9])f\./$1/g; # hide factories
      printVerbatim($line);
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
    while ($line =~ /#([^#]*)#/) {
      my $inside = $1;
      if ($inside =~ /^[A-Z]\w+$/) {
        $inside = "\\texttt{$inside}";  # just a class name
      } else {
        #$inside = color($inside);
        $inside =~ s/([%&])/\\$1/g;
        $inside =~ s/(\\\&|\\\%|<<|>>>?)/\\text{\\ttfamily $1}/g;
        $inside = "\\ensuremath{\\mathtt{$inside}}";
      }
      $line =~ s/#([^#])*#/$inside/;
    } 
    if ($line =~ /\\cppimport\{([^}]+)\}/) {
      print("%$line");
    } elsif ($line =~ /\\(code|java)import\{([^}]+)\}/) {
      my $args=$2;
      (my $class) = $line =~ /\{\w+\/(\w+)\./;
      print("%$line");
#      print('\htmlrule');
      print('\begin{verbatim}'."\n");
      snarfit($args, 0); 
      print('\end{verbatim}'."\n");
#      print('\htmlrule');
    } else {
      print($line);
    }
  }
}


