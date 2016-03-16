(ns tocsin.core-test
  (:require [clojure.test :refer :all]
            [tocsin.core :refer :all]))

(deftest notifies-bugsnag-with-the-appropriate-notify-environments
  (is (should-notify? nil))
  (is (should-notify? {:notify-environments nil}))
  (is (should-notify? {:notify-environments #{"development"} :environment "development"}))
  (is (not (should-notify? {:notify-environments #{"production"} :environment "development"}))))
