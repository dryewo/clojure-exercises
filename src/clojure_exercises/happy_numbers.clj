(ns clojure-exercises.happy-numbers
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Happy_number

(defn happy? [n]
  true)

(future-facts "about happy?"
              (filter happy? (range 100))
              => [1 7 10 13 19 23 28 31 32 44 49 68 70 79 82 86 91 94 97])
