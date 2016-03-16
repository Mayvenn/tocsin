(ns tocsin.core
  (:require [clj-bugsnag.core :as bugsnag]))

(def default-options
  {:version nil
   :notify-environments #{"production" "staging" "acceptance"}})

(defn- environment [options]
  (:environment options "production"))

(defn should-notify? [{:keys [notify-environments] :as options}]
  (or (not notify-environments)
      (notify-environments (environment options))))

(defn notify
  "Main interface for manually reporting exceptions.
   Suggested critical options: :api-key, :environment"
  ([exception]
   (notify exception nil))
  ([exception options]
   (let [opts (merge default-options options)]
     (when (should-notify? opts)
       (bugsnag/notify exception opts)))))
