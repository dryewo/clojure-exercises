(ns clojure-exercises.balancing-brackets
  (:require [midje.sweet :refer :all]))

;; https://www.4clojure.com/problem/177

(defn balanced? [text]
  )

(future-facts "about balanced?"
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
