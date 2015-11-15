(ns clojure-exercises.answers.game-of-threes)

(defn game-of-threes [n]
  (loop [i n
         acc []]
    (if (<= i 1)
      acc
      (case (rem i 3)
        0 (recur (/ i 3) (conj acc [i 0]))
        1 (recur (dec i) (conj acc [i -1]))
        2 (recur (inc i) (conj acc [i 1]))))))

(defn unfold [z f]
  (lazy-seq
    (when-let [[x s] (f z)]
      (cons x (unfold s f)))))

(defn game-of-threes2 [n]
  (unfold n #(when-not (<= % 1)
              (case (rem % 3)
                0 [[% 0] (/ % 3)]
                1 [[% -1] (dec %)]
                2 [[% 1] (inc %)]))))
