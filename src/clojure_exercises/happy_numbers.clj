(ns clojure-exercises.happy-numbers
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Happy_number

;; 13
;; 1*1 + 3*3 = 10
;; 1*1 + 0*0 = 1

;; 6
;; 36
;; 9 + 36 = 45
;; 16 + 25 = 41
;; 16 + 1 = 17
;;

;; 44
;; 16 + 16 = 32
;; 9 + 4 = 13
;; 1 + 9 = 10
;; 1

;; 43
;; 16 + 9 = 25
;; 4 + 25 = 29
;; 4 + 81 = 85
;; 64 + 25 = 89
;; 64 + 81 = 155
;; 1 + 25 + 25 = 51
;; 25 + 1 = 26
;; 4 + 36 = 40
;; 16
;; 1 + 36 = 37
;; 9 + 49 = 58
;; 25 + 64 = 89

(defn sqr [n]
  (* n n))

(defn number->digits1 [n]
  (->> n
       str
       (map str)
       (map #(Integer/valueOf ^String %))))

(defn number->digits2 [n]
  (loop [n n
         acc []]
    (if (zero? n)
      (rseq acc)
      (let [q (quot n 10)
            r (rem n 10)]
        (recur q (conj acc r))))))

(facts
  (number->digits1 43) => [4 3])

(facts
  (number->digits2 43) => [4 3])

(defn happy-step [n]
  (->> n
       number->digits1
       (map sqr)
       (apply +)))

(facts "about happy-step"
  (happy-step 0) => 0
  (happy-step 1) => 1
  (happy-step 45) => 41)

(defn happy? [n]
  (loop [n n
         acc #{}]
    (cond
      (= n 1) true
      (acc n) false
      :else (recur (happy-step n) (conj acc n)))))

(facts "about happy?"
  (happy? 1) => true)

(facts "about happy?"
  (filter happy? (range 100))
  => [1 7 10 13 19 23 28 31 32 44 49 68 70 79 82 86 91 94 97])
