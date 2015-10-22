(defproject frege-hello "0.1.0-SNAPSHOT"
  :description "Example standalone Frege application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [;; need to depend on Frege for uberjar:
                 [org.frege-lang/frege "3.23.370-g898bc8c"]]
  :plugins [[lein-fregec "3.23.370"]]
  :profiles {:uberjar {:prep-tasks ["fregec"]}})
