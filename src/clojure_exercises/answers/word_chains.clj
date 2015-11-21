(ns clojure-exercises.answers.word-chains
  (:require [clojure.set :as set]
            [midje.sweet :refer :all]))

(defn one-replaced? [w1 w2]
  (and (= (count w1) (count w2))
       (= 1 (->> (map = w1 w2)
                 (remove true?)
                 count))))

(defn one-added? [word1 word2]
  (let [len-diff (- (count word1) (count word2))]
    (if-not (or (= len-diff 1)
                (= len-diff -1))
      false
      (let [[longer-word shorter-word] (if (pos? len-diff)
                                         [word1 word2]
                                         [word2 word1])]
        (loop [[h1 & t1] longer-word
               [h2 & t2 :as sw] shorter-word
               skips-left 1]
          (if-not (and h1 h2)
            true
            (if-not (= h1 h2)
              (if (zero? skips-left)
                false
                (recur t1 sw (dec skips-left)))
              (recur t1 t2 skips-left))))))))

(defn connectable? [w1 w2]
  (or (= w1 w2)
      (one-replaced? w1 w2)
      (one-added? w1 w2)))

(defn build-chain [chain words]
  (if (empty? words)
    chain
    (let [w (last chain)
          candidates (filter #(connectable? w %) words)]
      (if (empty? candidates)
        nil
        (some seq (for [c candidates]
                    (build-chain (conj chain c) (remove #{c} words))))))))

(defn chainable? [words]
  (boolean
    (some seq (for [w words]
                (build-chain [w] (remove #{w} words))))))

;; Implementation using sets is 8-9% faster
(defn build-chain2 [chain word-set]
  (if (empty? word-set)
    chain
    (let [w (last chain)
          candidates (set/select #(connectable? w %) word-set)]
      (if (empty? candidates)
        nil
        (some seq (for [c candidates]
                    (build-chain2 (conj chain c) (disj word-set c))))))))

(defn chainable?2 [words]
  (let [word-set (set words)]
    (boolean
      (some seq (for [w word-set]
                  (build-chain2 [w] (disj word-set w)))))))

;; TESTS

(facts "about one-replaced?"
  (one-replaced? "abc" "") => false
  (one-replaced? "abc" "ab") => false
  (one-replaced? "aba" "aaa") => true
  (one-replaced? "aaa" "aba") => true
  (one-replaced? "abc" "abd") => true
  (one-replaced? "abd" "abc") => true
  (one-replaced? "abc" "acb") => false)

(facts "about one-added?"
  (one-added? "" "") => false
  (one-added? "" "a") => true
  (one-added? "a" "") => true
  (one-added? "a" "a") => false
  (one-added? "a" "b") => false
  (one-added? "aaaa" "aaaab") => true
  (one-added? "aaaab" "aaaa") => true
  (one-added? "baaaa" "aaaa") => true
  (one-added? "aaaa" "baaaa") => true
  (one-added? "aaaa" "aabaa") => true
  (one-added? "aabaa" "aaaa") => true)

(facts "about connectable?"
  (connectable? "aaa" "aaa") => true
  (connectable? "aaa" "abaa") => true
  (connectable? "aaa" "aba") => true
  (connectable? "aaa" "aaaaa") => false
  (connectable? "aaa" "bab") => false)

(facts "about build-chain"
  (build-chain ["cat"] #{"cat1" "cat12"}) => ["cat" "cat1" "cat12"]
  (build-chain ["cat"] #{"cat1" "cat2" "cat23"}) => ["cat" "cat1" "cat2" "cat23"]
  (build-chain ["cat"] #{"cat1" "cat12" "cat3" "cat43"}) => nil)

(facts "about chainable?"
  (chainable? #{"hat" "coat" "dog" "cat" "oat" "cot" "hot" "hog"}) => true
  (chainable? #{"spout" "do" "pot" "pout" "spot" "dot"}) => true
  (chainable? #{"share" "hares" "shares" "hare" "are"}) => true
  (chainable? #{"cot" "hot" "bat" "fat"}) => false
  (chainable? #{"to" "top" "stop" "tops" "toss"}) => false
  (chainable? #{"share" "hares" "hare" "are"}) => false)

(facts "about build-chain2"
  (build-chain2 ["cat"] #{"cat1" "cat12"}) => ["cat" "cat1" "cat12"]
  (build-chain2 ["cat"] #{"cat1" "cat2" "cat23"}) => ["cat" "cat1" "cat2" "cat23"]
  (build-chain2 ["cat"] #{"cat1" "cat12" "cat3" "cat43"}) => nil)

(facts "about chainable?2"
  (chainable?2 #{"hat" "coat" "dog" "cat" "oat" "cot" "hot" "hog"}) => true
  (chainable?2 #{"spout" "do" "pot" "pout" "spot" "dot"}) => true
  (chainable?2 #{"share" "hares" "shares" "hare" "are"}) => true
  (chainable?2 #{"cot" "hot" "bat" "fat"}) => false
  (chainable?2 #{"to" "top" "stop" "tops" "toss"}) => false
  (chainable?2 #{"share" "hares" "hare" "are"}) => false)