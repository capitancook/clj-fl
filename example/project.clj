(defproject example "0.1.0-SNAPSHOT"
  :description "Use cases for the Frame Language library in CLojure"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.6.0"]
                 [lein-light-nrepl "0.0.18"]
                 [adamclements/vijual "0.3.0-SNAPSHOT"]]
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
