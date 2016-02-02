(ns clojure-exercises.answers.factorial
  (:require [midje.sweet :refer :all]))

(defn factorial-naive [n]
  (if (zero? n)
    1
    (*' n (factorial-naive (dec n)))))

(defn factorial-tailrec-impl [n acc]
  (if (zero? n)
    acc
    (recur (dec n) (*' acc n))))

(defn factorial-tailrec [num]
  (factorial-tailrec-impl num 1))

(defn factorial-loop [num]
  (loop [n   num
         acc 1]
    (if (zero? n)
      acc
      (recur (dec n) (*' acc n)))))

(defn factorial-reduce [n]
  (reduce *' 1 (range 2 (inc n))))

(comment

  ;; Blows the stack (sometimes)
  (factorial-naive 20000)
  ;; Works
  (factorial-tailrec 15000)
  ;; Also works
  (factorial-loop 15000)
  ;; Works too
  (factorial-reduce 15000)
  )

(facts "about factorial"
  (factorial-naive 0) => 1
  (factorial-naive 3) => 6
  (factorial-naive 5) => 120
  (factorial-naive 25) => anything
  (factorial-naive 15000) => (throws StackOverflowError))

(facts "about factorial-tailrec"
  (factorial-tailrec 0) => 1
  (factorial-tailrec 3) => 6
  (factorial-tailrec 5) => 120
  (factorial-tailrec 25) => anything
  (factorial-tailrec 15000) => anything)

(facts "about factorial-loop"
  (factorial-loop 0) => 1
  (factorial-loop 3) => 6
  (factorial-loop 5) => 120
  (factorial-loop 25) => anything
  (factorial-loop 15000) => anything)

(facts "about factorial-reduce"
  (factorial-reduce 0) => 1
  (factorial-reduce 3) => 6
  (factorial-reduce 5) => 120
  (factorial-reduce 25) => anything
  (factorial-reduce 15000) => anything)
