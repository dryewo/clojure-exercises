(ns clojure-exercises.answers.balancing-brackets
  (:require [instaparse.core :as insta]
            [midje.sweet :refer :all]))

(defn process-char [context c]
  (let [cur (last context)]
    (case c
      \( (conj context c)
      \) (if (= cur \() (pop context) (reduced [:error context c]))
      \{ (conj context c)
      \} (if (= cur \{) (pop context) (reduced [:error context c]))
      \[ (conj context c)
      \] (if (= cur \[) (pop context) (reduced [:error context c]))
      context)))

(defn balanced? [text]
  (empty? (reduce process-char [] text)))

(def parser (insta/parser "
Text         = Node*
Node         = TextNode | BracketsNode | ParentsNode | BracesNode
TextNode     = #'[^\\(\\)\\[\\]\\{\\}]+'
BracketsNode = '[' Node* ']'
ParentsNode  = '(' Node* ')'
BracesNode   = '{' Node* '}'
"))

;; ~100x slower
(defn balanced?2 [text]
  (not (insta/failure? (parser text))))

;; TESTS

(facts "about process-char"
  (process-char [] \() => [\(]
  (process-char [] \[) => [\[]
  (process-char [] \{) => [\{]
  (process-char [] \a) => []
  (process-char [\(] \)) => []
  (process-char [\[] \]) => []
  (process-char [\{] \}) => []
  (unreduced (process-char [\(] \])) => [:error [\(] \]]
  (unreduced (process-char [] \))) => [:error [] \)])

(facts "about balanced?"
  (balanced? " nfjosf ") => true
  (balanced? "edfe(efwe)b") => true
  (balanced? "fr[er] erf") => true
  (balanced? "asf {  }ea  ") => true
  (balanced? "({[]})") => true
  (balanced? "]") => false
  (balanced? "[(])") => false
  (balanced? "[}") => false
  (balanced? "]") => false
  (balanced? "{") => false)

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

(facts "about balanced?2"
  (balanced?2 " nfjosf ") => true
  (balanced?2 "edfe(efwe)b") => true
  (balanced?2 "fr[er] erf") => true
  (balanced?2 "asf {  }ea  ") => true
  (balanced?2 "({[]})") => true
  (balanced?2 "]") => false
  (balanced?2 "[(])") => false
  (balanced?2 "[}") => false
  (balanced?2 "]") => false
  (balanced?2 "{") => false)
