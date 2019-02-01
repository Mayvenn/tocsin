(ns tocsin.core
  (:require [tocsin.clj-bugsnag :as bugsnag]
            [cheshire.core :refer [generate-string]])
  (:import [com.fasterxml.jackson.core JsonGenerationException]))

(defn replace-with-valid-json [d]
  (try
    (generate-string d)
    d
    (catch JsonGenerationException e
      (prn-str d))))

(defn remove-unruly-keys [unruly-keys d]
  (apply dissoc d unruly-keys))

(defn sanitize-ex-data [opts ex-data]
  (->> ex-data
       (remove-unruly-keys (:unruly-ex-data-keys opts))
       replace-with-valid-json))

(defn force-meta-option [opts e]
  ;; notice the key is currently ex–data, longer dash than ex-data
  {:meta {"ex–data" (when-let [ex-data (ex-data e)]
                      (sanitize-ex-data opts ex-data))}})

(def default-options
  {:version nil
   :notify-environments #{"production" "staging" "acceptance"}
   :unruly-ex-data-keys #{:type :schema :system :component}})

(defn- environment [options]
  (:environment options "production"))

(defn should-notify? [{:keys [notify-environments] :as options}]
  (or (not notify-environments)
      (notify-environments (environment options))))

(defn notify
  "Main interface for manually reporting exceptions.
   Suggested critical options: :api-key, :environment"
  [exception {:keys [api-key environment] :as options}]
  (assert (seq environment) "environment should be provided")
  (assert (seq api-key) "api-key should be provided")

  (let [opts (merge default-options options)]
    (when (should-notify? opts)
      (bugsnag/notify exception (merge opts (force-meta-option opts exception))))
    exception))
