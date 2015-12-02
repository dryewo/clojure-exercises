(ns clojure-exercises.eratosthenes
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes

(defn divisible? [n q]
  (zero? (rem n q)))

(facts "about divisible?"
  (divisible? 9 2) => false
  (divisible? 8 4) => true)

(defn sieve [coll m]
  (into [] (remove #(divisible? % m) coll)))

(facts "about sieve"
  (sieve [2 3 4 5 6 7] 2) => [3 5 7]
  (sieve [2 3 4 5 6 7] 3) => [2 4 5 7])

(defn primes-up-to [n]
  (loop [[m & tail] (range 2 (inc n))
         r []]
    (if-not m
      r
      (recur (sieve tail m) (conj r m)))))

(defn divisible-by-any? [n coll]
  (boolean (some #(divisible? n %) coll)))

(fact "about divisible-by-any?"
  (divisible-by-any? 10 [2 3 5 7]) => true
  (divisible-by-any? 11 [2 3 5 7]) => false)

(defn primes-up-to2 [n]
  (reduce (fn [acc x]
            (if (divisible-by-any? x acc)
              acc
              (conj acc x)))
          [] (range 2 (inc n))))

(facts "about primes-up-to"
  (primes-up-to 100) => [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97])
(facts "about primes-up-to2"
  (primes-up-to2 100) => [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97])
