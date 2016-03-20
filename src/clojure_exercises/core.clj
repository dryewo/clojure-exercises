(ns clojure-exercises.core
  (:require [clojure-exercises.answers.einstein-riddle :as einstein])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& [subcommand]]
  (case subcommand
    "einstein" (einstein/benchmark)
    (println "Hello, World!")))
