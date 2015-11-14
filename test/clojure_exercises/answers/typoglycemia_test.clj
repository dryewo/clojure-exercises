(ns clojure-exercises.answers.typoglycemia-test
  (:require [midje.sweet :refer :all]
            [clojure-exercises.answers.typoglycemia :refer :all]))

(facts "about split-word"
  (fact "behaves reasonably on non-conforming input"
    (split-word "") => [[] [] []]
    (split-word "a") => [[\a] [] []]
    (split-word "ab") => [[\a] [] [\b]])
  (fact "does what it's supposed to"
    (split-word "abaca") => [[\a] [\b \a \c] [\a]]))

(facts "about processable-word?"
  (processable-word? "") => false
  (processable-word? "a") => false
  (processable-word? "ab") => false
  (processable-word? "abc") => false
  (processable-word? "abca") => true
  (processable-word? "1234") => false)

(facts "about process-word"
  (fact "can shuffle"
    (process-word "more") => "mroe"
    (provided
      (shuffle [\o \r]) => [\r \o])))

(defn typoglycemic? [original-text re]
  (fn [actual-text]
    (boolean (and (not= original-text actual-text)
                  (re-seq re actual-text)))))

(facts "about typoglycemize"
  (fact "in some cases it does not change"
    (typoglycemize nil) => ""
    (typoglycemize "") => ""
    (typoglycemize "1234") => "1234"
    (typoglycemize "a") => "a"
    (typoglycemize "are") => "are"
    (typoglycemize "Big Ben") => "Big Ben")
  (fact "works on a single word"
    (typoglycemize "I am better") => "I am bteetr"
    (provided
      (process-word "better") => "bteetr"))
  (fact "works on sentences"
    (typoglycemize "Surely You're Joking, Mr. Feynman!") => anything
    (provided
      (process-word "Surely") => ""
      (process-word "Joking") => ""
      (process-word "Feynman") => ""))
  (fact
    (typoglycemize "Formation")
    => (typoglycemic? "Formation"
                      #"^F[ormatio]{7}n$")
    (typoglycemize "Surely You're Joking, Mr. Feynman!")
    => (typoglycemic? "Surely You're Joking, Mr. Feynman!"
                      #"^S[urel]{4}y You're J[okin]{4}g, Mr. F[eynma]{5}n!$")))
