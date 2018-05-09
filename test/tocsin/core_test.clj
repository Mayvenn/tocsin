(ns tocsin.core-test
  (:require [clojure.test :refer :all]
            [tocsin.core :refer :all]))

(deftest notifies-bugsnag-with-the-appropriate-notify-environments
  (is (should-notify? nil))
  (is (should-notify? {:notify-environments nil}))
  (is (should-notify? {:notify-environments #{"development"} :environment "development"}))
  (is (not (should-notify? {:notify-environments #{"production"} :environment "development"}))))

(deftest meta-has-empty-ex-data-when-not-present
  (is (= {:meta {"ex–data" nil}} (force-meta-option default-options (Exception.)))))

(deftest meta-has-sanitized-ex-data
  (testing "remove unruly keys"
    (is (= {:meta {"ex–data" {:a 1}}} (force-meta-option default-options (ex-info "oh no" {:a 1 :system (fn [] 1) :type "big"})))))
  (testing "replacing something that can't be json with a str"
    (is (.contains (get-in (force-meta-option default-options (ex-info "oh no" {:a (fn [] 1)}))
                            [:meta "ex–data"])
                    ":a"))))


(deftest ensures-api-key-and-environment-are-present
  (let [e (Exception. "An exception")]
    (is (thrown? AssertionError (notify e {:environment "development"}))
        "api-key should be present")
    (is (thrown? AssertionError (notify e {:api-key "api-key"}))
        "environment should be present")))
