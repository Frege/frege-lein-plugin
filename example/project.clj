(defproject example "0.1.0-SNAPSHOT"
  :description "Example Clojure / Frege mixed language application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha3"]
                 ;; need to depend on Frege for runtime:
                 [com.theoryinpractise.frege/frege "3.21.586-g026e8d7"]]
  :plugins [[lein-fregec "0.1.0-SNAPSHOT"]]
  :frege-source-paths ["src/frege"]
  :main example.core
  :profiles {:uberjar {:aot :all}})
