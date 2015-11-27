(defproject example "0.1.0-SNAPSHOT"
  :description "Example Clojure / Frege mixed language application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 ;; need to depend on Frege for runtime:
                 [org.frege-lang/frege "3.23.422-ga05a487"]]
  :plugins [[lein-fregec "3.23.422"]]
  :frege-source-paths ["src/frege"]
  :main example.core
  :profiles {:uberjar {:aot :all
                       :prep-tasks ["fregec" "compile"]}})
