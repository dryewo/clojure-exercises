(ns clojure-exercises.number-maze-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [clojure-exercises.number-maze :refer :all]))

(future-facts "about solve"
              (count (solve 1 1)) => 1
              (count (solve 3 12)) => 3
              (count (solve 12 3)) => 3
              (count (solve 5 9)) => 3
              (count (solve 9 2)) => 9
              (count (solve 9 12)) => 5)
