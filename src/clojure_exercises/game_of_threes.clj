(ns clojure-exercises.game-of-threes
  (:require [midje.sweet :refer :all]))

;; https://www.reddit.com/r/dailyprogrammer/comments/3r7wxz

(defn game-of-threes [n]
  [])

(future-facts "about game-of-threes"
              (game-of-threes -100) => []
              (game-of-threes 1) => []
              (game-of-threes 100) => [[100 -1] [99 0] [33 0] [11 1] [12 0] [4 -1] [3 0]])
