(ns clojure-exercises.answers.typoglycemia
  (:require [schema.core :as s]))

(defn letter? [^Character c]
  (Character/isLetter c))

(s/defn processable-word? :- s/Bool
  [word :- s/Str]
  (boolean (and (> (count word) 3)
                (every? letter? word))))

(s/defn split-word :- [[Character]]
  [word :- s/Str]
  (let [len (count word)
        [start tail] (split-at 1 word)
        [mid end] (split-at (- len 2) tail)]
    [start mid end]))

(s/defn process-word :- s/Str
  [word :- s/Str]
  (let [[start mid end] (split-word word)]
    (apply str (flatten [start (shuffle mid) end]))))

(defn parse-words [text]
  (sequence (comp
              (partition-by letter?)
              (map #(apply str %)))
            text))

(s/defn typoglycemize :- s/Str
  [text :- (s/maybe s/Str)]
  (let [words (parse-words (str text))
        processed-words (for [w words]
                          (if (processable-word? w)
                            (process-word w)
                            w))]
    (apply str processed-words)))

