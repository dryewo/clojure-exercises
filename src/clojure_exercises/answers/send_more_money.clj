(ns clojure-exercises.answers.send-more-money
  (:require [midje.sweet :refer :all]
            [loco.core :refer [solutions solution]]
            [loco.constraints :refer :all]
            [instaparse.core :as insta]
            [instaparse.transform :as instatr]))

(defn format-solution [text sol]
  (apply str (for [t text]
               (if-let [n (->> t str keyword (get sol))]
                 n
                 t))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Specific solution

(defn send-more-money []
  (solution (concat
              (for [v [:E :N :D :O :R :Y]]
                ($in v 0 9))
              (for [v [:S :M]]
                ($in v 1 9))
              [($distinct [:S :E :N :D :M :O :R :Y])
               ($= ($+ ($scalar [:S :E :N :D] [1000 100 10 1])
                       ($scalar [:M :O :R :E] [1000 100 10 1]))
                   ($scalar [:M :O :N :E :Y] [10000 1000 100 10 1]))])))

(defn solve1 []
  (format-solution "SEND+MORE=MONEY" (send-more-money)))

(facts ""
  (solve1) => "9567+1085=10652")

(comment
  (Character/toLowerCase \v)
  (->> \v (#(Character/toLowerCase ^Character %)) str keyword)
  (set (map (comp keyword str) "sendmoremoney"))

  (for [sol (send-more-money)]
    (format-solution "SEND+MORE=MONEY" sol)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Generic solution

(def parser (insta/parser "
Equals = Expr <'='> Expr
<Expr> = Sum | Diff | Node
Sum = Node <'+'> Node
Diff = Node <'-'> Node
Node = #'\\w+'
"))

(defn ->kw-vec [text]
  (mapv (comp keyword str) text))

(defn parse-tree [text]
  (let [raw-tree (parser text)]
    (when (insta/failure? raw-tree)
      (throw (ex-info "Error parsing" {:text text :result raw-tree})))
    (instatr/transform {:Node (fn [node] [:Node (->kw-vec node)])} raw-tree)))

(defn branch? [x]
  (and (sequential? x) (not= :Node (first x))))

(defn nodes [tree]
  (map second (remove branch? (tree-seq branch? next tree))))

(comment
  (tree-seq branch? next (parse-tree "AA+BB=CC"))
  (instatr/transform {:Equals #(vector %1 %2)
                      :Sum    #(vector %1 %2)
                      :Diff   #(vector %1 %2)
                      :Node   identity}
                     (parse-tree "AA+BB=CC"))
  )

(defn var-domains [var-vecs]
  (let [first-chars (set (map first var-vecs))
        all-chars   (set (apply concat var-vecs))]
    (concat (for [v first-chars]
              ($in v 1 9))
            (for [v (clojure.set/difference all-chars first-chars)]
              ($in v 0 9)))))

(defn expr-constraint [tree]
  (instatr/transform
    {:Equals #($= %1 %2)
     :Sum    #($+ %1 %2)
     :Diff   #($- %1 %2)
     :Node   (fn [vs]
               ($scalar (reverse vs)
                        (take (count vs) (iterate #(* 10 %) 1))))}
    tree))

(defn send-more-money-spec [text]
  (let [expr-tree (parse-tree text)
        var-vecs  (nodes expr-tree)
        domains   (var-domains var-vecs)
        all-vars  (set (apply concat var-vecs))
        constr    (expr-constraint expr-tree)]
    (concat domains
            [($distinct all-vars)]
            [constr])))

(defn solve2 [text]
  (for [sol (solutions (send-more-money-spec text))]
    (format-solution text sol)))

;; TESTS

(facts "about to-kw-vec"
  (->kw-vec "Ab") => [:A :b])

(facts "about parse-tree"
  (parse-tree "A+B=C") => [:Equals [:Sum [:Node [:A]] [:Node [:B]]] [:Node [:C]]])

(facts "about nodes"
  (nodes (parse-tree "AA+BB=CC")) => [[:A :A] [:B :B] [:C :C]])

(facts "about var-domains"
  (var-domains [[:S :M :S]]) => [($in :S 1 9) ($in :M 0 9)])

(facts "about solve-and-format"
  (solve2 "SEND+MORE=MONEY") => ["9567+1085=10652"]
  (solve2 "SEND=MONEY-MORE") => ["9567=10652-1085"])

(comment
  (solutions (send-more-money-spec "SEND+MORE=MONEY"))
  (solve2 "SEND+MOST=MONEY")
  (time (count (solution (send-more-money-spec "SEND+MORE=MONEY"))))
  (let [spec (send-more-money-spec "SEND+MORE=MONEY")]
    (time (count (solution spec))))
  (time (count (solution (send-more-money-spec "AAA+BBB=CCDD"))))
  (solve2 "AAA+BBB=CCDD")
  ($in :S 0 1)
  (var-domains ["SMS"])
  (re-seq #"w" "a")
  (instatr/transform {:Assertion (fn [l r]
                                   ($= l r))
                      :Sum       (fn [a b] ($+ a b))
                      :Node      (fn [vs] ($scalar (reverse vs) (take (count vs) (iterate #(* 10 %) 1))))}
                     (parse-tree "SEND+MORE=MONEY"))
  (solution [($in :a 0 9)
             ($in :b 0 9)
             ($= 15 ($scalar [:b :a] (iterate #(* 10 %) 1)))])
  (instatr/transform {:Node ->kw-vec} (parser "SEND+MORE=MONEY"))
  (remove sequential? (tree-seq sequential? next [:Assertion [:Sum "SEND" "MORE"] "MONEY"])))
