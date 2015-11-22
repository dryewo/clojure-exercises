(ns clojure-exercises.answers.search-a-star
  (:require [midje.sweet :refer :all]
            [clojure.data.priority-map :refer [priority-map]]))

;; Gives neighbours cost of 1: [1 2 3] into {1 1, 2 1, 3 1}
(defn normalized-neighbours [neighbours-fn]
  #(let [ns (neighbours-fn %)]
    (if (map? ns)
      ns
      (into {} (for [n ns] [n 1])))))

;; A* search
;; https://en.wikipedia.org/wiki/A*_search_algorithm
;; Finds cheapest path from start to goal.
;; Parameters:
;;  start - start node
;;  neighbours - function: x -> {n1 c1, n2 c2 ...}, neighbours with costs
;;                      or x -> [n1 n2 ...] (will be converted to {n1 1, n2 1 ...}
;;  h - heuristic, h(x), estimated cost from x to goal, should never overestimate the minimal cost
;;  goal? - predicate for goal
;; Implementation names:
;; g - g(x), known cost from start to x
;; f - f(x) = g(x) + h(x)
;; TODO Implement optimization for monotonic h(x)
(defn search-A* [start neighbours h goal?]
  (let [nbrs (normalized-neighbours neighbours)
        h (memoize #(or (h %) 0))]
    (loop [queue (priority-map start (h start))
           previous {}
           f {start 0}]
      (when-let [[x fx] (peek queue)]
        ;(println (seq queue))
        (if (goal? x)
          (reverse (take-while identity (iterate previous x)))
          (let [gx (- fx (h x))
                better-fns (for [[n cn] (nbrs x)
                                 :let [gn (+ gx cn)
                                       old-fn (f n Double/POSITIVE_INFINITY)
                                       fn (+ gn (h n))]
                                 :when (< fn old-fn)]
                             [n fn])]
            (recur (into (pop queue) better-fns)
                   (into previous (for [[n] better-fns] [n x]))
                   (into f better-fns))))))))

;; {[:a :b] 1, [:a :c] 2} => {:a {:b 1 :c 2}}
(defn edges->neighbours [edges]
  (reduce (fn [acc [[a b] c]]
            (update acc a assoc b c))
          {} edges))

;; TESTS

(facts "about normalized-neighbours"
  ((normalized-neighbours (juxt dec inc)) 2) => {1 1, 3 1})

(facts "about edges->neighbours"
  (edges->neighbours {[:a :b] 1, [:a :c] 2}) => {:a {:b 1 :c 2}})

(facts "about search-A*"
  ;; https://en.wikipedia.org/wiki/A*_search_algorithm#/media/File:AstarExampleEn.gif
  (let [edges {[:start :a] 1.5, [:a :b] 2, [:b :c] 3, [:c :goal] 4
               [:start :d] 2, [:d :e] 3, [:e :goal] 2}
        neighbours (edges->neighbours edges)
        h {:a 4 :b 2 :c 4 :d 4.5 :e 2}]
    (search-A* :start neighbours h #(= % :goal)) => [:start :d :e :goal]))

;; This solution to Number Maze is wrong due to invalid heuristic
;; (solve-A* 16 31) => [16 18 20 40 42 21 23 25 27 29 31]
;; instead of          [16 8 10 12 14 28 56 58 29 31]

;(def OPS [#(if (even? %) (/ % 2) nil)
;          #(* % 2)
;          #(+ % 2)])
;
;(defn neighbours-fn [ops]
;  (let [juxt-ops (apply juxt ops)]
;    (fn [n]
;      (remove nil? (juxt-ops n)))))
;
;(defn solve-A* [start end]
;  (let [h #(Math/log (Math/abs ^Integer (- end %)))
;        goal? #(= % end)]
;    (search-A* start (neighbours-fn OPS) h goal?)))
