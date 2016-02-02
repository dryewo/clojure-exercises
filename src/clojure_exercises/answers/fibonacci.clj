(ns clojure-exercises.answers.fibonacci
  (:require [midje.sweet :refer :all]))

(defn fibonacci-naive [n]
  (cond
    (< 1 n) (+' (fibonacci-naive (- n 1)) (fibonacci-naive (- n 2)))
    (= 1 n) 1
    :else 0))

(defn fibonacci-tailrec-impl [n prev cur]
  (cond
    (< 1 n) (recur (dec n) cur (+' prev cur))
    (= 1 n) cur
    :else 0))

(defn fibonacci-tailrec [n]
  (fibonacci-tailrec-impl n 0 1))

(defn fibonacci-loop [n]
  (loop [n    n
         prev 0
         cur  1]
    (cond
      (< 1 n) (recur (dec n) cur (+' prev cur))
      (= 1 n) cur
      :else 0)))

(facts "fibonacci-naive"
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

(facts "fibonacci-tailrec"
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

(facts "fibonacci-loop"
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
