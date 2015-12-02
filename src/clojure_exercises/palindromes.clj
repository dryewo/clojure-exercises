(ns clojure-exercises.palindromes
  (:require [midje.sweet :refer :all]
            [clojure.string :as str]))

(defn normalize [text]
  (->> text
       (filter #(Character/isLetter ^Character %))
       (map #(Character/toLowerCase ^Character %))))

(defn normalize2 [text]
  (sequence
    (comp
      (filter #(Character/isLetter ^Character %))
      (map #(Character/toLowerCase ^Character %)))
    text))

(facts "about normalize"
  (normalize "Was it a car\nor a cat\nI saw?")
  => (seq "wasitacaroracatisaw")
  (normalize2 "Was it a car\nor a cat\nI saw?")
  => (seq "wasitacaroracatisaw")
  )

(defn palindrome? [text]
  (let [normalized-text (normalize text)]
    (= normalized-text (reverse normalized-text))))

(defn palindrome2? [text]
  (let [normalized-text (normalize2 text)]
    (= normalized-text (reverse normalized-text))))

(facts
  "about palindrome?"
  (palindrome? "Are we not drawn onward, \nwe few, drawn onward to new area?")
  => false
  (palindrome? "А роза упала на лапу Азора.")
  => true
  (palindrome? "A man, a plan, \na canal, a hedgehog, \na podiatrist, \nPanama!")
  => false
  (palindrome? "Was it a car\nor a cat\nI saw?")
  => true)
