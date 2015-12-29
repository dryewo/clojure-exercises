(ns clojure-exercises.answers.tree-reparenting
  (:require [midje.sweet :refer :all]
            [clojure-exercises.answers.utils :refer [spy]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Search

(defn find-elem-impl
  [[node & children] goal path]
  (if-not node
    nil
    (let [new-path (conj path node)]
      (if (= goal node)
        new-path
        (some seq (for [c children]
                    (find-elem-impl c goal new-path)))))))

(defn find-elem
  "Non-tail-recursive depth-first search. Returns path to the goal element or nil."
  [tree elem]
  (find-elem-impl tree elem []))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tree API

(defn child-or-nil
  "(child-or-nil FOO) returns a predicate that passes only when given [FOO ...]"
  [child-root]
  #(when (and (sequential? %)
              (= child-root (first %)))
    %))

(defn append-child
  "[ROOT CHILDREN...] => [ROOT child CHILDREN...]"
  [[root & children :as tree] child]
  (if (and tree child)
    (cons root (cons child children))
    (or tree child)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Reparenting

(defn reparent-impl
  "Descends into the tree along the provided path, doing reparenting step by step."
  [tree path]
  (loop [dst nil
         src tree
         [p & ps] (next path)]
    ;(println dst src p ps)
    (if-not p
      (append-child src dst)
      (let [subtree            (some (child-or-nil p) src)
            all-except-subtree (remove (child-or-nil p) src)]
        (recur (append-child all-except-subtree dst)
               subtree
               ps)))))

(defn reparent [tree elem]
  (reparent-impl tree (find-elem tree elem)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Tests

(facts "about find-elem"
  (find-elem nil 'a) => nil
  (find-elem '[a] 'a) => '[a]
  (find-elem '[a] 'b) => nil
  (find-elem '[a [b [c]]] 'c) => '[a b c]
  (find-elem '[a [b [d] [e]] [c [f] [g]]] 'd) => '[a b d]
  (find-elem '[a [b [d] [e]] [c [f] [g]]] 'b) => '[a b])

(facts "about append-child"
  (append-child nil nil) => nil
  (append-child '[a b] nil) => '[a b]
  (append-child nil '[a b]) => '[a b]
  (append-child '[a [b]] '[c]) => '[a [c] [b]])

(facts "about reparent-impl"
  (reparent-impl '[t [e] [a]] nil) => '[t [e] [a]]
  (reparent-impl '[t [e] [a]] '[t]) => '[t [e] [a]]
  (reparent-impl '[t [e] [a]] '[t a]) => '[a [t [e]]]
  (reparent-impl '[a [b [d] [e]] [c [f] [g]]] '[a b d]) => '[d [b [a [c [f] [g]]] [e]]]
  (reparent-impl '[a [b [d] [e]] [c [f] [g]]] '[a b]) => '[b [a [c [f] [g]]] [d] [e]])

(facts "about reparent"
  (reparent '[a] 'a) => '[a]
  (reparent '[a] 'b) => '[a]
  (reparent '[t [e] [a]] 'a) => '[a [t [e]]]
  (reparent '[a [b [d] [e]] [c [f] [g]]] 'd) => '[d [b [a [c [f] [g]]] [e]]]
  (reparent '[a [b [d] [e]] [c [f] [g]]] 'b) => '[b [a [c [f] [g]]] [d] [e]])
