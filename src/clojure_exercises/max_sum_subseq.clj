(ns clojure-exercises.max-sum-subseq
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Maximum_subarray_problem

(defn max-sum-subseq [coll]
  )

(future-facts "about max-sum-subseq"
              (max-sum-subseq nil) => []
              (max-sum-subseq []) => []
              (max-sum-subseq [1]) => [1]
              (max-sum-subseq [1 2]) => [1 2]
              (max-sum-subseq [-1]) => []
              (max-sum-subseq [-1 1]) => [1]
              (max-sum-subseq [1 -1]) => [1]
              (max-sum-subseq [0]) => []
              (max-sum-subseq [10 -20 11]) => [11]
              (max-sum-subseq [10 -20 5]) => [10]
              (max-sum-subseq [1 0 2]) => [1 0 2]
              (max-sum-subseq [1 1 1 -4 1 3 -1]) => [1 3]
              (max-sum-subseq [1 1 1 -4 1 2 -1]) => [1 1 1]
              (max-sum-subseq [-1 -1 -1 -1]) => []
              (max-sum-subseq [-1 -1 2 -1 -1]) => [2])
