(ns clojure-exercises.fibonacci
  (:require [midje.sweet :refer :all]))

;; Fibonacci numbers:
;; F(n) = F(n-1) + F(n-2)
;; 0 1 1 2 3 5 8 13 21 ...

(defn fibonacci-naive
  "Using direct recursion"
  [n]
  )

(defn fibonacci-tailrec
  "Using helper function for tail recursion"
  [n]
  )

(defn fibonacci-loop
  "Using loop"
  [n]
  )

(future-facts "fibonacci-naive"
              (tabular
                (fact
                  (fibonacci-naive ?n) => ?res)
                ?n ?res
                0 0
                1 1
                2 1
                3 2
                4 3
                5 5
                6 8)
              (fibonacci-naive 15000) => (throws StackOverflowError))

(future-facts "fibonacci-tailrec"
              (tabular
                (fact
                  (fibonacci-tailrec ?n) => ?res)
                ?n ?res
                0 0
                1 1
                2 1
                3 2
                4 3
                5 5
                6 8)
              (fibonacci-tailrec 15000) => anything)

(future-facts "fibonacci-loop"
              (tabular
                (fact
                  (fibonacci-loop ?n) => ?res)
                ?n ?res
                0 0
                1 1
                2 1
                3 2
                4 3
                5 5
                6 8)
              (fibonacci-loop 15000) => anything)
