(defproject clj-fl "0.1.0-prealfa7"
;;   :description "Frame Language in Clojure"
;;   :dependencies [[org.clojure/clojure "1.6.0"]]
;;   :license {:name "Eclipse Public License"
;;             :url "http://www.eclipse.org/legal/epl-v10.html"}
;;   :url "https://github.com/capitancook/clj-fl")

  :description "Use cases for the Frame Language library in CLojure. The project contains the dependencies
  necessary to be used with LightTable"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.6.0"]
 ;                [clj-fl "0.1.0-prealfa7"]
                 [lein-light-nrepl "0.0.18"]
                 [adamclements/vijual "0.3.0-SNAPSHOT"]]
  :main clj-fl.core
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
