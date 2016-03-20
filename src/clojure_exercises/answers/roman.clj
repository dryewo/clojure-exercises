(ns clojure-exercises.answers.roman
  (:require [midje.sweet :refer :all]
            [automat.core :as a]
            [automat.viz :refer (view)]))

;; Naïve implementation

(def hundreds ["" "C" "CC" "CCC" "CD" "D" "DC" "DCC" "DCCC" "CM"])
(def tens ["" "X" "XX" "XXX" "XL" "L" "LX" "LXX" "LXXX" "XC"])
(def ones ["", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"])

(defn get-thousands [n]
  (apply str (repeat (quot n 1000) "M")))

(defn get-others [n factor digits]
  (let [k (quot n factor)]
    (get digits k)))

(defn make-roman-naive [n]
  (str (get-thousands n)
       (get-others (rem n 1000) 100 hundreds)
       (get-others (rem n 100) 10 tens)
       (get-others (rem n 10) 1 ones)))

;; Generic implementation

(def ROMAN_DIGITS ["I" "V" "X" "L" "C" "D" "M"])

(defn roman-digit [n [d1 d5 d10 :as ds]]
  (case (count ds)
    3 [(quot n 10) (apply str (case (rem n 10)
                                0 []
                                1 [d1]
                                2 [d1 d1]
                                3 [d1 d1 d1]
                                4 [d1 d5]
                                5 [d5]
                                6 [d5 d1]
                                7 [d5 d1 d1]
                                8 [d5 d1 d1 d1]
                                9 [d1 d10]))]
    1 [0 (apply str (repeat n d1))]
    (throw (IllegalArgumentException. "Not enough digits to represent."))))

(defn make-roman-generic [digits n]
  (let [digit-groups (partition-all 3 2 digits)]
    (->> (loop [gs  digit-groups
                n   n
                res nil]
           (if (zero? n)
             res
             (let [[next-n XVI] (roman-digit n (first gs))]
               (recur (next gs) next-n (cons XVI res)))))
         (apply str))))

(def make-roman (partial make-roman-generic ROMAN_DIGITS))

;(defn up-to-n [n c v]
;  (a/interpose-$ v (repeat n (a/? c))))
;
;(defn reducers [v]
;  (fn [state _] (+ state v)))
;
;(def roman-automat
;  (a/compile [(a/or (up-to-n 1 \V 5) (up-to-n 3 \I 1))]
;             {:reducers reducers}))
;
;(defn parse-roman [r]
;  (let [prepared-input (map int r)
;        res            (reduce (partial a/advance roman-automat) 0 prepared-input)]
;    (or (:value res) res)))
;
;(facts "about parse-roman"
;  (tabular
;    (fact (parse-roman ?r) => ?n)
;    ?r ?n
;    "" 0, "II" 2, "V" 5, "VIII" 8)
;  (tabular
;    (fact (parse-roman ?r) => (throws IllegalArgumentException))
;    ?r
;    " " "IIII" "VV" "VIV"))
;
;(comment
;
;  (a/find roman-automat nil (map int "II"))
;  ((eval (list 'add 1)) 10 nil)
;  ((reducers (list 'add 5)) 4 nil)
;  (view roman-automat)
;  (view (up-to-n 3 9 5))
;  (reduce (partial a/advance roman-automat) :aaa [73])
;
;  )

;; TESTS

(facts "about make-roman-naive"
  (make-roman-naive 1) => "I"
  (make-roman-naive 2) => "II"
  (make-roman-naive 4) => "IV"
  (make-roman-naive 5) => "V"
  (make-roman-naive 6) => "VI"
  (make-roman-naive 1952) => "MCMLII"
  (make-roman-naive 2345) => "MMCCCXLV")

(facts "about roman-digit"
  (roman-digit 4 ["A"]) => [0 "AAAA"]
  (roman-digit 4 ["B" "C"]) => (throws IllegalArgumentException)
  (roman-digit 30 ["A" "B" "C"]) => [3 ""]
  (tabular
    (fact ""
      (roman-digit ?n ["I" "V" "X"]) => [0 ?r])
    ?n ?r
    0 ""
    1 "I"
    2 "II"
    3 "III"
    4 "IV"
    5 "V"
    6 "VI"
    7 "VII"
    8 "VIII"
    9 "IX"))

(facts "about make-roman"
  (tabular
    (fact (make-roman ?n) => ?r)
    ?n ?r
    0 ""
    1 "I"
    2 "II"
    3 "III"
    1952 "MCMLII"
    2345 "MMCCCXLV"))
