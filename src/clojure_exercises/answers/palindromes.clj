(ns clojure-exercises.answers.palindromes)

(defn normalize-text [text]
  (into []
        (comp (filter #(Character/isLetter ^Character %))
              (map #(Character/toLowerCase ^Character %)))
        text))

(defn palindrome? [text]
  (let [normalized (normalize-text text)]
    (= normalized
       (rseq normalized))))
