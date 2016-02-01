(ns clojure-exercises.answers.countdown
  (:require [midje.sweet :refer :all]
            [clojure.string :as str]
            [clojure-exercises.answers.utils :refer [spy]]
            [criterium.core :as crit]))

;; http://www.cs.nott.ac.uk/~pszgmh/countdown.pdf
;; http://usi-pl.github.io/lc/sp-2015/doc/Bird_Wadler.%20Introduction%20to%20Functional%20Programming.1ed.pdf

(defn subseqs [[x & xs]]
  (if-not x
    [()]
    (let [sxs (subseqs xs)]
      (concat sxs (map (partial cons x) sxs)))))

(defn interleave' [x [y & ys]]
  (if-not y
    [[x]]
    (cons (list* x y ys)
          (map (partial cons y) (interleave' x ys)))))

(defn perms [[x & xs]]
  (if-not x
    [()]
    (let [pxs (perms xs)]
      (mapcat (partial interleave' x) pxs))))

(defn selections [n xs]
  (if-not (pos? n)
    [()]
    (let [sxs (selections (dec n) xs)]
      (mapcat #(for [x xs] (cons x %)) sxs))))

(facts "subseqs"
  (map (partial apply str) (subseqs (seq "123"))) => ["" "3" "2" "23" "1" "13" "12" "123"]
  (count (subseqs (range 8))) => 256)

(facts "interleave'"
  (interleave' 1 [2 2]) => [[1 2 2] [2 1 2] [2 2 1]])

(facts "perms"
  (perms [1 2 3]) => '((1 2 3) (2 1 3) (2 3 1) (1 3 2) (3 1 2) (3 2 1))
  (count (perms (range 5))) => 120)

(facts "selections"
  (selections 3 [0 1]) => '((0 0 0) (1 0 0) (0 1 0) (1 1 0) (0 0 1) (1 0 1) (0 1 1) (1 1 1))
  (count (selections 4 (range 4))) => 256)

(comment

  (subseqs [1 2 3])
  (crit/quick-bench (count (subseqs (range 18))))
  (time (count (subseqs (range 21))))
  (time (count (perms (range 10))))
  (count (mapcat perms (subseqs (range 6))))

  1 2 3 4
  (1 + 2) * (3 + 4)
  1 2 + 3 4 + *

  ;  (()) => ((cons 0 ())) (cons 1 ()))
  ;((0) (1)) => ()

  )
