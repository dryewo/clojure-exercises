(ns clojure-exercises.eratosthenes
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes

(defn primes-up-to [n]
  [])

(future-facts "about primes-up-to"
              (primes-up-to 100) => [2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97])
