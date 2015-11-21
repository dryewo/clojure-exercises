(ns clojure-exercises.frequencies
  (:require [midje.sweet :refer :all]))

(defn frequencies' [coll]
  {})

(def colors ["brown", "red", "green", "yellow", "yellow", "brown", "brown", "black"])
(def result {"brown" 3, "red" 1, "green" 1, "yellow" 2, "black" 1})

(future-facts "about frequencies"
              (frequencies' colors) => result)
