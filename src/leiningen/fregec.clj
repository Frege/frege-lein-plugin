;; copyright (c) 2014 Sean Corfield
;; work-in-progress
;; currently hard-codes source path and compile path and filename for
;; testing

(ns leiningen.fregec
  (:require [clojure.java.io :as io]
            [leiningen.core.classpath :as cp]
            [leiningen.core.eval :as eval]
            [leiningen.core.project :as project])
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

(def ^:private frege-flags
  "Very version dependent! We should extract these from
  frege.compiler.enums.Flags$TFlag I think!"
  {:warnings 2
   :withcp   3
   :runjavac 4})

(defn- flags-to-bits
  "Given a sequence of Frege compiler flags, make a long (bit) value."
  [flags]
  (reduce + (map (fn [flag] (apply * (repeat (get frege-flags flag 0) 2)))
                 flags)))

(defn- print-opts
  "Run the Frege code to print the compiler options."
  [opts]
  (->> opts
       frege.compiler.Main/printopts
       frege.prelude.PreludeBase$TST/performUnsafe
       .call))

(defn- subprocess-form
  "Build a form that can run the Frege compiler in a sub-process."
  [project srcs out cp flags files]
  `(binding [*out* *err*]
     (let [opts#     (frege.compiler.Main/createopts
                      (into-array String ~srcs)
                      ~flags ~out
                      (into-array String ~cp) "")
           compiler# (frege.compiler.Main/runfregec
                      (into-array String ~files)
                      opts# (java.io.PrintWriter. *err*))
           function# (frege.prelude.PreludeBase$TST/performUnsafe compiler#)]
       (when-not (.call function#)
         (println "Frege compilation failed!")
         (System/exit 1)))))

(def ^:private subprocess-profile
  {:dependencies [^:displace ['org.clojure/clojure (clojure-version)]
                  ['com.theoryinpractise.frege/frege "3.21.586-g026e8d7"]]
   :eval-in :subprocess})

(defn fregec
  "Compile Frege source files in :frege-source-path to :compile-path

Set :fregec-options in project.clj to pass options to the Frege compiler."
  [project]
  (binding [*out* *err*]
    (let [project (merge {:frege-source-paths [(or (:frege-source-path project)
                                                   ".")]
                          :compile-path "build"}
                         project)
          srcs  (:frege-source-paths project)
          out   (:compile-path project)
          files (stale-frege-sources srcs out)
          flags (flags-to-bits [:warnings :withcp :runjavac])
          cp    (cp/get-classpath project)
          form  (subprocess-form project
                                 (into [] srcs) out
                                 (into [] cp) flags
                                 (into [] files))]
      ;; (print-opts options) ;; debugging
      (binding [eval/*pump-in* false]
        (eval/eval-in (project/merge-profiles project [subprocess-profile])
                      form)))))
