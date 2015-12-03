lein-fregec
===========

A Leiningen plugin to compile Frege (http://www.frege-lang.org) code.

Usage - Simple
-----

The easiest way to use Leiningen with Frege (or with Clojure and Frege) is to take advantage of the `frege` template --
[frege-lein-template](https://github.com/Frege/frege-lein-template) -- to create a new Leiningen project for either a pure Frege project or a mixed Clojure / Frege project (with a Clojure main entry point):

    lein new frege myapp

This will create a folder called `myapp` containing a Leiningen project with an example of a pure Frege program in it. If you want a mixed Clojure / Frege project:

    lein new frege myapp -- :with-clojure

The Frege template will always set up a project with the most recent stable version of Frege. If you want to modify the `project.clj` file, read the section below on what settings are available.

Usage - Manual
-----

If you already have a Leiningen project, you can add this plugin as follows:

Add `[lein-fregec "3.23.442"]` to `:plugins` in your `project.clj`. The version of `lein-fregec` matches the version of the Frege compiler it is compatible with and uses.

Usage - Configuration & Execution
-----

Set `:frege-source-paths` to the location(s) of your Frege source files. Default is the current directory but I highly recommend using `src/frege` and structuring your projects that way (although the `hello` example relies on current directory). The template generates projects that either use `src` (pure Frege) or `src/frege` (mixed Clojure / Frege).

Run `lein fregec` to compile Frege source files to `.class` files.

The output of compilation will go to the `:compile-path` directory, which defaults to `target/classes/` in Leiningen.

You may specify additional Frege compiler options via `:fregec-options` in `project.clj` (as a vector of strings) or via the command line. Note that command line options for Leiningen tasks start with `:` (and are converted to `-` options automatically):

```sh
lein fregec :v
```

This will pass `-v` to the Frege compiler (verbose mode). If you want to see the full list of options being passed to the Frege compiler, set the `DEBUG` environment variable to `true`:

```sh
DEBUG=true lein fregec
```

This will also display the exact version of the Frege compiler that the plugin is using.

Run `lein uberjar` to compile Frege source files and create a JAR file in the `target/` folder. You need to ensure that Frege compilation is part of the `:uberjar` profile in `project.clj`:

```clojure
:profiles {:uberjar {:aot :all
                     :prep-tasks ["fregec" "compile"]}}
```

This tells Leiningen to run the `fregec` task and the `compile` task before building the JAR file. That will run the Frege compiler and also compile any Clojure code in the project.

Also, in order to include the Frege runtime in the resulting JAR file, you will need the following dependency in your `project.clj` file:

```clojure
:dependencies [[org.frege-lang/frege "3.23.442-SNAPSHOT]]
```

The version here should exactly match that displayed by `lein-fregec` when you use the `DEBUG=true` environment variable!

This tells Leiningen that your project depends on Frege, and it will package it into the standalone JAR it produces. The standalone JAR can be run as follows:

```sh
java -cp target/frege-hello-0.1.0-SNAPSHOT-standalone.jar Hello
```

This assumes your `project.clj` starts out like this:

```clojure
(defproject frege-hello "0.1.0-SNAPSHOT"
  ...)
```

You can also run your (pure Frege) code by specifying `:run` and the class name (followed by any arguments for your `main` method):

```sh
lein fregec :run Hello
```

Arguments to the Frege compiler should come before `:run` (and can use `:` or `-` to introduce them). Arguments to the program being run should come after the class name (and are passed exactly as-is).

If you have tests, you can run those using `:test`:

```sh
lein fregec :test HelloTest
```

This is equivalent to:

```sh
lein fregec :run frege.tools.Quick HelloTest
```

You can pass flags to QuickCheck like this:

```sh
lein fregec :test -v HelloTest
```

which is equivalent to:

```sh
lein fregec :run frege.tools.Quick -v HelloTest
```

There will also be a non-standalone JAR will which does not contain the Frege runtime.

An example of a pure Frege project can be found in the [hello directory](https://github.com/Frege/frege-lein-plugin/tree/master/hello) which is a self-contained Leiningen project with its own README.

An example of mixed Clojure / Frege usage can be found in the [example directory](https://github.com/Frege/frege-lein-plugin/tree/master/example) which is also a self-contained project.

License
-------

Copyright (c) 2014-2015 Sean Corfield

Distributed under the BSD License, the same as Frege.
