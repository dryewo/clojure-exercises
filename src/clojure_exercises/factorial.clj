(ns clojure-exercises.factorial
  (:require [midje.sweet :refer :all]))

;; Factorial:
;; n! = n * (n-1) * ... * 1

(defn factorial-naive
  "Using direct recursion"
  [n]
  )

;; Hint: use (recur ...)
(defn factorial-tailrec
  "Using helper function for tail recursion"
  [n]
  )

(defn factorial-loop
  "Using loop"
  [n]
  )

(defn factorial-reduce
  "Using reduce"
  [n]
  )

(future-facts "about factorial-naive"
              (factorial-naive 0) => 1
              (factorial-naive 3) => 6
              (factorial-naive 5) => 120
              (factorial-naive 25) => anything
              (factorial-naive 10000) => (throws StackOverflowError))

(future-facts "about factorial-tailrec"
              (factorial-tailrec 0) => 1
              (factorial-tailrec 3) => 6
              (factorial-tailrec 5) => 120
              (factorial-tailrec 25) => anything
              (factorial-tailrec 15000) => anything)

(future-facts "about factorial-loop"
              (factorial-loop 0) => 1
              (factorial-loop 3) => 6
              (factorial-loop 5) => 120
              (factorial-loop 25) => anything
              (factorial-loop 15000) => anything)

(future-facts "about factorial-reduce"
              (factorial-reduce 0) => 1
              (factorial-reduce 3) => 6
              (factorial-reduce 5) => 120
              (factorial-reduce 25) => anything
              (factorial-reduce 15000) => anything)
