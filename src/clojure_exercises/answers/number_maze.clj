(ns clojure-exercises.answers.number-maze
  (:require [midje.sweet :refer :all])
  (:import (clojure.lang PersistentQueue)))

(def OPS [#(if (even? %) (/ % 2) nil)
          #(* % 2)
          #(+ % 2)])

(defn step [ops path]
  (let [n (last path)]
    (->> ops
         (map #(% n))
         (remove nil?)
         (distinct)
         (remove (set path))
         (map #(conj path %)))))

;; Bad complexity
(defn solve [start end]
  (loop [paths [[start]]]
    (if-let [res (some #(when (= end (last %)) %) paths)]
      res
      (recur (mapcat #(step OPS %) paths)))))

;; Proper breadth-first search

(defn search-bf [start neighbors goal?]
  (loop [queue (conj PersistentQueue/EMPTY start)
         previous {}                                        ; map: x -> previous(x)
         visited #{}]
    (when (seq queue)
      (let [cur (peek queue)]
        (if (goal? cur)
          ;; build path from start to cur:
          (reverse (take-while (complement nil?) (iterate previous cur)))
          (let [nbrs (remove visited (neighbors cur))]
            (recur (into (pop queue) nbrs)
                   (into previous (for [n nbrs :when (not (previous n))]
                                    [n cur]))
                   (conj visited cur))))))))

(defn neighbors-fn [ops]
  (fn [n]
    (->> ops
         (map #(% n))
         (remove nil?))))

;; ~2x slower
(defn neighbors-fn2 [ops]
  (let [juxt-ops (apply juxt ops)]
    (fn [n]
      (remove nil? (juxt-ops n)))))

;; ~3x slower
(defn neighbors-fn3 [ops]
  (fn [n]
    (sequence
      (comp (map #(% n))
            (remove nil?))
      ops)))

;; Much faster than solve:
;; [32 63]   3x: 0.019s vs 0.064s
;; [64 127]  10x: 0.073s vs 0.729
;; [128 255] 27x: 0.4s vs 11s

;; These results do not correlate with neighbors-fn benchmarks ???
;; Version using neighbors-fn2 is the fastest (results for [9 2])
;; ~1 ms
(defn solve-naive [start end]
  (search-bf start (neighbors-fn OPS) #(= % end)))
;; ~0.87 ms
(defn solve-naive2 [start end]
  (search-bf start (neighbors-fn2 OPS) #(= % end)))
;; ~0.90 ms
(defn solve-naive3 [start end]
  (search-bf start (neighbors-fn3 OPS) #(= % end)))

;; TESTS

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

(facts "about search-naive"
  (let [visited (atom [])
        res (search-bf 0
                       (juxt inc dec)
                       (fn [x]
                         (swap! visited conj x)
                         (= x 5)))]
    [res @visited]) => [[0 1 2 3 4 5] [0 1 -1 2 -2 3 -3 4 -4 5]])

(facts "about neighbors-fn"
  ((neighbors-fn OPS) 3) => [6 5]
  ((neighbors-fn OPS) 4) => [2 8 6])

(facts "about neighbors-fn2"
  ((neighbors-fn2 OPS) 3) => [6 5]
  ((neighbors-fn2 OPS) 4) => [2 8 6])

(facts "about neighbors-fn3"
  ((neighbors-fn3 OPS) 3) => [6 5]
  ((neighbors-fn3 OPS) 4) => [2 8 6])

(facts "about solve-naive"
  (solve-naive 1 1) => [1]
  (solve-naive 3 12) => [3 6 12]
  (solve-naive 12 3) => [12 6 3]
  (solve-naive 5 9) => [5 7 9]
  (solve-naive 9 2) => [9 18 20 10 12 6 8 4 2]
  (solve-naive 9 12) => [9 18 20 10 12])
