(ns clojure-exercises.tree-reparenting
  (:require [midje.sweet :refer :all]))

;; https://www.4clojure.com/problem/130

(defn reparent [tree elem]
  )

(future-facts "about reparent"
              (reparent '[a] 'a) => '[a]
              (reparent '[t [e] [a]] 'a) => '[a [t [e]]]
              (reparent '[a [b [d] [e]] [c [f] [g]]] 'd) => '[d [b [a [c [f] [g]]] [e]]]
              (reparent '[a [b [d] [e]] [c [f] [g]]] 'b) => '[b [a [c [f] [g]]] [d] [e]])
