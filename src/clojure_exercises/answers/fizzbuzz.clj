(ns clojure-exercises.answers.fizzbuzz
  (:require [midje.sweet :refer :all]))

(defn factor-names [i factors-spec]
  (->> factors-spec
       (filter (fn [[k _]] (zero? (rem i k))))
       (map second)
       seq))

(defn fizzbuzz [n factors-spec]
  (for [i (range 1 (inc n))]
    (let [fnames (factor-names i factors-spec)]
      [i (apply str fnames)])))

(def FIZZBUZZ_SPEC [[3 "fizz"] [5 "buzz"]])

(facts "about factor-names"
  (factor-names 7 FIZZBUZZ_SPEC) => nil
  (factor-names 10 FIZZBUZZ_SPEC) => ["buzz"]
  (factor-names 15 FIZZBUZZ_SPEC) => ["fizz" "buzz"])

(facts "about fizzbuzz"
  (fizzbuzz 15 FIZZBUZZ_SPEC)
  => '([1 ""] [2 ""] [3 "fizz"] [4 ""] [5 "buzz"]
        [6 "fizz"] [7 ""] [8 ""] [9 "fizz"] [10 "buzz"]
        [11 ""] [12 "fizz"] [13 ""] [14 ""] [15 "fizzbuzz"]))

