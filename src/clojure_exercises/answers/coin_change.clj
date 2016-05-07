(ns clojure-exercises.answers.coin-change
  (:require [midje.sweet :refer :all]
            [loco.core :refer [solutions solution]]
            [loco.constraints :refer :all]))

;; https://en.wikipedia.org/wiki/Change-making_problem

(defn make-change [denominations n]
  {:pre [(>= n 0)]}
  (if (zero? n)
    {}
    (if (empty? denominations)
      nil
      (let [denoms (set denominations)
            vars   (for [d denoms] [:x d])
            sol    (solution (concat
                               (for [v vars] ($in v 0 n))
                               [($= n ($scalar vars denoms))])
                             :minimize (apply $+ vars))]
        (#(when (seq %) %)
          (into (sorted-map) (for [[[_ d] c] sol
                                   :when (pos? c)]
                               [d c])))))))

(facts "about make-change"
  (make-change [] 0) => {}
  (make-change [] 42) => nil
  (make-change [2 10] 13) => nil
  (make-change [1 20 25] 80) => {20 4}
  (make-change [1 5 10 25] 18) => {1 3 5 1 10 1})

(comment
  (time (make-change [1 10 100 1000] 5432))
  (sort (set [1]))
  (for [d [1]] [:x d])
  (for [v [[:x 1]]] ($in v 0 42))
  (solution [($in :1 0 15)
             ($in :5 0 10)
             ($= 25 ($scalar [:1 :5] [1 5]))]
            :minimize ($+ :1 :5))
  (solutions [($in :x 1 6)
              ($in :y 3 7)
              ($= ($+ :x :y) 12)]))

