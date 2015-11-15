(ns user
  (:require [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [clojure.test :as test]
            [schema.core :as s]
            [midje.repl :as midje]
            [criterium.core :as crit]))

(defn run-tests []
  (s/with-fn-validation
    (midje/load-facts 'clojure-exercises.*)
    #_(test/run-all-tests #"clojure-exercises\..*")))

(defn tests []
  (refresh :after 'user/run-tests))
;
;(defn run-benchmark []
;  (crit/bench (doublets "wheat" "bread")))
;
;(defn benchmark []
;  (refresh :after 'user/run-benchmark))
