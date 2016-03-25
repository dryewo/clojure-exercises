(ns clojure-exercises.sorted-merge
  (:require [midje.sweet :refer :all]))

;; Implement a function that inserts an element into a sorted sequence

(defn insert-in-order
  ([coll x]
   (insert-in-order compare coll x))
  ([cmp coll x]
    ))

(future-facts "about insert-in-order"
              (fact "works"
                (insert-in-order nil 1) => [1]
                (insert-in-order [] 1) => [1]
                (insert-in-order [1 1 1 1] 1) => [1 1 1 1 1]
                (insert-in-order [1 2 3] 2) => [1 2 2 3])
              (fact "supports 2-way comparison functions"
                (insert-in-order > [3 2 1] 2) => [3 2 2 1])
              (fact "supports 3-way comparison functions"
                (insert-in-order #(compare %2 %1) [3 2 1] 2) => [3 2 2 1])
              (fact "supports infinite sequences"
                (take 5 (insert-in-order (range) 2)) => [0 1 2 2 3]))


;; Implement a function that merges two sorted sequences, retaining the order

(defn merge-seqs
  ([xs ys]
   (merge-seqs compare xs ys))
  ([cmp xs ys]
    ))

(future-facts "about merge-seqs"
              (fact "works"
                (merge-seqs [] []) => []
                (merge-seqs [] [1 2 3]) => [1 2 3]
                (merge-seqs [1 2 3] []) => [1 2 3]
                (merge-seqs [1] [1]) => [1 1]
                (merge-seqs [2] [1]) => [1 2]
                (merge-seqs [1 3 5] [2 4 6]) => [1 2 3 4 5 6])
              (fact "supports 2-way comparison functions"
                (merge-seqs > [5 3 1] [6 4 2]) => [6 5 4 3 2 1])
              (fact "supports 3-way comparison functions"
                (merge-seqs #(compare %2 %1) [5 3 1] [6 4 2]) => [6 5 4 3 2 1])
              (fact "works with infinite sequences"
                (take 6 (merge-seqs [1 2] (range))) => [0 1 1 2 2 3]
                (take 5 (merge-seqs (range) (range))) => [0 0 1 1 2]))
