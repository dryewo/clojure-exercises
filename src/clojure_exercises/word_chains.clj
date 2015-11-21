(ns clojure-exercises.word-chains
  (:require [midje.sweet :refer :all]))

;; https://www.4clojure.com/problem/82

(defn chainable? [words]
  )

(future-facts "about chainable?"
              (chainable? #{"hat" "coat" "dog" "cat" "oat" "cot" "hot" "hog"}) => true
              (chainable? #{"spout" "do" "pot" "pout" "spot" "dot"}) => true
              (chainable? #{"share" "hares" "shares" "hare" "are"}) => true
              (chainable? #{"cot" "hot" "bat" "fat"}) => false
              (chainable? #{"to" "top" "stop" "tops" "toss"}) => false
              (chainable? #{"share" "hares" "hare" "are"}) => false)
