(ns clojure-exercises.answers.game-of-threes-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clojure-exercises.answers.game-of-threes :refer :all]))

(defn test-game-of-threes [f]
  (is (= [] (f -100)))
  (is (= [] (f 1)))
  (is (= [[100 -1] [99 0] [33 0] [11 1] [12 0] [4 -1] [3 0]] (f 100))))

(deftest can-game-of-threes
  (test-game-of-threes game-of-threes)
  (test-game-of-threes game-of-threes2))

(facts "about unfold"
  (take 5 (unfold 1 #(vector % (inc %)))) => [1 2 3 4 5]
  (unfold 1 #(when-not (= % 3)
              [% (inc %)])) => [1 2])