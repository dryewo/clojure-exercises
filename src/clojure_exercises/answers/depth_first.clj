(ns clojure-exercises.answers.depth-first
  (:require [midje.sweet :refer :all])
  (:import (clojure.lang LazySeq)))

;; Depth-first tree traversal. Same as clojure.core/tree-seq

(defn tree-seq-df [children root]
  (tree-seq children children root))

;; TESTS

(def TALL_TREE (into {} (for [r (range 100000)]
                          [r [(inc r)]])))

(facts "about tree-seq-df"
  (take 10 (tree-seq-df (juxt dec inc) 0)) => [0 -1 -2 -3 -4 -5 -6 -7 -8 -9]
  ;;     1
  ;;    / \
  ;;   2   3
  ;;  / \  |
  ;; 4   5 6
  (map first (tree-seq-df next [1 [2 [4] [5]] [3 [6]]])) => [1 2 4 5 3 6]
  ;;     :a
  ;;     / \
  ;;   :b  :c
  ;;   / \  |
  ;; :d  :e :f
  (tree-seq-df {:a [:b :c], :b [:d :e], :c [:f]} :a) => [:a :b :d :e :c :f]
  (fact "Can potentially handle infinite trees"
    (class (tree-seq-df (constantly nil) 0)) => LazySeq)
  (fact "Does not blow the stack"
    (tree-seq-df TALL_TREE 0) => (has-suffix [99999 100000])))
