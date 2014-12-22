(ns clj_fl.core-test
  (:require [clojure.test :refer :all]
            [clj-fl.core :refer :all]))

(deftest create-frame
  (testing "create a frame"
    (fcreate-frame "test")
    (is (= (:frame (fget-frame "test") "test")))))

(run-tests)

(println "Tests completed without error")
