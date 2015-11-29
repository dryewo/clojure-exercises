(ns clojure-exercises.answers.integer-partitions
  (:require [midje.sweet :refer :all]))

(def partitions*
  (memoize
    (fn [n maxvalue]
      [n maxvalue]
      (if (<= n 0)
        [[]]
        (for [x (range (min n maxvalue) 0 -1)
              p (partitions* (- n x) x)]
          (cons x p))))))

(defn partitions [n]
  (partitions* n n))

(facts "about implementation"
  (partitions* 0 1) => [[]]
  (partitions* 1 1) => [[1]]
  (partitions* 1 2) => [[1]]
  (partitions* 2 1) => [[1 1]]
  (partitions* 3 1) => [[1 1 1]]
  (partitions* 4 1) => [[1 1 1 1]]
  (partitions* 2 2) => [[2] [1 1]]
  (partitions* 3 2) => [[2 1] [1 1 1]]
  (partitions* 4 2) => [[2 2] [2 1 1] [1 1 1 1]])

(facts "about partitions"
  (partitions 0) => [[]]
  (partitions 1) => [[1]]
  (partitions 2) => [[2] [1 1]]
  (partitions 3) => [[3] [2 1] [1 1 1]]
  (partitions 4) => [[4] [3 1] [2 2] [2 1 1] [1 1 1 1]])