(ns clojure-exercises.answers.factorial
  (:require [midje.sweet :refer :all]))

(defn factorial [n]
  (if (zero? n)
    1
    (*' n (factorial (dec n)))))

(defn factorial-t [n acc]
  ;(println n acc)
  (if (zero? n)
    acc
    (recur (dec n) (*' acc n))))

(defn factorial-tailrec [num]
  (factorial-t num 1))

(defn factorial-loop [num]
  (loop [n   num
         acc 1]
    (if (zero? n)
      acc
      (recur (dec n) (*' acc n)))))

(comment

  ;; Blows the stack
  (factorial 10000)
  ;; Works
  (factorial-tailrec 10000)
  ;; Also works
  (factorial-loop 10000)

  )

(facts "about factorial"
  (factorial 0) => 1
  (factorial 3) => 6
  (factorial 5) => 120)

(facts "about factorial-t"
  (factorial-t 0 1) => 1
  (factorial-t 3 1) => 6
  (factorial-t 5 1) => 120)

(facts "about factorial-loop"
  (factorial-loop 0) => 1
  (factorial-loop 3) => 6
  (factorial-loop 5) => 120)
