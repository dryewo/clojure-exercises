(ns clojure-exercises.answers.word-matrix
  (:require [midje.sweet :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure-exercises.answers.utils :refer [spy]]
            [criterium.core :as crit])
  (:import (clojure.lang PersistentQueue)
           (java.util HashMap)))

;; Inspired by:
;; http://img.artlebedev.ru/kovodstvo/idioteka/i/3195C27E-DC06-40E4-9753-C0F7E58377BE.jpg

;; Approaches to solution:
;; 1. Crawl the matrix, try to check in the dictionary (need to index the dictionary)
;; 2. Enumerate words, try to match (need to index the matrix)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Matrix API

(defn neighbours-4 [[x y]]
  [[(dec x) y]
   [(inc x) y]
   [x (dec y)]
   [x (inc y)]])

(defn neighbours-8 [[x y]]
  (for [dx [-1 0 1]
        dy [-1 0 1]
        :when (not= 0 dx dy)]
    [(+ x dx) (+ y dy)]))

(defn fits? [[cx cy] [x y]]
  (and (< -1 x cx)
       (< -1 y cy)))

(defn neighbours [cxcy xy]
  (filter (partial fits? cxcy) (neighbours-4 xy)))

(defn matrix-get [matrix [x y]]
  (-> matrix (nth x) (nth y)))

(defn matrix-size [matrix]
  [(count matrix)
   (apply min (map count matrix))])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Processing functions

;; fastest
(defn get-prefixes [s]
  (reduce (fn [acc x]
            (conj acc (str (peek acc) x)))
          [] s))

;; slightly slower
(defn get-prefixes2 [s]
  (reduce (fn [acc x]
            (conj acc (conj (or (peek acc) []) x)))
          [] s))

