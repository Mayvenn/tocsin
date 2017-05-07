(defproject tocsin "0.1.2-SNAPSHOT"
  :description "Sends exceptions"
  :url "https://github.com/Mayvenn/tocsin"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [cheshire "5.5.0"]
                 [clj-bugsnag "0.2.9"]]
  :profiles
  {:dev {:source-paths ["dev"]
         :dependencies [[diff-eq "0.1.0"]
                        [org.clojure/tools.namespace "0.2.9"]]
         :injections [(require 'diff-eq.core)
                      (diff-eq.core/diff!)]}})
