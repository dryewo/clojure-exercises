(ns clojure-exercises.answers.einstein-riddle
  (:require [midje.sweet :refer :all]
            [clojure.math.combinatorics :as c]))

(def nationalities [:norwegian :brit :swede :dane :german])
(def colors [:red :green :white :yellow :blue])
(def beverages [:tea :coffee :milk :beer :water])
(def cigars [:pall-mall :dunhill :blends :blue-master :prince])
(def pets [:dogs :birds :cats :horses :fish])

(defn N [coll val]
  (.indexOf coll val))

(defn next-to [n1 n2]
  (let [d (- n1 n2)]
    (or (= d 1) (= d -1))))

(defn solve []
  (for [
        col (c/permutations colors)
        :when (= 1 (- (N col :white) (N col :green)))
        nat (c/permutations nationalities)
        :when (= 0 (N nat :norwegian))
        :when (= (N nat :brit) (N col :red))
        :when (next-to (N nat :norwegian) (N col :blue))
        bev (c/permutations beverages)
        :when (= 2 (N bev :milk))
        :when (= (N bev :coffee) (N col :green))
        :when (= (N bev :tea) (N nat :dane))
        pet (c/permutations pets)
        :when (= (N pet :dogs) (N nat :swede))
        cig (c/permutations cigars)
        :when (= (N cig :dunhill) (N col :yellow))
        :when (= (N cig :prince) (N nat :german))
        :when (= (N cig :blue-master) (N bev :beer))
        :when (= (N cig :pall-mall) (N pet :birds))
        :when (next-to (N cig :blends) (N bev :water))
        :when (next-to (N cig :blends) (N pet :cats))
        :when (next-to (N cig :dunhill) (N pet :horses))
        ]
    [nat pet col cig bev]))

(comment
  (time (doall (solve)))
  )
