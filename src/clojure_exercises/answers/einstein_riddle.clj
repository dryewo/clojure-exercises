(ns clojure-exercises.answers.einstein-riddle
  (:require [clojure.math.combinatorics :as c]
            [loco.core :refer [solutions solution]]
            [loco.constraints :refer :all])
  (:import (java.util List)))

(def nationalities [:norwegian :brit :swede :dane :german])
(def colors [:red :green :white :yellow :blue])
(def beverages [:tea :coffee :milk :beer :water])
(def cigars [:pall-mall :dunhill :blends :blue-master :prince])
(def pets [:dogs :birds :cats :horses :fish])

(defn N [^List coll val]
  (.indexOf coll val))

(defn next-to [n1 n2]
  (let [d (- n1 n2)]
    (or (= d 1) (= d -1))))

(defn same [coll1 val1 coll2 val2]
  (= val2 (nth coll2 (N coll1 val1))))

(defn solve []
  (let [[sol] (for [
                    col (c/permutations colors)
                    :when (= 1 (- (N col :white) (N col :green)))
                    nat (c/permutations nationalities)
                    :when (= 0 (N nat :norwegian))
                    :when (same nat :brit col :red)
                    :when (next-to (N nat :norwegian) (N col :blue))
                    bev (c/permutations beverages)
                    :when (= 2 (N bev :milk))
                    :when (same bev :coffee col :green)
                    :when (same bev :tea nat :dane)
                    pet (c/permutations pets)
                    :when (same pet :dogs nat :swede)
                    cig (c/permutations cigars)
                    :when (same cig :dunhill col :yellow)
                    :when (same cig :prince nat :german)
                    :when (same cig :blue-master bev :beer)
                    :when (same cig :pall-mall pet :birds)
                    :when (next-to (N cig :blends) (N bev :water))
                    :when (next-to (N cig :blends) (N pet :cats))
                    :when (next-to (N cig :dunhill) (N pet :horses))
                    ]
                [nat pet col cig bev])]
    (apply map vector sol)))

(defn benchmark []
  (dotimes [_ 10]
    (time (doall (solve)))))

(comment
  (time (doall (solve))))

(defn solve2 []
  (let [sol (solution (concat
                        (for [v (concat nationalities beverages pets cigars colors)]
                          ($in v 1 5))
                        [($distinct nationalities)
                         ($distinct beverages)
                         ($distinct pets)
                         ($distinct cigars)
                         ($distinct colors)
                         ($= :brit :red)
                         ($= :swede :dogs)
                         ($= :dane :tea)
                         ($= 1 ($- :white :green))
                         ($= :green :coffee)
                         ($= :pall-mall :birds)
                         ($= :yellow :dunhill)
                         ($= 3 :milk)
                         ($= 1 :norwegian)
                         ($= 1 ($abs ($- :blends :cats)))
                         ($= 1 ($abs ($- :horses :dunhill)))
                         ($= :blue-master :beer)
                         ($= :german :prince)
                         ($= 1 ($abs ($- :norwegian :blue)))
                         ($= 1 ($abs ($- :blends :water)))]))]
    (for [[_ xs] (into (sorted-map) (group-by second sol))]
      (map first xs))))

(comment
  (time (solve2)))
