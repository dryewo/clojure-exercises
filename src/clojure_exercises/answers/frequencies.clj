(ns clojure-exercises.answers.frequencies
  (:require [midje.sweet :refer :all]))

(defn frequencies-via-reduce [coll]
  (reduce (fn [acc v] (update acc v (fnil inc 0))) {} coll))

(defn frequencies-via-reduce2 [coll]
  (reduce (fn [acc x]
            (assoc acc x (inc (get acc x 0))))
          {} coll))

(defn frequencies-via-group-by [coll]
  (->> coll
       (group-by identity)
       (map (fn [[v arr]] [v (count arr)]))
       (into {})))

(defn frequencies-via-loop [coll]
  (loop [[c & cs] coll
         acc {}]
    (if c
      (recur cs (update acc c (fnil inc 0)))
      acc)))

(defn frequencies-via-recursion [coll]
  (if (seq coll)
    (merge-with + {(first coll) 1} (frequencies-via-recursion (next coll)))
    {}))

(def colors ["brown", "red", "green", "yellow", "yellow", "brown", "brown", "black"])
(def result {"brown" 3, "red" 1, "green" 1, "yellow" 2, "black" 1})

(facts "about frequencies"
  (frequencies-via-reduce colors) => result
  (frequencies-via-reduce2 colors) => result
  (frequencies-via-group-by colors) => result
  (frequencies-via-loop colors) => result
  (frequencies-via-recursion colors) => result)
