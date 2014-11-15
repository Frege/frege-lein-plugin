lein-fregec
===========

A Leiningen plugin to compile Frege code.

Usage
-----

Add `[lein-fregec "0.1.0-SNAPSHOT"]` to `:plugins` in `project.clj`.

Set `:frege-source-paths` to the location(s) of your Frege source files. Default is the current directory right now.

Run `lein fregec` to compile them to `.class` files.

Frege compiler options are hard-coded to warnings, withcp, runjavac at present. Better support will come soon (maybe).

An example of mixed Clojure / Frege usage will can be found in the [example directory](https://github.com/seancorfield/lein-fregec/tree/master/example) which is a self-contained Leiningen project!

License
-------

Copyright (c) 2014 Sean Corfield

Distributed under the Eclipse Public License, the same as Clojure.
