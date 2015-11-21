(ns clojure-exercises.palindromes
  (:require [midje.sweet :refer :all]))

(defn palindrome? [text]
  )

(future-facts
  "about palindrome?"
  (palindrome? "Are we not drawn onward, \nwe few, drawn onward to new area?")
  => false
  (palindrome? "A man, a plan, \na canal, a hedgehog, \na podiatrist, \nPanama!")
  => false
  (palindrome? "Was it a car\nor a cat\nI saw?")
  => true)
