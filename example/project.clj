(defproject example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha3"]
                 [com.theoryinpractise.frege/frege "3.21.586-g026e8d7"]]
  :frege-source-path "src/frege"
  :plugins [[lein-fregec "0.1.0-SNAPSHOT"]]
  :main example.core
  :profiles {:uberjar {:aot :all}})
