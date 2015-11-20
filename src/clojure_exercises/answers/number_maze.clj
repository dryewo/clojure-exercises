(ns clojure-exercises.answers.number-maze)

(def OPS [#(if (even? %) (/ % 2) nil)
          #(* % 2)
          #(+ % 2)])

(defn step [ops path]
  (let [n (last path)]
    (->> ops
         (map #(% n))
         (remove nil?)
         (distinct)
         (remove (set path))
         (map #(conj path %)))))

(defn solve [start end]
  (loop [paths [[start]]]
    (if-let [res (some #(when (= end (last %)) %) paths)]
      res
      (recur (mapcat #(step OPS %) paths)))))
