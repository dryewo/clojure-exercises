(ns clojure-exercises.typoglycemia
  (:require [schema.core :as s]
            [midje.sweet :refer :all]
            [clojure-exercises.answers.typoglycemia :refer [typoglycemic?]]))

;; https://www.reddit.com/r/dailyprogrammer/comments/3s4nyq

(s/defn typoglycemize :- s/Str
  [text :- (s/maybe s/Str)]
  "")

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