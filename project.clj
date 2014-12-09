(defproject clj-fl "0.1.0-prealfa9"
  :description "Frame Language in Clojure"
;;   :dependencies [[org.clojure/clojure "1.6.0"]]
;;   :license {:name "Eclipse Public License"
;;             :url "http://www.eclipse.org/legal/epl-v10.html"}
;;   :url "https://github.com/capitancook/clj-fl")
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-time "0.6.0"]
;                 [clj-fl "0.1.0-prealfa9"]
                 [lein-light-nrepl "0.0.18"]
                 [adamclements/vijual "0.3.0-SNAPSHOT"]]
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})
