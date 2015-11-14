(ns clojure-exercises.answers.frequencies-test
  (:require [clojure.test :refer :all]
            [clojure-exercises.answers.frequencies :refer :all]))

(def colors ["brown", "red", "green", "yellow", "yellow", "brown", "brown", "black"])
(def result {"brown" 3, "red" 1, "green" 1, "yellow" 2, "black" 1})

(defn test-frequencies [fun]
  (is (= result (fun colors))))

(deftest can-frequencies
  (test-frequencies frequencies-via-reduce)
  (test-frequencies frequencies-via-reduce2)
  (test-frequencies frequencies-via-group-by)
  (test-frequencies frequencies-via-loop)
  (test-frequencies frequencies-via-recursion))
