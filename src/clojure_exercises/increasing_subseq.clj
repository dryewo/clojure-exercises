(ns clojure-exercises.increasing-subseq
  (:require [midje.sweet :refer :all]))

(defn longest-increasing-subseq [coll]
  [])

(future-facts "about longest-increasing-subseq"
              (longest-increasing-subseq []) => []
              (longest-increasing-subseq [1 2 3 4 5 1 2 3 4 6]) => [1 2 3 4 5]
              (longest-increasing-subseq [1 2 3 4 5 4 3 2 3 4 5 4 3]) => [1 2 3 4 5]
              (longest-increasing-subseq [9 8 7 6 5 4 3 2 1]) => [9])
