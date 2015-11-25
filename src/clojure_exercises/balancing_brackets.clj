(ns clojure-exercises.balancing-brackets
  (:require [midje.sweet :refer :all]))

;; https://www.4clojure.com/problem/177

;; ( { ) }

(defn spy [x]
  (prn x)
  x)

(defn balanced? [text]
  (let [closing? {\) \(
                  \] \[
                  \} \{}
        opening? (set (vals closing?))]
    (empty? (spy (reduce (fn [acc c]
                           (cond
                             (opening? c) (conj acc c)
                             (closing? c) (if (= (last acc) (closing? c))
                                            (pop acc)
                                            (reduced [:error acc c]))
                             :else acc))
                         [] text)))))



(facts "about balanced?"
  (balanced? "This string has no brackets.") => true
  (balanced? "class Test {
      public static void main(String[] args) {
        System.out.println(\"Hello world.\");
      }
    }") => true
  (balanced? "([]([(()){()}(()(()))(([[]]({}()))())]((((()()))))))") => true
  (balanced? "(start, end]") => false
  (balanced? "())") => false
  (balanced? "[ { ] } ") => false
  (balanced? "([]([(()){()}(()(()))(([[]]({}([)))())]((((()()))))))") => false
  (balanced? "[") => false)
