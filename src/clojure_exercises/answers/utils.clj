(ns clojure-exercises.answers.utils)

(defmacro spy [x]
  `(let [x# ~x]
     (println ~(str x) "=>" x#)
     x#))
