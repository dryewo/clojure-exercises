(defproject clojure-exercises "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.combinatorics "0.1.1"]
                 [prismatic/schema "1.0.5"]
                 [midje "1.8.3"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [instaparse "1.4.1"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [automat "0.1.3"]
                 [potemkin "0.4.3"]]
  :main ^:skip-aot clojure-exercises.core
  :target-path "target/%s"
  :uberjar-name "clojure-exercises.jar"
  :profiles {:uberjar {:aot :all}
             :dev     {:source-paths ["dev"]
                       :repl-options {:init-ns user}
                       :plugins      [[lein-midje "3.2"]]
                       :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                      [org.clojure/java.classpath "0.2.3"]
                                      [criterium "0.4.4"]]}})
