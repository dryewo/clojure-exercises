(ns clojure-exercises.answers.countdown
  (:require [midje.sweet :refer :all]
            [clojure.string :as str]
            [clojure-exercises.answers.utils :refer [spy]]))

;; http://dai.fmph.uniba.sk/courses/FPRO/bird_pearls.pdf
;; http://www.cs.nott.ac.uk/~pszgmh/countdown.pdf
;; http://usi-pl.github.io/lc/sp-2015/doc/Bird_Wadler.%20Introduction%20to%20Functional%20Programming.1ed.pdf

(defn subseqs [[x & xs]]
  (if-not xs
    [[x]]
    (let [xss (subseqs xs)]
      (concat xss (cons [x] (map (partial cons x) xss))))))

;(defn interleave' [x [y & ys]]
;  (if-not y
;    [[x]]
;    (cons (list* x y ys)
;          (map (partial cons y) (interleave' x ys)))))

;(defn perms [[x & xs]]
;  (if-not x
;    [()]
;    (let [pxs (perms xs)]
;      (mapcat (partial interleave' x) pxs))))

;(defn selections [n xs]
;  (if-not (pos? n)
;    [()]
;    (let [sxs (selections (dec n) xs)]
;      (mapcat #(for [x xs] (cons x %)) sxs))))

(defn legal? [op v1 v2]
  (condp = op
    '+ true
    '- (> v1 v2)
    '* true
    '/ (zero? (mod v1 v2))))

(defn legal?' [op v1 v2]
  (condp = op
    '+ (<= v1 v2)
    '- (> v1 v2)
    '* (and (> v1 1) (> v2 v1))
    '/ (and (> 1 v2) (zero? (mod v1 v2)))))

(defn unmerges [[x1 x2 :as xxs]]
  (if (= 2 (count xxs))
    [[[x1] [x2]] [[x2] [x1]]]
    (let [[x & xs] xxs
          add (fn [[ys zs]] [[(cons x ys) zs] [ys (cons x zs)]])]
      (concat [[[x] xs] [xs [x]]]
              (mapcat add (unmerges xs))))))

(defn evaluate [op v1 v2]
  (condp = op
    '+ (+ v1 v2)
    '- (- v1 v2)
    '* (* v1 v2)
    '/ (/ v1 v2)))

(defn combine [[e1 v1] [e2 v2]]
  (for [op '[+ - * /]
        :when (legal? op v1 v2)]
    [(list op e1 e2) (evaluate op v1 v2)]))

(defn expressions [[x & xs :as xxs]]
  (if-not xs
    [[x x]]
    (for [[ys zs] (unmerges xxs)
          ev1 (expressions ys)
          ev2 (expressions zs)
          ev  (combine ev1 ev2)]
      ev)))

(defn dist [x y]
  (Math/abs ^int (- x y)))

(defn nearest [n [c & cx]]
  (reduce (fn [[res mindist] [e v]]
            (let [d (dist n v)]
              (if (< d mindist)
                [[e v] d]
                [res mindist])))
          [c (dist n (second c))]
          cx))

(defn countdown1 [n xs]
  (-> xs
      subseqs
      ((partial mapcat expressions))
      ((partial nearest n))))

;; TESTS

(facts "subseqs"
  (map (partial apply str) (subseqs (seq "123"))) => ["3" "2" "23" "1" "13" "12" "123"]
  (count (subseqs (range 8))) => 255)

;(facts "interleave'"
;  (interleave' 1 [2 2]) => [[1 2 2] [2 1 2] [2 2 1]])

;(facts "perms"
;  (perms [1 2 3]) => '((1 2 3) (2 1 3) (2 3 1) (1 3 2) (3 1 2) (3 2 1))
;  (count (perms (range 5))) => 120)

;(facts "selections"
;  (selections 3 [0 1]) => '((0 0 0) (1 0 0) (0 1 0) (1 1 0) (0 0 1) (1 0 1) (0 1 1) (1 1 1))
;  (count (selections 4 (range 4))) => 256)

(facts "legal?"
  (legal? '+ 2 1) => true
  (legal? '- 2 1) => true
  (legal? '- 1 2) => false
  (legal? '* 1 1) => true
  (legal? '/ 5 2) => false
  (legal? '/ 4 2) => true)

(facts "unmerges"
  (unmerges [1 2]) => [[[1] [2]] [[2] [1]]]
  (unmerges [1 2 3]) => '([[1] (2 3)] [(2 3) [1]] [(1 2) [3]] [[2] (1 3)] [(1 3) [2]] [[3] (1 2)]))

(facts "combine"
  (combine [2 2] [1 1]) => '([(+ 2 1) 3] [(- 2 1) 1] [(* 2 1) 2] [(/ 2 1) 2]))

(facts "expressions"
  (expressions [1 2]) => '([(+ 1 2) 3] [(* 1 2) 2] [(+ 2 1) 3] [(- 2 1) 1] [(* 2 1) 2] [(/ 2 1) 2]))

(facts "nearest"
  (nearest 10 [[:a 8] [:b 13]]) => [[:a 8] 2])

(facts "countdown1"
  (countdown1 10 [5 7 2]) => '[[(* 5 2) 10] 0])

(comment

  (subseqs [1 2 3])
  (crit/quick-bench (count (subseqs (range 18))))
  (time (count (subseqs (range 21))))
  (time (count (perms (range 10))))
  (count (mapcat perms (subseqs (range 6))))
  (unmerges [1 2 3 4])
  ;; ~7 s
  (time (countdown1 831 [1 3 7 10 25 50]))
  ;; ~900 ms
  (time (with-redefs [legal? legal?']
          (countdown1 831 [1 3 7 10 25 50])))

  )
