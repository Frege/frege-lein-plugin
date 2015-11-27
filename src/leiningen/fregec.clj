;; copyright (c) 2014-2015 Sean Corfield

(ns leiningen.fregec
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [leiningen.core.classpath :as cp]
            [leiningen.core.eval :as eval]
            [leiningen.core.project :as project])
  (:import java.io.File
           (java.nio.file Files Paths)))

(def ^:private fregec-version "3.23.422-ga05a487")

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

(defn- subprocess-compiler-form
  "Build a form that can run the Frege compiler in a sub-process."
  [fregec-args]
  `(binding [*out* *err*]
     (when-not (frege.compiler.Main/runCompiler (into-array String ~fregec-args))
       (println "Frege compilation failed!")
       (System/exit 1))))

(def ^:private subprocess-profile
  {:dependencies [^:displace ['org.clojure/clojure (clojure-version)]
                  ['org.frege-lang/frege fregec-version]]
   :eval-in :subprocess})

(defn- file-exists
  "Given a path, return true if it exists as a file."
  [path]
  (.exists (io/file path)))

(defn- additional-fregec-args
  "Given command line arguments, convert to Frege compiler arguments."
  [args]
  (map (fn [^String arg] (if (.startsWith arg ":")
                           (str "-" (subs arg 1))
                           arg)) args))

(def ^:private valid-ops
  "Valid command line arguments that affect plugin behavior."
  #{":run" ":test"})

(defn- run-class-args
  "Given command line arguments, extract keyword arg and ClassName.
  Returns a vector of the keyword arg (or nil), the ClassName (or nil), the args
  before the keyword arg, and the args after the ClassName."
  [args]
  (let [[before & [[kw arg & run-args]]] (split-with (fn [arg] (not (valid-ops arg))) args)]
    (if (and kw arg)
      [(keyword (subs kw 1)) arg before run-args]
      [nil nil args nil])))

(defn- run-main-form
  "Build a form that can run the main method of the given class."
  [class-name args]
  `(let [c# (Class/forName ~class-name)
         j-args# (into-array String ~args)
         m-arg-types# (into-array Class [(class j-args#)])
         main# (.getDeclaredMethod c# "main" m-arg-types#)]
     (if main#
       (.invoke main# nil (into-array Object [j-args#]))
       (println "Unable to find" (str ~class-name "/main") "method"))))

(defn fregec
  "Compile Frege source files in :frege-source-paths to :compile-path

Set :fregec-options in project.clj to pass options to the Frege compiler.
Additional options may be passed on the command line.

To display the Frege compiler's help, use: lein fregec :help"
  [project & args]
  (binding [*out* *err*]
    (let [[op class-name args run-args] (run-class-args args)
          project (merge {:frege-source-paths ["."]} project)
          srcs  (:frege-source-paths project)
          out   (:compile-path project)
          flags (:fregec-options project)
          files (stale-frege-sources srcs out)
          cp    (->> (cp/get-classpath project)
                     ;; only pass directories / files that exist:
                     (filter file-exists))
          path-sep (System/getProperty "path.separator")
          fregec-args (-> ["-d" out ; output directory
                           "-fp" (str/join path-sep cp) ; imported Frege package paths
                           "-sp" (str/join path-sep srcs) ; source paths
                           "-make"] ; let compiler do dependency stuff
                          (into (additional-fregec-args args))
                          (into (concat flags files)))]
      (leiningen.core.main/debug "Frege compiler version:" fregec-version)
      (apply leiningen.core.main/debug "Frege compiler arguments:" fregec-args)
      ;; ensure output directory exists:
      (Files/createDirectories (Paths/get out (into-array String []))
                               (into-array java.nio.file.attribute.FileAttribute []))
      (binding [eval/*pump-in* false]
        (eval/eval-in (project/merge-profiles project [subprocess-profile])
                      (subprocess-compiler-form fregec-args)))
      (when class-name
        (eval/eval-in (project/merge-profiles project [subprocess-profile])
                      (case op
                        :run (run-main-form class-name (vec run-args))
                        :test (run-main-form "frege.tools.Quick" (into [class-name] run-args))))))))
