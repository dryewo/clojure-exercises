(ns clojure-exercises.answers.eratosthenes
  (:require [midje.sweet :refer :all]))

(defn primes-up-to [n]
  (loop [res []
         [i :as input] (range 2 n)]
    (if-not i
      res
      (recur (conj res i) (into [] (remove #(zero? (rem % i)) input))))))

(defn primes-up-to2 [n]
  (loop [i 2
         res []]
    (if (>= i n)
      res
      (recur (inc i) (if (not-any? #(zero? (rem i %)) res)
                       (conj res i)
                       res)))))

(def primes-up-to-100
  [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97])

(facts "about primes-up-to"
  (primes-up-to 100) => primes-up-to-100
  (primes-up-to2 100) => primes-up-to-100)
