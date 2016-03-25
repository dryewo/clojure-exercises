(ns clojure-exercises.answers.sorted-merge
  (:require [midje.sweet :refer :all]
            [clojure.core.match :refer [match]])
  (:import (java.util Comparator)))

;; Inserting an item into a sorted collection

;; Does more than intended, actually doing one iteration of quick-sort
(defn insert-in-order
  ([coll x]
   (insert-in-order compare coll x))
  ([cmp coll x]
   (let [pred #(neg? (.compare ^Comparator cmp % x))]
     (concat (filter pred coll)
             [x]
             (remove pred coll)))))

;; ~3x slower, but supports infinite sequences
(defn insert-in-order2
  ([coll x]
   (insert-in-order2 compare coll x))
  ([cmp [c :as coll] x]
   (lazy-seq
     (if (seq coll)
       (if (neg? (.compare ^Comparator cmp x c))
         (cons x coll)
         (cons c (insert-in-order2 cmp (rest coll) x)))
       (list x)))))

;; As slow as previous, but shorter
(defn insert-in-order3
  ([coll x]
   (insert-in-order3 compare coll x))
  ([cmp coll x]
   (let [pred #(neg? (.compare ^Comparator cmp % x))]
     (concat (take-while pred coll)
             [x]
             (drop-while pred coll)))))

(comment
  (let [coll (range 100000)]
    (time (count (insert-in-order coll 500000)))
    (time (count (insert-in-order2 coll 500000)))
    (time (count (insert-in-order3 coll 500000))))
  )

(facts "about insert-in-order"
  (insert-in-order [1 1 1 1] 1) => [1 1 1 1 1]
  (insert-in-order [1 2 3] 2) => [1 2 2 3]
  (insert-in-order > [3 2 1] 2) => [3 2 2 1]
  (insert-in-order2 [1 1 1 1] 1) => [1 1 1 1 1]
  (insert-in-order2 [1 2 3] 2) => [1 2 2 3]
  (insert-in-order2 > [3 2 1] 2) => [3 2 2 1]
  (take 5 (insert-in-order2 (range) 2)) => [0 1 2 2 3])

(facts "about insert-in-order3"
  (fact "works"
    (insert-in-order3 nil 1) => [1]
    (insert-in-order3 [] 1) => [1]
    (insert-in-order3 [1 1 1 1] 1) => [1 1 1 1 1]
    (insert-in-order3 [1 2 3] 2) => [1 2 2 3])
  (fact "supports 2-way comparison functions"
    (insert-in-order3 > [3 2 1] 2) => [3 2 2 1])
  (fact "supports 3-way comparison functions"
    (insert-in-order3 #(compare %2 %1) [3 2 1] 2) => [3 2 2 1])
  (fact "supports infinite sequences"
    (take 5 (insert-in-order3 (range) 2)) => [0 1 2 2 3]))

(comment
  (do
    (time (last (take 100000 (map (partial + 100) (range)))))
    (time (last (take 100000 (iterate inc' 100)))))
  )


;; Merging sorted collections

(defn merge-seqs
  ([xs ys]
   (merge-seqs compare xs ys))
  ([cmp [x :as xs] [y :as ys]]
   (lazy-seq
     (if (empty? xs)
       ys
       (if (empty? ys)
         xs
         (if (neg? (.compare ^Comparator cmp x y))
           (cons x (merge-seqs cmp (rest xs) ys))
           (cons y (merge-seqs cmp xs (rest ys)))))))))

;; For some reason does not work with infinite seqs (???)
(defn merge-seqs2
  ([xs ys]
   (merge-seqs2 compare xs ys))
  ([cmp [x :as xs] [y :as ys]]
   (lazy-seq
     (match [(seq xs) (seq ys)]
       [nil ys] ys
       [xs nil] xs
       [_ _] (if (neg? (.compare ^Comparator cmp x y))
               (cons x (merge-seqs2 cmp (rest xs) ys))
               (cons y (merge-seqs2 cmp xs (rest ys))))))))

(comment
  (let [X (range 10000)
        Y (range 10000)]
    (time (count (merge-seqs X Y)))
    (time (count (merge-seqs2 X Y))))
  )

(facts "about merge-seqs"
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
