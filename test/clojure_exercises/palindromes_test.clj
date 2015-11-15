(ns clojure-exercises.palindromes-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clojure-exercises.palindromes :refer :all]))

(future-facts
  "about palindrome?"
  (palindrome? "Are we not drawn onward, \nwe few, drawn onward to new area?")
  => false
  (palindrome? "A man, a plan, \na canal, a hedgehog, \na podiatrist, \nPanama!")
  => false
  (palindrome? "Was it a car\nor a cat\nI saw?")
  => true)
