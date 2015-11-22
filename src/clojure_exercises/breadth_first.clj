(ns clojure-exercises.breadth-first
  (:require [midje.sweet :refer :all])
  (:import (clojure.lang LazySeq)))

(defn tree-seq-bf [children root]
  )

(future-facts "about tree-seq-bf"
              (class (tree-seq-bf (constantly nil) 0)) => LazySeq
              (tree-seq-bf (constantly nil) 0) => [0]
              ;;     1
              ;;    / \
              ;;   2   3
              ;;  / \  |
              ;; 4   5 6
              (map first (tree-seq-bf rest [1 [2 [4] [5]] [3 [6]]]))
              => [1 2 3 4 5 6]
              ;;     :a
              ;;     / \
              ;;   :b  :c
              ;;   / \  |
              ;; :d  :e :f
              (tree-seq-bf {:a [:b :c], :b [:d :e], :c [:f]} :a)
              => [:a :b :c :d :e :f])
