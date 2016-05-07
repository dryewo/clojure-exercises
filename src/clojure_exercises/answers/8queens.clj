(ns clojure-exercises.answers.8queens
  (:require [midje.sweet :refer :all]
            [loco.core :refer [solutions solution]]
            [loco.constraints :refer :all]))

;; Adapted after https://github.com/jeffbrown/clojure-n-queens

(defn abs [n]
  (if (neg? n) (- n) n))

(defn is-valid-addition? [board proposed-col]
  (let [board-size (count board)]
    (every? false?
            (for [row (range board-size)
                  :let [col (board row)]]
              (or (= col proposed-col)
                  (= (- board-size row) (abs (- proposed-col col))))))))

(defn add-to-boards [boards board-size]
  (for [board          boards
        column-counter (range 1 (inc board-size))
        :when (is-valid-addition? board column-counter)]
    (conj board column-counter)))

(defn solve-for-board-size [board-size]
  (nth (iterate #(add-to-boards % board-size) [[]]) board-size))

(facts
  (count (solve-for-board-size 8)) => 92)

(comment
  (solve-for-board-size 4)
  (time (count (solve-for-board-size 11)))
  )

;; Solution using Loco

(defn prettify-solution [sol]
  (vals (into (sorted-map) sol)))

(defn solve-for-board-size2 [n]
  (let [indexes (range n)
        model   (concat
                  (for [i indexes] ($in [:x i] 1 n))
                  [($distinct (for [i indexes] [:x i]))
                   ($distinct (for [i indexes] ($- [:x i] i)))
                   ($distinct (for [i indexes] ($+ [:x i] i)))])]
    (map prettify-solution (solutions model))))

(facts ""
  (solve-for-board-size 8) => (just (solve-for-board-size2 8) :in-any-order))

(comment
  (solve-for-board-size2 4)
  (time (count (solve-for-board-size2 11)))
  (solutions [($in :x 0 1)
              ($in :y 0 1)
              ($distinct [($+ :x 1) ($+ :y 1)])]))
