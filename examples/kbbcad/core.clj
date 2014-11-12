(ns examples.kbbcad.core
  (:require [clj-fl.core :refer :all]
            [examples.kbbcad.renderlib :refer :all]
            [vijual :as v]))


(load-kb-vec "examples\\kbbcad\\kb.clj")


(startgraphics 400 400)
((eval (fget-ii "kitchen1" :render2d :proc )) "kitchen1")

(startgraphics 400 400)
(render2dsu "kitchen1")

(fget-ii "flat1" :render2d :proc )
(v/draw-tree (vector (showcofs "kitchen1")))


