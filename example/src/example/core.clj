(ns example.core
  (:import Fibonacci)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (doseq [n (range 10)]
    (println "Fibonacci number" n "is" (Fibonacci/fib n))))
