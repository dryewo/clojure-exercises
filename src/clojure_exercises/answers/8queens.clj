(ns clojure-exercises.answers.8queens
  (:require [midje.sweet :refer :all])
  (:import (clojure.lang PersistentQueue)))

;; board: [[row col] ...]

;(defn bounds-ok? [size board]
;  (every? #(<= 1 % size) (flatten board)))
;
;(defn count-ok? [size board]
;  (= size (count board)))
;
;(defn same-row? [size board]
;  (= size (count (set (map first board)))))
;
;(defn same-column? [size board]
;  (= size (count (set (map second board)))))
;
;;; [1 1] [2 2] [3 3]
;;; [1 2] [2 3] [3 4]
;;; [1 3] [2 2] [3 1]
;
;;; r c -> r - N + c
;
;(defn same-diagonal? [size board]
;  (and (= size (count (set (map (partial apply -) board))))
;       (= size (count (set (map (partial apply #(- %1 (- size %2))) board))))))
;
;(facts ""
;  (bounds-ok? 2 [[1 1] [2 2]]) => true
;  (bounds-ok? 2 [[1 1] [2 3]]) => false
;  (bounds-ok? 2 [[1 1] [2 0]]) => false
;  (count-ok? 2 [[1 1] [2 2]]) => true
;  (count-ok? 2 [[1 1]]) => false
;  (same-row? 2 [[1 2] [2 1]]) => true
;  (same-row? 2 [[1 1] [1 2]]) => false
;  (same-column? 2 [[1 2] [2 1]]) => true
;  (same-column? 2 [[1 1] [2 1]]) => false
;  (same-diagonal? 2 [[1 1] [2 2]]) => false
;  (same-diagonal? 2 [[1 2] [2 1]]) => false
;  (same-diagonal? 2 [[1 1] [1 2]]) => true)
;
;(defn check [size board]
;  (let [brd (set board)]
;    (and (count-ok? size brd)
;         (bounds-ok? size brd)
;         (same-row? size brd)
;         (same-column? size brd)
;         (same-diagonal? size brd))))
;
;(defn board->str [size board]
;  (clojure.string/join
;    "\n" (for [r (range 1 (inc size))]
;           (clojure.string/join
;             " " (for [c (range 1 (inc size))]
;                   (if (contains? (set board) [r c])
;                     "*"
;                     "-"))))))
;
;(comment
;  (println (board->str 2 [[1 1] [1 2]]))
;  )
;
;(defn queens-impl [size acc]
;  (if (= size (count acc))
;    acc))

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

(comment
  (time (count (solve-for-board-size 10)))
  )

;(defn queens [size]
;  (let [solutions (atom [])
;        impl-fn (fn impl [board]
;                  (if (= size (count board))
;                    (when (check size board)
;                      (swap! solutions conj board))
;                    (let [next-row (count board)]
;                      )))]
;    (impl-fn [])))

;(defn queens [size]
;  (loop [queue PersistentQueue/EMPTY
;         row 1
;         results []]
;    (if (> row size)
;      results
;      (let []
;        (recur (conj queue new-work) (inc row) results)))))

;(facts "queens"
;  )
