(ns clojure-exercises.tree-reparenting
  (:require [midje.sweet :refer :all]))

;; https://www.4clojure.com/problem/130

(defn search-impl [[root & subtrees] goal path]
  (let [new-path (conj path root)]
    (if (= goal root)
      new-path
      (loop [[st & sts] subtrees]
        (when st
          (if-let [result (search-impl st goal new-path)]
            result
            (recur sts)))))))

(defn search [tree goal]
  (search-impl tree goal []))


(facts "about search"
  (search '[a] 'a) => '[a]
  (search '[a [b]] 'b) => '[a b]
  (search '[a [b [c] [d]] [e [f] [g [h] [i]]]] 'h)
  => '[a e g h])

(defn pred [child y]
  (and (sequential? child)
       (= y (first child))))

(defn detach-child [tree child]
  (remove #(pred % child) tree))

(defn extract-child [tree child]
  (first (filter #(pred % child) tree)))

(facts "about detach-child and extract-child"
  (detach-child '[a [b] [c]] 'b) => '[a [c]]
  (detach-child '[a [b] [c]] 'c) => '[a [b]]
  (extract-child '[a [b] [c]] 'c) => '[c])

(defn reparent [tree new-root]
  (loop [[cur-path & rest-path] (next (search tree new-root))
         acc tree]
    (if-not cur-path
      acc
      (let [new-child  (detach-child acc cur-path)
            new-parent (extract-child acc cur-path)]
        (recur rest-path (conj new-parent new-child))))))

(defn reparent2 [tree new-root]
  (reduce (fn [acc p]
            (conj (extract-child acc p) (detach-child acc p)))
          tree
          (next (search tree new-root))))

(facts "about reparent"
  (reparent '[a] 'a) => '[a]
  (reparent '[t [e] [a]] 'a) => '[a [t [e]]]
  (reparent '[a [b [d] [e]] [c [f] [g]]] 'd) => '[d [b [e] [a [c [f] [g]]]]]
  (reparent '[a [b [d] [e]] [c [f] [g]]] 'b) => '[b [d] [e] [a [c [f] [g]]]])

(facts "about reparent2"
  (reparent2 '[a] 'a) => '[a]
  (reparent2 '[t [e] [a]] 'a) => '[a [t [e]]]
  (reparent2 '[a [b [d] [e]] [c [f] [g]]] 'd) => '[d [b [e] [a [c [f] [g]]]]]
  (reparent2 '[a [b [d] [e]] [c [f] [g]]] 'b) => '[b [d] [e] [a [c [f] [g]]]])
