(ns clojure-exercises.answers.8queens
  (:require [midje.sweet :refer :all]))

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
  (time (count (solve-for-board-size 10)))
  )
