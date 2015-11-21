(ns clojure-exercises.game-of-threes)

;; https://www.reddit.com/r/dailyprogrammer/comments/3r7wxz

;; 5 * 4 * 3 * 2

(defn factorial [n]
  (if (= n 0)
    1
    (*' n (factorial (dec n)))))

(defn factorial-tr
  ([n]
   (factorial-tr n 1))
  ([n acc]
   (if (= n 0)
     acc
     (recur (dec n) (*' acc n)))))

(defn factorial-loop [n]
  (loop [i n
         acc 1]
    (if (= i 0)
      acc
      (recur (dec i) (*' acc i)))))

;; 102 -> [[102 0] (game-of-threes 34)]
;; 34  -> [[34 -1] (game-of-threes 33)]

;; 102 -> [[102 0] [34 -1] (game-of-threes 33)]

(defn game-of-threes-bad [n]
  (if (= n 1)
    []
    (let [r (rem n 3)]
      (case r
        0 (concat [[n 0]] (game-of-threes-bad (/ n 3)))
        1 (concat [[n -1]] (game-of-threes-bad (dec n)))
        2 (concat [[n 1]] (game-of-threes-bad (inc n)))))))

;; 3, [[12 0] [4 -1]] -> (recur (/ 3 3) (conj acc [3 0]))
;; 1, [[12 0] [4 -1] [3 0]] -> [[12 0] [4 -1] [3 0]]

(defn game-of-threes-tr [n acc]
  (if (= n 1)
    acc
    (let [r (rem n 3)]
      (case r
        0 (recur (/ n 3) (conj acc [n 0]))
        1 (recur (dec n) (conj acc [n -1]))
        2 (recur (inc n) (conj acc [n 1]))))))

(defn game-of-threes [n]
  (loop [i n
         acc []]
    (if (<= i 1)
      acc
      (let [[new-val element]
            (case (rem i 3)
              0 [(/ i 3) [i 0]]
              1 [(dec i) [i -1]]
              2 [(inc i) [i 1]])]
        (recur new-val (conj acc element))))))
