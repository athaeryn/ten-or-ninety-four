(defproject ten-or-ninety-four "0.1.0-SNAPSHOT"
  :description "Should I take 10 or 94?"
  :url "https://github.com/athaeryn/ten-or-ninety-four"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.1"]
                 [cheshire "5.5.0"]
                 [clj-http "2.0.0"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler ten-or-ninety-four.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
