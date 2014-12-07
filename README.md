lein-fregec
===========

A Leiningen plugin to compile Frege code.

Usage
-----

Add `[lein-fregec "0.1.0-SNAPSHOT"]` to `:plugins` in `project.clj`.

Set `:frege-source-paths` to the location(s) of your Frege source files. Default is the current directory right now.

Run `lein fregec` to compile Frege source files to `.class` files.

Run `lein do fregec, uberjar` to compile Frege source files and create a JAR file in the `target/` folder. In order to include the Frege runtime in the resulting JAR file, you will need the following dependency in your `project.clj` file:

    :dependencies [[com.theoryinpractise.frege/frege "3.21.586-g026e8d7"]]

This tells Leiningen that your project depends on Frege, and it will package it into the standalone JAR it produces. The standalone JAR can be run as follows:

    java -cp target/frege-hello-0.1.0-SNAPSHOT-standalone.jar Hello

This assumes your `project.clj` starts out like this:

    (defproject frege-hello "0.1.0-SNAPSHOT"
      ...)

There will also be a non-standalone JAR will which does not contain the Frege runtime.

Frege compiler options are hard-coded to warnings, withcp, runjavac at present. Better support will come soon (maybe). If your `project.clj` has `:debug` defined as a truthy value, the Frege compiler options will be printed prior to compilation.

An example of mixed Clojure / Frege usage will can be found in the [example directory](https://github.com/seancorfield/lein-fregec/tree/master/example) which is a self-contained Leiningen project!

License
-------

Copyright (c) 2014 Sean Corfield

Distributed under the Eclipse Public License, the same as Clojure.
