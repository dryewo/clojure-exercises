(ns clojure-exercises.answers.happy-numbers
  (:require [midje.sweet :refer :all]))

(defn step [n]
  (->> n
       str
       (sequence (comp
                   (map str)
                   (map #(Integer/valueOf ^String %))
                   (map #(* % %))))
       (apply +)))

(defn happy? [n]
  (loop [i n
         seen? #{}]
    (if (= 1 i)
      true
      (if (seen? i)
        false
        (recur (step i) (conj seen? i))))))

(facts "about step"
  (step 0) => 0
  (step 1) => 1
  (step 14) => 17
  (step 68) => 100)

(facts "about happy?"
  (filter happy? (range 100))
  => [1 7 10 13 19 23 28 31 32 44 49 68 70 79 82 86 91 94 97])
