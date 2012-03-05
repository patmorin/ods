#!/usr/bin/perl

require 5.001;
use strict;
use warnings;

my $output = 0;

sub methodSig($$) {
   my $name=shift(@_);
   my $parms=shift(@_);
   my @parms = split(/,/,$parms);
   @parms = map { /(\w+)$/ ; $1 } @parms;
   $" = ",";
   return "$name(@parms)";
}

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
  #$line = color($line);
  #while ($line =~ s/(^|[^\\])\{/$1\@/) {} # change { to @
  #while ($line =~ s/(^|[^\\])\}/$1\$/) {} # change } to $
  #$line =~ s/\\([{}])/$1/g;               # unescape \{
  $line =~ s/\t/  /g;
  print("$line\n");
  $output = 1;
}

sub snarfit($$) {
  my $w = "A-Za-z0-9<>\\[\\]<>";   # things that occur in type names
  my $k = "(typename|static|public|protected|private|const|final|template\\s*<\\s*class\\s+\\w+\\s+>)";  # boring keywords
  my $args = shift(@_);
  my @params = split(/\./, $args);
  my $wc = shift(@_);
  my $class=$params[0];
  $class=~s/ods\///;
  my $basedir="../cpp";
  my $cppfile="${basedir}/${class}.h";
  my $d = 0;  # level of nesting
  my $print = 10000;
  my $indent = "";
  my $inclass = 0;
  if (!open(FP, "<",$cppfile)) {
     print("Unable to open $cppfile");
     print(STDERR "Unable to open $cppfile");
     return;
  }
  while (my $line = <FP>) {
    chomp($line);
    my $d0 = 1;
    my $k = '(typename|const|inline|public:|private:|protected:|static|virtual)\s';
    $line =~ s/$k//g;
    my $template = 'template\\s*<[^\]]+>';
    $line =~ s/$template//g;
    my $type = '\w+\s*(<[^>]+>)?[*&]*';
    if ($d == $d0 && $line =~ /^\s*class\s+(\w+)/) {
      # class definition
      $class = $1;
      $inclass = 1;
      if (wanted($class, @params)) { $print = $d; $indent = "\t"; }
    } elsif ($d == $d0 && $line =~ /^\s*$type\s+(\w+)(<[^\]]+>?)::(\w+)\s*\(([^)]*)\)/) {
      # external method definition
      $class = $2;
      my $sig = methodSig($4, $5);
      $line =~ s/${class}(<[^>]+>)?:://;
      if (wanted($sig, @params)) { $print = $d; $indent = "\t"; }
    } elsif ($d == $d0 && $line =~ /^\s*(\w+)(<[^\]]+>?)::(\w+)\s*\(([^)]*)\)/) {
      # constructor
      $class = $1;
      my $sig = methodSig($3, $4);
      print(STDERR "CONSTRUCTOR $sig @params\n");
      $line =~ s/${class}(<[^>]+>)?:://g;
      if (wanted($sig, @params)) { print(STDERR "WANTED\n"); $print = $d; $indent = "\t"; }
    } elsif ($d == $d0+1 && $inclass == 1 && $line =~ /^\s*$type\s+(\w+)\s*\(([^)]*)\)[^;]*$/) { 
      # internal method definition
      my $sig = methodSig($2, $3);
      if (wanted($sig, @params)) { $print = $d; }
    } elsif ($d == $d0+1 && $inclass == 1 && $line =~ /^\s*$type\s+(operator.*)\s*\(([^)]*)\)[^;]*$/) { 
      # internal method definition
      print(STDERR "OPERATOR $2\n");
      my $sig = $2;
      if (wanted($sig, @params)) { $print = $d; }
    } elsif ($d == $d0 && $line =~ /^\s*$type\s+(\w+)\s*\(([^)]*)\)[^;]*$/) {
      # external function definition
      my $sig = methodSig($2, $3);
      # print(STDERR "$line - $sig");
      if (wanted($sig, @params)) { $print = $d; }
    } elsif ($d == $d0+1 && $inclass == 1 && $line =~ /^\s*$type\s+(\&|\*)*(\w+).*;.*/) {
      # instance or class variable declaration
      if (wanted($3, @params)) {
        printVerbatim($line);
      }
    } elsif ($d == $d0+1 && $line =~ /^\s*(class|struct)\s*(\w+)/) {
      # internal class definition
      if (wanted($2, @params)) {
        $print = $d;
      }
    }
    while ($line =~ /\{/g) { $d++; }
    if ($d > $print) { 
      if ($line =~ /^\w+:\s*$/) { }
      elsif ($line =~ /^\s*using\s/) { }
      elsif ($line =~ /^\s*$/) { }
      else { printVerbatim("$indent$line"); }
    } else {
      $indent = "";
      $print = 10000;
    }
    while ($line =~ /\}/g) { $d--; }
    if ($d == $d0) { $inclass = "0"; $class = ""; }
  }
  close(FP);
} 

MAIN: {
  while (my $line = <STDIN>) {
    while ($line =~ /#([^#]*)#/) {
      my $inside = $1;
      $inside =~ s/([%&])/\\$1/g;
      #$inside = color($inside);
      $inside = "\\ensuremath{\\mathtt{$inside}}";
      $line =~ s/#([^#])*#/$inside/;
    } 
    if ($line =~ /\\javaimport\{([^}#]+)\}/) {
      # do nothing 
    } elsif ($line =~ /\\(code|cpp)import\{([^}#]+)\}/) {
      print("%$line");
      my $args=$2;
      (my $class) = $line =~ /ods\/(\w+)\./;
      print('\begin{verbatim}'."\n");
      $output = 0;
      snarfit($args, 0);
      if ($output == 0) {
        print("NO OUTPUT PRINTED ( args=$args )\n");
        print(STDERR "NO OUTPUT PRINTED ( args=$args )\n");
      }
      print('\end{verbatim}'."\n");
    } else {
      print($line);
    }
  }
}


