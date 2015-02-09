# lein-fregec Frege example

Example of standalone Frege project.

## Installation

Download from https://github.com/seancorfield/lein-fregec.

## Usage

Run the Frege compiler to verify the code compiles:

    lein fregec

Create a JAR file containing the application and the Frege runtime:

    lein uberjar

Run the application:

    java -cp target/hello-0.1.0-SNAPSHOT-standalone.jar Hello

## License

Copyright (c) 2014-2015 Sean Corfield

Distributed under the Eclipse Public License, the same as Clojure.

