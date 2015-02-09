lein-fregec
===========

A Leiningen plugin to compile Frege (http://www.frege-lang.org) code.

Usage
-----

Add `[lein-fregec "3.22.324"]` to `:plugins` in `project.clj`. The version of `lein-fregec` matches the version of the Frege compiler it is compatible with and uses.

Set `:frege-source-paths` to the location(s) of your Frege source files. Default is the current directory but I highly recommend using `src/frege` and structuring your projects that way.

Run `lein fregec` to compile Frege source files to `.class` files.

The output of compilation will go to the `:compile-path` directory, which defaults to `target/classes/` in Leiningen.

You may specify additional Frege compiler options via `:fregec-options` in `project.clj` (as a vector of strings) or via the command line. Note that command line options for Leiningen tasks start with `:` (and are converted to `-` options automatically):

    lein fregec :v

This will pass `-v` to the Frege compiler (verbose mode). If you want to see the full list of options being passed to the Frege compiler, set the `DEBUG` environment variable to `true`:

    DEBUG=true lein fregec

This will also display the exact version of the Frege compiler that the plugin is using.

Run `lein uberjar` to compile Frege source files and create a JAR file in the `target/` folder. You need to ensure that Frege compilation is part of the `:uberjar` profile in `project.clj`:

    :profiles {:uberjar {:aot :all
                         :prep-tasks ["fregec" "compile"]}}

This tells Leiningen to run the `fregec` task and the `compile` task before building the JAR file. That will run the Frege compiler and also compile any Clojure code in the project.

Also, in order to include the Frege runtime in the resulting JAR file, you will need the following dependency in your `project.clj` file:

    :dependencies [[com.theoryinpractise.frege/frege "3.22.324-g630677b"]]

The version here should exactly match that displayed by `lein-fregec` when you use the `DEBUG=true` environment variable!

This tells Leiningen that your project depends on Frege, and it will package it into the standalone JAR it produces. The standalone JAR can be run as follows:

    java -cp target/frege-hello-0.1.0-SNAPSHOT-standalone.jar Hello

This assumes your `project.clj` starts out like this:

    (defproject frege-hello "0.1.0-SNAPSHOT"
      ...)

There will also be a non-standalone JAR will which does not contain the Frege runtime.

An example of mixed Clojure / Frege usage can be found in the [example directory](https://github.com/seancorfield/lein-fregec/tree/master/example) which is a self-contained Leiningen project!

License
-------

Copyright (c) 2014-2015 Sean Corfield

Distributed under the Eclipse Public License, the same as Clojure.
