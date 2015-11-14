(ns clojure-exercises.typoglycemia-test
  (:require [midje.sweet :refer :all]
            [clojure-exercises.typoglycemia :refer :all]
            [clojure-exercises.answers.typoglycemia-test :refer [typoglycemic?]]))

(future-facts
  "about typoglycemize"
  (fact "in some cases it does not change"
    (typoglycemize "") => ""
    (typoglycemize "1234") => "1234"
    (typoglycemize "a") => "a"
    (typoglycemize "are") => "are")
  (fact
    (typoglycemize "Formation")
    => (typoglycemic? "Formation"
                      #"^F[ormatio]{7}n$")
    (typoglycemize "Surely You're Joking, Mr. Feynman!")
    => (typoglycemic? "Surely You're Joking, Mr. Feynman!"
                      #"^S[urel]{4}y You're J[okin]{4}g, Mr. F[eynma]{5}n!$")))