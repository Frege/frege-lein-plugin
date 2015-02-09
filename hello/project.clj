(defproject hello "0.1.0-SNAPSHOT"
  :description "Example standalone Frege application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [;; need to depend on Frege for uberjar:
                 [com.theoryinpractise.frege/frege "3.22.324-g630677b"]]
  :plugins [[lein-fregec "3.22.324"]]
  :profiles {:uberjar {:prep-tasks ["fregec"]}})
