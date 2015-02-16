(ns example.core-test
  (:require [clojure.test :refer :all]
            [example.core :refer :all])
  (:import Fibonacci))

(deftest test-fib
  (is (= 0 (Fibonacci/fib 0)))
  (is (= 1 (Fibonacci/fib 1)))
  (is (= 1 (Fibonacci/fib 2)))
  (is (= 55 (Fibonacci/fib 10))))
