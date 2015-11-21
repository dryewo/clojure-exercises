(ns clojure-exercises.number-maze
  (:require [midje.sweet :refer :all]))

;; https://www.4clojure.com/problem/106

(defn solve [start end]
  )

(future-facts "about solve"
              (count (solve 1 1)) => 1
              (count (solve 3 12)) => 3
              (count (solve 12 3)) => 3
              (count (solve 5 9)) => 3
              (count (solve 9 2)) => 9
              (count (solve 9 12)) => 5)