;; very slow
(defn get-prefixes3 [s]
  (for [i (range (count s))]
    (take (inc i) s)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dictionary index protocol

(defprotocol DictIndex
  (lookup [_ s] "returns nil, :prefix or :entry"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Naive dictionary index implementations

;; Clojure sets implementation, medium index build, slow lookup

(defrecord NaiveDictIndex [entries prefixes]
  DictIndex
  (lookup [_ s]
    (cond
      (contains? entries s) :entry
      (contains? prefixes s) :prefix)))

(defn make-naive-dict-index [words]
  (let [entries  (into #{} words)
        prefixes (into #{} (mapcat get-prefixes words))]
    (->NaiveDictIndex entries prefixes)))

;; Clojure map implementation, slow index build, fast lookup

(defrecord NaiveDictIndex2 [entries]
  DictIndex
  (lookup [_ s]
    (get entries s)))

(defn make-naive-dict-index2 [words]
  (let [entries (-> {}
                    (into (for [p (mapcat get-prefixes words)]
                            [p :prefix]))
                    (into (for [w words]
                            [w :entry])))]
    (->NaiveDictIndex2 entries)))

;; java.util.HashMap based implementation, the fastest build, the fastest lookup

(defrecord NaiveDictIndex3 [entries]
  DictIndex
  (lookup [_ s]
    (get entries s)))

(defn make-naive-dict-index3 [words]
  (let [hmap (HashMap.)]
    (doseq [p (mapcat get-prefixes words)]
      (.put hmap p :prefix))
    (doseq [w words]
      (.put hmap w :entry))
    (->NaiveDictIndex3 hmap)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Trie-based dictionary index (not finished)

;(defrecord TrieDictIndex [entries prefixes]
;  DictIndex
;  (lookup [_ s]
;    (cond
;      (contains? entries s) :entry
;      (nil? s) false
;      (not (false? (get-in prefixes (seq s) false))) :prefix)))
;
;(defn make-trie-dict-index [words]
;  (let [entries  (into #{} words)
;        prefixes (reduce (fn [acc w]
;                           (update-in acc (seq w) identity))
;                         {} words)]
;    (->TrieDictIndex entries prefixes)))
;
;(facts "about make-trie-dict-index"
;  (make-trie-dict-index ["foo"]) => {:entries  #{"foo"}
;                                     :prefixes {\f {\o {\o nil}}}}
;  (make-trie-dict-index ["foo" "bar"]) => {:entries  #{"foo" "bar"}
;                                           :prefixes {\f {\o {\o nil}}
;                                                      \b {\a {\r nil}}}}
;  (make-trie-dict-index ["bad" "bare"]) => {:entries  #{"bad" "bare"}
;                                            :prefixes {\b {\a {\d nil
;                                                               \r {\e nil}}}}})

(comment

  (def dict (load-dict))
  (def lookup-test-data (->> dict
                             shuffle
                             (sequence (comp
                                         (take 100000)
                                         (map butlast)
                                         (map (partial apply str))))))
  (time (count (into #{} dict)))
  (crit/quick-bench (doall (get-prefixes "12345678901234567890")))
  (time (count (into #{} (mapcat get-prefixes (take 100000 dict)))))
  (time (count (into #{} (mapcat get-prefixes2 (take 100000 dict)))))
  (time (count (into #{} (mapcat get-prefixes3 (take 100000 dict)))))
  (time (count (make-naive-dict-index dict)))
  (let [index (time (make-naive-dict-index dict))]
    (time (doseq [w lookup-test-data]
            (lookup index w))))
  (let [index (time (make-naive-dict-index2 dict))]
    (time (doseq [w lookup-test-data]
            (lookup index w))))
  (let [index (time (make-naive-dict-index3 dict))]
    (time (doseq [w lookup-test-data]
            (lookup index w))))

  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Search implementation

(defn get-initial-work [matrix]
  (let [[cx cy] (matrix-size matrix)]
    (for [x (range cx)
          y (range cy)]
      {:xy      [x y]                                       ; new tile to process
       :visited #{}                                         ; previously visited tiles
       :path    []})))                                      ; previously accumulated path

(defn search [dict-index matrix]
  (let [cxcy         (matrix-size matrix)
        initial-work (get-initial-work matrix)]
    (loop [work-queue (into PersistentQueue/EMPTY initial-work)
           results    []]
      ;(Thread/sleep 100)
      (if (empty? work-queue)
        (map (partial apply str) results)                   ; convert char seqs back to strings
        (let [{:keys [xy visited path]} (peek work-queue)
              cur-val        (matrix-get matrix xy)
              new-path       (conj path cur-val)
              new-visited    (conj visited xy)
              dict-path-type (lookup dict-index (apply str new-path))
              nbrs-xys       (when dict-path-type
                               (neighbours cxcy xy))
              new-work       (for [nbr-xy nbrs-xys
                                   :when (not (visited nbr-xy))]
                               {:xy      nbr-xy
                                :visited new-visited
                                :path    new-path})
              new-results    (if (= :entry dict-path-type)
                               (conj results new-path)
                               results)]
          (recur (-> work-queue pop (into new-work))
                 new-results))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Utility functions

(defn load-dict
  ([]
   (load-dict 3))
  ([n]
   (->> "/usr/share/dict/words"
        io/reader
        line-seq
        (filter #(< n (count %)))
        ;(take 10)
        (map str/upper-case))))

(def alphabet (into [] (map char (range (int \A) (int \Z)))))

(defn generate-matrix
  ([n]
   (generate-matrix n n))
  ([cx cy]
   (repeatedly cx (partial repeatedly cy (partial rand-nth alphabet)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TESTS

(comment

  ;; For REPL fiddling
  ;; Load only words of 6 and longer
  (def dict-index (make-naive-dict-index3 (load-dict 6)))
  ;; does 100x100 in about 1.3s
  (let [matrix  (generate-matrix 100)
        results (time (search dict-index matrix))]
    (println (str/join "\n" (map (partial apply str) matrix)))
    (println results))

  )

(facts "about get-prefixes"
  (get-prefixes "123") => ["1" "12" "123"]
  (get-prefixes2 "123") => (map seq ["1" "12" "123"])
  (get-prefixes3 "123") => (map seq ["1" "12" "123"]))

(facts "about dict-index"
  (doseq [index [(make-naive-dict-index ["foo" "fool"])
                 (make-naive-dict-index2 ["foo" "fool"])
                 (make-naive-dict-index3 ["foo" "fool"])]]
    ;(println index)
    (lookup index "f") => :prefix
    (lookup index "fo") => :prefix
    (lookup index "foo") => :entry
    (lookup index "fool") => :entry
    (lookup index nil) => falsey
    (lookup index "o") => falsey))

(facts "about neighbours"
  (neighbours-4 [0 0]) => [[-1 0] [1 0] [0 -1] [0 1]]
  (neighbours-8 [0 0]) => [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]]
  (fits? [3 3] [-1 0]) => false
  (fits? [3 3] [0 1]) => true
  (fits? [3 3] [0 3]) => false
  (neighbours [3 3] [0 0]) => (just [[0 1] [1 0]] :in-any-order))

(def TEST_DATA_1 (map vec ["fi"
                           "el"]))

(def TEST_DATA_2 (map vec
                      ["FXIF"
                       "AMLE"
                       "EWBX"
                       "ASTU"]))

(facts "about matrix-get"
  (matrix-get TEST_DATA_2 [0 0]) => \F
  (matrix-get TEST_DATA_2 [2 1]) => \W)

(facts "about matrix-size"
  (matrix-size [[1]]) => [1 1]
  (matrix-size [[1 1]]) => [1 2]
  (matrix-size [[1 1] [2]]) => [2 1])

(facts "about get-initial-work"
  (map :xy (get-initial-work TEST_DATA_1)) => [[0 0] [0 1] [1 0] [1 1]])

(facts "about search"
  (let [dict-index (make-naive-dict-index3 ["life" "file"])]
    (search dict-index TEST_DATA_1) => (just ["life" "file"] :in-any-order)))
