(ns clojure-exercises.game-of-threes-test
  (:require [midje.sweet :refer :all]
            [clojure-exercises.game-of-threes :refer :all]))

(future-facts "about game-of-threes"
              (game-of-threes -100) => []
              (game-of-threes 1) => []
              (game-of-threes 100) => [[100 -1] [99 0] [33 0] [11 1] [12 0] [4 -1] [3 0]])
