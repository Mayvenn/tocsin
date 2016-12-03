# Tocsin

Send exceptions to bugsnag.

# Installation

Add to your dependencies in your `project.clj`:

```clojure
[tocsin "0.1.1"]
```

# Usage

The minimum needed is to call `notify`:

```clojure
(ns myapp.core
   (:require [tocsin.core :as tocsin]
             [environ.core :refer [env]] ;; see https://github.com/weavejester/environ
             ...))

(try
   ... start app ...
   (catch Exception e
     (tocsin/notify e {:api-key (env :bugsnag-token)
                       :environment (env :environment)
                       :project-ns "myapp"})))
```

Here's the options broken down:

 - `:api-key` is your bugsnag project api key.
 - `:environment` is the environment to report to bugsnag. By default only `"production", "staging" "acceptance"` environment exceptions are sent to bugsnag. We usually set this to `"development"` in our `.lein-env` to prevent local development exceptions getting reported in bugsnag. See `:notify-release-stages` below.
 - `:project-ns` the namespace prefix for bugsnag to determine how to filter exceptions.

Anytime you want to capture thrown exception just use `try...catch` with `tocsin/notify`.

## Technical Details

`tocsin/notify` options are a pass-through to [clj-bugsnag](https://github.com/wunderlist/clj-bugsnag) with the following changes:

 - `:ex-data` sent via clj-bugsnag no longer has a en-dash (ex–data) but just a hypen (ex-data). Because it's harder to override clj-bugsnag's key. 
 - `:unruly-ex-data-keys` can be provided as a vector of keys to automatically dissoc from `:ex-data`. Defaults to `#{:type :schema :system :component}` which correspond to [stuart-sierra's component](https://github.com/stuartsierra/component) exceptions that fail to start.
 - If `:ex-data` cannot be converted to json properly (probably because java classes are embedded in it), then tocsin will use `prn-str` on `:ex-data` and send the string through as a fallback.
 - `:notify-release-stages` defaults to `#{"production" "staging" "acceptance"}`
 - Unlike clj-bugsnag, all options are passed to bugsnag (except `:unruly-ex-data-keys`).
 
