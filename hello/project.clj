(defproject frege-hello "0.1.0-SNAPSHOT"
  :description "Example standalone Frege application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [;; need to depend on Frege for uberjar:
                 [com.theoryinpractise.frege/frege "3.22.367-g2737683"]]
  :plugins [[lein-fregec "3.22.367"]]
  :profiles {:uberjar {:prep-tasks ["fregec"]}})
