(ns clojure-exercises.answers.montyhall
  (:require [midje.sweet :refer :all]))

(defn setup-game [door-count]
  {:prize-door   (rand-int door-count)
   :closed-doors (range door-count)})

(facts "about setup-game"
  (fact "works"
    (setup-game 3) => {:prize-door ..rnd.. :closed-doors [0 1 2]}
    (provided
      (rand-int 3) => ..rnd..)))

(defn choose-door [{:keys [closed-doors]}]
  (rand-nth (vec closed-doors)))

(defn open-non-winning-door [{:keys [closed-doors prize-door] :as game} chosen-door]
  (let [doors-not-to-open (set [chosen-door prize-door])
        door-to-open      (rand-nth (vec (remove doors-not-to-open closed-doors)))]
    (update game :closed-doors #(vec (remove #{door-to-open} %)))))

(facts "about open-non-winning-door"
  (tabular
    (fact "works"
      (open-non-winning-door {:prize-door ?prize :closed-doors [0 1 2]} ?chosen-door)
      => {:prize-door ?prize :closed-doors ?remaining-closed-doors}
      (provided
        (rand-nth ?doors-to-open) => ?door-to-open))
    ?prize ?chosen-door ?doors-to-open ?door-to-open ?remaining-closed-doors
    0 0 [1 2] 1 [0 2]
    0 0 [1 2] 2 [0 1]
    0 1 [2] 2 [0 1]
    0 2 [1] 1 [0 2]
    1 0 [2] 2 [0 1]
    1 1 [0 2] 0 [1 2]
    1 1 [0 2] 2 [0 1]
    1 2 [0] 0 [1 2]
    2 0 [1] 1 [0 2]
    2 1 [0] 0 [1 2]
    2 2 [0 1] 0 [1 2]
    2 2 [0 1] 1 [0 2]))

(defn choose-another-door [{:keys [closed-doors]} chosen-door]
  (let [doors-to-choose-from (vec (remove #{chosen-door} closed-doors))]
    (rand-nth doors-to-choose-from)))

(facts "about choose-another-door"
  (tabular
    (fact "works"
      (choose-another-door {:closed-doors ?closed-doors} ?initial-guess) => ?result)
    ?closed-doors ?initial-guess ?result
    [0 1] 0 1
    [0 1] 1 0
    [0 2] 0 2
    [0 2] 2 0
    [1 2] 1 2
    [1 2] 2 1))

(defn execute [game & {:keys [change-mind]}]
  (let [initial-guess (choose-door game)
        game          (open-non-winning-door game initial-guess)
        final-guess   (if change-mind
                        (choose-another-door game initial-guess)
                        initial-guess)]
    final-guess))

(facts "about execute"
  (fact "when mind to be changed, choose-another-door is called"
    (execute ..game.. :change-mind true) => ..final-guess..
    (provided
      (rand-nth anything) => anything :times 0
      (choose-door ..game..) => ..initial-guess..
      (open-non-winning-door ..game.. ..initial-guess..) => ..game-opened-door..
      (choose-another-door ..game-opened-door.. ..initial-guess..) => ..final-guess..))
  (fact "when mind is not changed, choose-another-door is not called"
    (execute ..game.. :change-mind false) => ..initial-guess..
    (provided
      (rand-nth anything) => anything :times 0
      (choose-door ..game..) => ..initial-guess..
      (open-non-winning-door ..game.. ..initial-guess..) => anything
      (choose-another-door anything anything) => anything :times 0)))

(defn check-game [game guess]
  (= guess (:prize-door game)))

(defn experiment [& {:keys [change-mind times doors]}]
  (repeatedly times (fn []
                      (let [game        (setup-game doors)
                            final-guess (execute game :change-mind change-mind)]
                        (check-game game final-guess)))))

(facts "about experiment"
  (fact "works"
    (experiment :change-mind ..change-mind.. :times 100 :doors ..doors..) => (n-of ..result.. 100)
    (provided
      (setup-game ..doors..) => ..game.. :times 100
      (execute ..game.. :change-mind ..change-mind..) => ..final-guess.. :times 100
      (check-game ..game.. ..final-guess..) => ..result..)))

(defn analyze
  "Takes a list of booleans, prints percentage of true"
  [results]
  (let [wins  (count (filter true? results))
        ratio (double (/ wins (count results)))]
    (println ratio "wins")))

(comment
  ;; 1/3
  (analyze (experiment :change-mind false :times 10000 :doors 3))
  ;; 2/3
  (analyze (experiment :change-mind true :times 10000 :doors 3))
  )
