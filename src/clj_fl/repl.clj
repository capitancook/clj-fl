(ns clj-fl.core
  (:require [clojure.test :as test]))

(load-kb-vec "..\\clj-fl-examples\\resources\\kb-orgalfa.clj")

(showisis "E1")
;;----- show relationships ---- start

(defn showcofs
  "show the hierarchy :cof for a given frame f"
  [f]
  (let [cofs (getcof f) cofslist (forceseq cofs)]
    (if (nil? cofs)
      (vector f)
      (reduce #( conj %1 ((if-let [showproc (fget-ii %2 :showcofs :proc)] (eval showproc) (vector %2)) %2)) (vector f)  cofslist))))

(defn showisis
  "show the hierarchy :isi for a given frame f"
  [f]
  (let [isis (getisi f) isislist (forceseq isis)]
    (if (nil? isis)
      (vector f)
      (reduce #( conj %1 ((if-let [showproc (fget-ii %2 :showisis :proc)] (eval showproc) (vector %2)) %2)) (vector f)  isislist))))

;;----- show relationships ---- end


(fclamp "E1" "E2" :prova)

(fget-ii "E2" :showcofs :proc)
(fget-ii "E1" :height :value)

(fput-p "E1" :prova :value "minni")

(fget "E2" :clamp :value)

(fget-frame "E1")
(fremove "E1" :prova)

(set '(1 2 3))
(set '(3 4 5))
(clojure.set/union (set '(1 2 3)) (set '(3 4 5)))

(into '() (clojure.set/union (set '(1 2 3)) (set '(3 4 5))))


