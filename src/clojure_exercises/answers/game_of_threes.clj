(ns clojure-exercises.answers.game-of-threes
  (:require [midje.sweet :refer :all]))

(defn game-of-threes [n]
  (loop [i n
         acc []]
    (if (<= i 1)
      acc
      (case (rem i 3)
        0 (recur (/ i 3) (conj acc [i 0]))
        1 (recur (dec i) (conj acc [i -1]))
        2 (recur (inc i) (conj acc [i 1]))))))

(defn unfold [z f]
  (lazy-seq
    (when-let [[x s] (f z)]
      (cons x (unfold s f)))))

(defn game-of-threes2 [n]
  (unfold n #(when-not (<= % 1)
              (case (rem % 3)
                0 [[% 0] (/ % 3)]
                1 [[% -1] (dec %)]
                2 [[% 1] (inc %)]))))

(facts "about game-of-threes"
  (game-of-threes -100) => []
  (game-of-threes 1) => []
  (game-of-threes 100) => [[100 -1] [99 0] [33 0] [11 1] [12 0] [4 -1] [3 0]])

(facts "about game-of-threes2"
  (game-of-threes2 -100) => []
  (game-of-threes2 1) => []
  (game-of-threes2 100) => [[100 -1] [99 0] [33 0] [11 1] [12 0] [4 -1] [3 0]])

(facts "about unfold"
  (take 5 (unfold 1 #(vector % (inc %)))) => [1 2 3 4 5]
  (unfold 1 #(when-not (= % 3)
              [% (inc %)])) => [1 2])

