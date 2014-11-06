;; copyright (c) 2014 Sean Corfield
;; work-in-progress
;; currently hard-codes source path and compile path and filename for
;; testing

(ns leiningen.fregec
  (:require [clojure.java.io :as io]
            [leiningen.classpath :as cp])
  (:import java.io.File))

(defn- stale-frege-sources
  "Returns a lazy seq of file paths: every Frege source file within dirs modified
  since it was most recently compiled into compile-path."
  [dirs compile-path]
  (for [dir dirs
        ^File source (filter #(-> ^File % (.getName) (.endsWith ".fr"))
                             (file-seq (io/file dir)))
        :let [rel-source (.substring (.getPath source) (inc (count dir)))
              rel-compiled (.replaceFirst rel-source "\\.fr$" ".class")
              compiled (io/file compile-path rel-compiled)]
        :when (>= (.lastModified source) (.lastModified compiled))]
    (.getPath source)))

(defn- create-opts
  "Create options for the Frege compiler."
  [project]
  (let [source-path (:frege-source-path project)
        compile-path (:compile-path project)
        options 0 ; will fix this shortly - 0 = just compile to Java
        prefix "" ; Frege package prefix - should always be empty string
        classpath (cp/get-classpath-string project)]
    (frege.compiler.Main/createopts (into-array String [source-path])
                                    options
                                    compile-path
                                    (into-array String []) ; classpath
                                    prefix)))

(defn- print-opts
  "Run the Frege code to print the compiler options."
  [opts]
  (->> opts
       frege.compiler.Main/printopts
       frege.prelude.PreludeBase$TST/performUnsafe
       .call))

(defn- compiler
  "Create compiler object for project and options."
  [project opts]
  (let [files (stale-frege-sources [(:frege-source-path project)]
                                   (:compile-path project))]
    (frege.compiler.Main/runfregec (into-array String files)
                                   opts
                                   (java.io.PrintWriter. *out*))))

(defn fregec
  "Compile Frege source files in :frege-source-path to :compile-path

Set :fregec-options in project.clj to pass options to the Frege compiler."
  [project]
  (let [project (merge {:frege-source-path "."
                        :compile-path "build"}
                       project)
        options (create-opts project)
        compiler (compiler project options)
        function (frege.prelude.PreludeBase$TST/performUnsafe compiler)]
    (print-opts options)
    (.call function)))
