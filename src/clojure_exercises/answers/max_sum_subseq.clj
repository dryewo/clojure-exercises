(ns clojure-exercises.answers.max-sum-subseq
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Maximum_subarray_problem

(defn max-sum-subseq [coll]
  (loop [[c :as cs] coll
         best     []
         best-sum 0
         cur      []
         cur-sum  0]
    (if (empty? cs)
      best
      (let [new-sum (+ cur-sum c)]
        (if (neg? new-sum)
          (recur (rest cs) best best-sum [] 0)
          (let [new-cur (conj cur c)]
            (if (> new-sum best-sum)
              (recur (rest cs) new-cur new-sum new-cur new-sum)
              (recur (rest cs) best best-sum new-cur new-sum))))))))

(defn do-benchmark [n]
  (let [datas (repeatedly 10 #(shuffle (range n)))]
    (doseq [d datas]
      (max-sum-subseq d))))

(comment
  (reduce + (max-sum-subseq (shuffle (range -50 50))))
  (time (do-benchmark 100))
  (time (do-benchmark 1000))
  (time (do-benchmark 10000))
  (time (do-benchmark 100000))
  (max-subarray [-1 -1 2 -1 -1])
  )

(defn test-max-sum-subseq []
  (facts "about max-sum-subseq"
    (max-sum-subseq nil) => []
    (max-sum-subseq []) => []
    (max-sum-subseq [1]) => [1]
    (max-sum-subseq [1 2]) => [1 2]
    (max-sum-subseq [-1]) => []
    (max-sum-subseq [-1 1]) => [1]
    (max-sum-subseq [1 -1]) => [1]
    (max-sum-subseq [0]) => []
    (max-sum-subseq [10 -20 11]) => [11]
    (max-sum-subseq [10 -20 5]) => [10]
    (max-sum-subseq [1 0 2]) => [1 0 2]
    (max-sum-subseq [1 1 1 -4 1 3 -1]) => [1 3]
    (max-sum-subseq [1 1 1 -4 1 2 -1]) => [1 1 1]
    (max-sum-subseq [-1 -1 -1 -1]) => []
    (max-sum-subseq [-1 -1 2 -1 -1]) => [2]))
(test-max-sum-subseq)


;; Fancy implementation, ~5x slower

(defn generic-max [a b]
  (if (neg? (compare a b)) b a))

(defrecord CollSum [coll sum]
  Comparable
  (compareTo [_ that] (.compareTo sum (:sum that))))

(defn max-subarray-pos+ [acc x]
  (if (neg? (:sum acc))
    (-> acc (assoc :coll [x]) (assoc :sum x))
    (-> acc (update :coll conj x) (update :sum + x))))

(defn max-subarray [A]
  (let [ending-heres (reductions max-subarray-pos+ (->CollSum [] 0) A)]
    (:coll (reduce generic-max ending-heres))))

(comment
  (reductions max-subarray-pos+ (->CollSum [] 0) [1 2 -4 2 -1 3 -1 -2]))

(facts "about generic-max"
  (generic-max 1 2) => 2
  (generic-max 2 1) => 2)

(facts "about CollSum"
  (compare (->CollSum anything 5) (->CollSum anything 6)) => (compare 5 6)
  (generic-max (->CollSum anything 5) (->CollSum anything 6)) => (->CollSum anything 6))

(facts ""
  (max-subarray-pos+ (->CollSum [] 0) 1) => (->CollSum [1] 1)
  (max-subarray-pos+ (->CollSum [1 2 3] 6) -7) => (->CollSum [1 2 3 -7] -1)
  (max-subarray-pos+ (->CollSum [-1] -1) -2) => (->CollSum [-2] -2))

(facts "about max-subarray"
  (with-redefs [max-sum-subseq max-subarray]
    (test-max-sum-subseq)))
