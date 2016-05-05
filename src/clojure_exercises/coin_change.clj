(ns clojure-exercises.coin-change
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Change-making_problem

(defn make-change [denominations n])

(future-facts "about make-change"
              (make-change [] anything) => {}
              (make-change [2 10] 13) => {}
              (make-change [1 20 25] 80) => {20 4}
              (make-change [1 5 10 25] 18) => {1 3 5 1 10 1})
