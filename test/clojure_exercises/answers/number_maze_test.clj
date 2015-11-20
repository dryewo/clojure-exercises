(ns clojure-exercises.answers.number-maze-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clojure-exercises.answers.number-maze :refer :all]))

(facts "about step"
  (step OPS [1]) => [[1 2] [1 3]]
  (step OPS [2]) => [[2 1] [2 4]]
  (step OPS [2 4]) => [[2 4 8] [2 4 6]]
  (step OPS [6 3]) => [[6 3 5]])

(facts "about solve"
  (solve 1 1) => [1]
  (solve 3 12) => [3 6 12]
  (solve 12 3) => [12 6 3]
  (solve 5 9) => [5 7 9]
  (solve 9 2) => [9 18 20 10 12 6 8 4 2]
  (solve 9 12) => [9 18 20 10 12])
