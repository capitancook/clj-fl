(ns example.core
  (:require  [clj-fl.core :refer :all]
             [clj-fl.renderlib :refer :all]
             [vijual :as v]))

(defn showkbcad
  []
  (load-kb-vec "resources\\kb-kbcad.clj")
  (println "***************************************************************************************")
  (println "*                                                                                     *")
  (println "* Knowledge Based Computer Aided design example for the Frame Language library clj-fl *")
  (println "*                                                                                     *")
  (println "***************************************************************************************")

  (println "***************************************************************************************")
  (println "*                                                                                     *")
  (println "* The knowledge Base contain the frame representation of a simple flat.               *")
  (println "* Flat1 is composed by :                                                              *")

  (v/draw-tree (vector (showcofs "flat1")))

  (println "***************************************************************************************")
  (println "* invoking the demon  in the render2d slot of the flat1 super class we can render its *")
  (println "* 2d graphic view.                                                                    *")
  (println "* Note that the graphics window doesn't repaint itself  so if you iconise  or         *")
  (println "* overlap it with another window you'll loose its graphics content.                   *")
  (println "***************************************************************************************")
  (startgraphics 600 600)
  ((eval (fget-ii "flat1" :render2d :proc )) "flat1")
  )

(defn showorgalfa
  []
  (load-kb-vec "resources\\kb-orgalfa.clj")
  (println "***************************************************************************************")
  (println "*                                                                                     *")
  (println "* Knowledge Based ERP example for the Frame Language library clj-fl                   *")
  (println "*                                                                                     *")
  (println "***************************************************************************************")
  (println "***************************************************************************************")
  (println "*                                                                                     *")
  (println "* The knowledge Base contain the frame representation of the OrgAlfa organization     *")
  (println "*                                                                                     *")
  (println "* OrgAlfa is composed by :                                                            *")
  (v/draw-tree (vector ((eval (fget-ii "OrgAlfa" :showcofs :proc)) "OrgAlfa")))
  )

