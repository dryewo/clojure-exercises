(ns clojure-exercises.roman
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Roman_numerals

(defn make-roman [n]
  )

(future-facts "about make-roman"
              (tabular
                (fact (make-roman ?n) => ?r)
                ?n ?r
                0 ""
                1 "I"
                2 "II"
                3 "III"
                1952 "MCMLII"
                2345 "MMCCCXLV"))
