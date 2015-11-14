(ns clojure-exercises.frequencies-test
  (:require [clojure.test :refer :all]
            [clojure-exercises.answers.frequencies-test :refer [test-frequencies]]
            [clojure-exercises.frequencies :refer :all]))

(deftest can-frequencies
  (test-frequencies frequencies'))
