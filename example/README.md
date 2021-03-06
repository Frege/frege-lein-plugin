# lein-fregec Clojure / Frege example

Example of Clojure / Frege mixed language project. Clojure main program calls Frege function.

## Installation

Download from https://github.com/Frege/frege-lein-plugin.

## Usage

Run the Frege compiler and then run the Clojure application:

    lein do fregec, run

Run the Clojure tests (which exercise the Frege functions):

    lein do fregec, test

You can also package up all the Clojure and Frege code and their runtimes:

    lein uberjar

That produces a JAR file which you can run:

    java -jar target/example-0.1.0-SNAPSHOT-standalone.jar

## License

Copyright (c) 2014-2015 Sean Corfield

Distributed under the BSD 3-clause License, the same as Frege itself.

