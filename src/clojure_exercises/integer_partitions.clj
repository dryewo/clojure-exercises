(ns clojure-exercises.integer-partitions
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Partition_(number_theory)

(defn partitions [n]
  )

(future-facts "about partitions"
              (partitions 0) => [[]]
              (partitions 1) => [[1]]
              (partitions 2) => [[2] [1 1]]
              (partitions 3) => [[3] [2 1] [1 1 1]]
              (partitions 4) => [[4] [3 1] [2 2] [2 1 1] [1 1 1 1]])
