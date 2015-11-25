# lein-fregec Frege example

Example of standalone Frege project.

## Installation

Download from https://github.com/Frege/frege-lein-plugin.

## Usage

Run the Frege compiler to verify the code compiles:

    lein fregec

Create a JAR file containing the application and the Frege runtime:

    lein uberjar

Run the application:

    java -cp target/frege-hello-0.1.0-SNAPSHOT-standalone.jar Hello

Note that you can also run the application without creating a JAR file after `lein fregec` if you specify the classpath that includes the Frege runtime:

    java -cp ~/.m2/repository/org/frege-lang/frege/3.23.401-g7c45277/frege-3.23.401-g7c45277.jar:target/classes Hello

A simpler, but slower way to achieve that is to rely on Leiningen to retrieve the necessary classpath:

    java -cp `lein classpath` Hello

Or you can simply run it using `lein-fregec` itself immediately after the compile:

    lein fregec :run Hello

## License

Copyright (c) 2014-2015 Sean Corfield

Distributed under the Eclipse Public License, the same as Clojure.

