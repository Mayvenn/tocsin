(defproject tocsin "0.2.2-SNAPSHOT"
  :description "Sends exceptions"
  :url "https://github.com/Mayvenn/tocsin"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [cheshire "5.8.0"]
                 [clj-stacktrace "0.2.8"]
                 [clj-http "2.0.0"]
                 [environ "1.0.2"]]
  :profiles
  {:dev {:source-paths ["dev"]
         :dependencies [[diff-eq "0.2.5"]
                        [org.clojure/tools.namespace "0.2.11"]]
         :injections [(require 'diff-eq.core)
                      (diff-eq.core/diff!)]}})
