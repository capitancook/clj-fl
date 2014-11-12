(ns example.core
  (:require [clj-fl.core :refer :all]
            [clj-fl.renderlib :refer :all]
            [vijual :as v]))

; load the example knowledge base for the use case OrgAlfa ERP

(load-kb-vec "example\\resource\\kb-employee.clj")

(v/draw-tree (vector ((eval (fget-ii "OrgAlfa" :showcofs :proc)) "OrgAlfa")))

(v/draw-tree (vector (showcofs "OrgAlfa")))

; load the example knowledge base the use case Knowledge Based Computer Aided Design

(load-kb-vec "example\\resource\\kb-kbcad.clj")


(startgraphics 400 400)
((eval (fget-ii "kitchen1" :render2d :proc )) "kitchen1")

(startgraphics 400 400)
(render2dsu "kitchen1")

(fget-ii "flat1" :render2d :proc )
(v/draw-tree (vector (showcofs "kitchen1")))
