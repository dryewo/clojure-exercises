(ns clojure-exercises.send-more-money
  (:require [midje.sweet :refer :all]
            [loco.core :refer [solutions solution]]
            [loco.constraints :refer :all]
            [instaparse.core :as insta]
            [instaparse.transform :as instatr]))

;; Specific solution

(defn solve1 []
  )

(future-facts "Should return one solution to SEND+MORE=MONEY"
              (solve1) => "9567+1085=10652")

;; Generic solution

(defn solve2 [text]
  )

(future-facts "Should parse the problem and return all the solutions"
              (solve2 "SEND+MORE=MONEY") => ["9567+1085=10652"]
              (solve2 "MORE+SEND=MONEY") => ["1085+9567=10652"]
              (solve2 "SEND=MONEY-MORE") => ["9567=10652-1085"])
