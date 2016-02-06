(ns clojure-exercises.fizzbuzz
  (:require [midje.sweet :refer :all]))

;; https://en.wikipedia.org/wiki/Fizz_buzz

(defn fizzbuzz [n factors-spec]
  )

(future-facts "about fizzbuzz"
              (fizzbuzz 15 [[3 "fizz"] [5 "buzz"]])
              => '([1 ""] [2 ""] [3 "fizz"] [4 ""] [5 "buzz"]
                    [6 "fizz"] [7 ""] [8 ""] [9 "fizz"] [10 "buzz"]
                    [11 ""] [12 "fizz"] [13 ""] [14 ""] [15 "fizzbuzz"]))
