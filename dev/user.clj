(ns user
  (:require [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [clojure.test :as test]
            [schema.core :as s]
            [midje.repl :as midje]
            [criterium.core :as crit]
            [automat.core :as a]
            [automat.viz :refer (view)]))

(defn tests []
  (s/with-fn-validation
    (midje/load-facts 'clojure-exercises.*)))

;(defn run-benchmark []
;  (crit/bench (doublets "wheat" "bread")))
;
;(defn benchmark []
;  (refresh :after 'user/run-benchmark))
