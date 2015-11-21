(ns clojure-exercises.answers.increasing-subseq
  (:require [midje.sweet :refer :all]))

(defn increasing-prefix [s]
  (reduce (fn [acc x]
            (if (or (nil? x)
                    (and (seq acc)
                         (< x (last acc))))
              (reduced acc)
              (conj acc x)))
          [] s))

(defn tails [coll]
  (lazy-seq (when-let [s (seq coll)]
              (cons s (tails (rest s))))))

(defn longest-increasing-subseq-naive [s]
  (if-not (seq s)
    []
    (->> s
         tails
         (map increasing-prefix)
         (apply max-key count))))

;; ~2x faster
(defn longest-increasing-subseq [coll]
  (loop [s coll
         res []]
    (if-not (seq s)
      res
      (let [pr (increasing-prefix s)
            pr-len (count pr)]
        (recur (drop pr-len s)
               (if (> pr-len (count res))
                 pr
                 res))))))

;; TESTS

(facts "about tails"
  (tails []) => []
  (tails [1 2 3]) => [[1 2 3] [2 3] [3]])

(facts "about increasing-prefix"
  (increasing-prefix []) => []
  (increasing-prefix [1 2 3]) => [1 2 3]
  (increasing-prefix [1 2 1]) => [1 2]
  (increasing-prefix [3 2 1]) => [3])

(facts "about longest-increasing-subseq-naive"
  (longest-increasing-subseq-naive []) => []
  (longest-increasing-subseq-naive [1 2 3 4 5 1 2 3 4 6]) => [1 2 3 4 6]
  (longest-increasing-subseq-naive [1 2 3 4 5 4 3 2 3 4 5 4 3]) => [1 2 3 4 5]
  (longest-increasing-subseq-naive [9 8 7 6 5 4 3 2 1]) => [1])

(facts "about longest-increasing-subseq"
  (longest-increasing-subseq []) => []
  (longest-increasing-subseq [1 2 3 4 5 1 2 3 4 6]) => [1 2 3 4 5]
  (longest-increasing-subseq [1 2 3 4 5 4 3 2 3 4 5 4 3]) => [1 2 3 4 5]
  (longest-increasing-subseq [9 8 7 6 5 4 3 2 1]) => [9])