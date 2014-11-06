lein-fregec
===========

A Leiningen plugin to compile Frege code.

Usage
-----

Add `[lein-fregec "0.1.0-SNAPSHOT"]` to `:plugins` in `project.clj`. For now you'll need to check out this repo and run `lein install` yourself.

Set `:frege-source-path` to the location of your Frege source files. Default is the current directory right now.

Run `lein fregec` to compile them to `.class` files.

**NOTE: It currently recompiles all the source files every time - BUG! And outputs a warning about source version support for annotations.**

Frege compiler options are hard-coded to warnings, withcp, runjavac at present. Better support will come soon (maybe).

Examples of mixed Clojure / Frege usage will come soon once I get that figured out.

License
-------

Copyright (c) 2014 Sean Corfield

Distributed under the Eclipse Public License, the same as Clojure.
