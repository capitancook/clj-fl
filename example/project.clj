(defproject example "0.1.0"
  :description "Use cases for the Frame Language library in CLojure. The project contains the dependencies
  necessary to be used with LightTable"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.6.0"]
                 [clj-fl "0.1.0-prealfa7"]
                 [lein-light-nrepl "0.0.18"]
                 [adamclements/vijual "0.3.0-SNAPSHOT"]]
  :main example.core
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
