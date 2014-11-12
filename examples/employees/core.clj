(ns examples.employes.core
  (:require [clj-fl.core :refer :all])
  (:require [vijual :as v]))


(load-kb-vec "examples\\employees\\kb.clj")

(v/draw-tree (vector ((eval (fget-ii "OrgAlfa" :showcofs :proc)) "OrgAlfa")))

(v/draw-tree (vector (showcofs "OrgAlfa")))
