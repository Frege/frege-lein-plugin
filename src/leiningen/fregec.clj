;; copyright (c) 2014 Sean Corfield
;; work-in-progress
;; currently hard-codes source path and compile path and filename for
;; testing

(ns leiningen.fregec
  (:require [leiningen.classpath :as cp]))

(defn fregec
  "Compile Frege source files in :frege-source-path to :compile-path

Set :fregec-options in project.clj to pass options to the Frege compiler."
  [project]
  (let [classpath (cp/get-classpath-string project)
        options (frege.compiler.Main/createopts (into-array String ["."]) 0 "cljbuild" (into-array String []) "")
        compiler (frege.compiler.Main/runfregec (into-array String ["Hi.fr"])
                                                options
                                                (java.io.PrintWriter. *out*))
        function (frege.prelude.PreludeBase$TST/performUnsafe compiler)]
    (.call function)))
