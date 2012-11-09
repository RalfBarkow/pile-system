Java implementation of a pile system
====================================
Introduction
------------
A _Pile_, or also called _Pile System_ by their inventors, is a (not so new, after all) paradigm of structuring and processing data. Whereas traditionally a metaphor of nested hierarchical containers is used to represent and process data - for instance words containing bytes containing bits, or tables containing rows containing fields -, pile systems take a radically different, a radically relational approach. In a pile system, data in its basic form is represented as a recursive, tree-like structure of relations pointing to other relations - a "pile or a heap of relations". A relation is hereby defined recursively as a directed edge pointing from one node to another, where both source and target nodes are relations themselves. Relations are not and do not serve as "data containers". This means, they do not logically hold or represent any atomic data item (bits or bytes or whatever) per se. Thus, as long as we neglect the necessity of physical implementation, relations do not have an ontological identity themselves. However, at the same time this pile of relations in its entirety makes up an algorithm on how to (re-)build the original data it represents. Similar to the Lisp or Clojure programming languages, pile systems are thus [homoiconic](http://en.wikipedia.org/wiki/Homoiconicity).

Representing data in such a way results in tree-like structures that are already known in computer science as _binary decision diagrams (BDD)_. However, pile system as an engineering approach or a metaphor literally turns things (or trees) upside down. Unlike binary decision diagrams, pile systems are defined, designed and built from their leaves, not from their root. They are also not so much concerned with "taking decisions" or representing binary functions, but they aim at enabling a different way of data representation and processing. Taking such a perspective has consequences that extend far beyond simple attempts to restructure data and are out of the scope of this introduction.

Leaves are the only relations connecting a pile with the outside world. A leave is usually called _Top_ in a pile system and it denotes a relation pointing to a logically atomic data item in the outside world of data containers such as a bit, a character, a row in a relational database, a document or an URI. In the lingo of "pile-ists" such a data item is usually called a _Top Value_ for obvious reasons. Connecting any two tops together results in a relation which can then be related to by even further relations. As a consequence, a pile tree is really a "hanging tree" or an "uprooted tree", growing from its leaves, placed at the top of the tree, downwards. For this reason and because every relation is always defined by its source and its target relation which are located further up in the tree, the pile-ists also use the terms "child" and "parent" in a reversed meaning compared to traditional computer scientists. A relation R connecting relations X (source) and Y (target) - written as R=(X,Y) - is said to have X and Y as its parents and to be a child of X and of Y. Furthermore, R's source parent X is called _Normative Parent_ whereas R's target parent Y is called _Associative Parent_. From the parents' perspective, R is called a _Normative Child_ of X and an _Associative Child_ of Y.

Ralf Westphal has written an excellent introduction to pile systems titled [_A Journey Into The Pile Universe_](http://www.lawsofform.de/wp-content/uploads/2006/05/RalfWestphalBlog.pdf). Unfortunately, another good article written by Ralf Westphal called _Freeing Data From the Silos - A Relationistic Approach to Information Processing_ seems to be no longer available online.

Implementation remarks
----------------------
This Java implementation very closely follows Ralph Westphal's hybrid C++/C# reference implementation. All the original naming was kept the same, and only where Java did not offer the same language concepts as C++ or C# small deviations from the original implementation were introduced. The goal was to build a simple, running prototype written purely in Java for demonstration purposes, not thinking of efficiency or completeness. Nevertheless the pile engine in its current state should actually be pretty computationally efficient (in terms of Java efficiency). When it comes to storing and retrieving texts there might be better algorithms than the ones implemented in the pile agent, especially so for dedicated text processing purposes. For instance, if processing natural, written language whole words instead of only characters could be stored as top values. Or for protein and genomic analysis, different storing, retrieving and search algorithms might turn out to be more efficient.

One should be aware that the pile agent is not one hundred percent free of bugs. For instance, in a text string "peter petra petro" three instances of the search string "etr" are found instead of just two.

Project structure
-----------------
Directory structure:
* _/src_: Contains the source code (.java files)
* _/bin_: Contains the byte code (.class files)
* _/test_: Contains JUnit 4 test cases
* _/data_: Contains some simple input data files

Package structure in the source directory:
* _pile_: the main class
* _pile.engine_: the pile core implementation
* _pile.text_: a pile agent for a text search engine
* _pile.ui_: classes for the (Swing) graphical user interface
* _pile.util_: Some helpful classes

Getting started
---------------
The program runs on Java v1.6. No third party libraries are required. Simply start the program by calling its main method in PileMain.class:

`$> cd <install-dir>/bin`

`$> java pile.PileMain`

Some sample input texts can be found in the /data directory.

Requirements
------------
* Java v1.6 or newer
* Input files must contain US-ASCII characters only

Further information
-------------------
Information and resources on the web:
* There is a [sourceforge repository](http://sourceforge.net/projects/pileworks/) containing two different reference implementations of a pile system. One was written by Erez Elul and Miriam Bedoni and is C++ only. It seems to be highly optimized and efficient, but is hard to understand due to its dense coding style, advanced data structure concepts and lack of illustrative documentation. The other one was developed by Ralf Westphal as a hybrid C#/C++ implementation. Besides the pile core (basically consisting of only a pile engine and a pile space) it also contains built on top of the pile core a simple pile agent serving as a text search engine. (This search engine seems to work in most cases, but still fails for some.)
* This [google project site on pile systems](http://code.google.com/p/pile/) seems to hold another example implementation. Some of the files are .exe and .dll files, and unfortunately for some of the already compiled files no source code is provided.
* [Ralf Westphal's (old) blog](http://weblogs.asp.net/ralfw/archive/tags/Pile/default.aspx) contains multiple helpful and introductory articles. Unfortunately, all the illustrating pictures seem to be gone. His new blog can be found on http://blog.ralfw.de/.
* [Ralf Barkow's blog](http://ralfbarkow.wordpress.com/) covers some more theoretical ground on the foundations of pile systems.
* [Andreas Mertens in his blog](http://www.lawsofform.de/pile/) offers (amongst other material) JNI-wrapper classes for the C++ reference implementation by Erez Elul and Miriam Bedoni.
* As Ralf Barkow mentions in [this blog post](http://ralfbarkow.wordpress.com/2006/06/21/the-cauchycantor-diagonal-method/) the [Cauchy/Cantor method described by W. T. Hardgrave 1976](http://dl.acm.org/citation.cfm?doid=942574.807126) or some method similar to it is used internally in both reference implementations to store and retrieve relations.
* A more philosophical foundation and perspective can be found in Peter Krieg's book "Die Paranoide Maschine: Computer zwischen Wahn und Sinn", Publisher Heise Heinz, 2005. ISBN-10: 3936931186, ISBN-13: 978-3936931181. 
* On an even deeper level, pile systems are related to Gotthard Günther's fascinating an pioneering work on Kenogrammatik, Morphogrammatik and proemial relations.

License
-------
This software is distributed under the GNU GPL v2 license.