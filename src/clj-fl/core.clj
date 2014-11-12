(ns clj-fl.core
  (:require [clojure.test :as test]))

(def frames (atom {}))


;;;
;; Service functions
;

;; Load & Save knowledge base

(defn reset-kb
  "reset the knowledge base"
  []
  (reset! frames {}))

(defn load-kb-vec
  "load the knowledge base stored in the file f as a vector of frame maps"
  [f]
  (let [frs (read-string (slurp f)) fs (reduce #(assoc %1 (get-in %2 [:frame :value]) %2) {} frs)]
    (reset! frames  fs)))

(defn save-kb
  "save the current knowledge base in the file f"
  [f]
  (spit f @frames))

(defn load-kb
  "load the knowledge base stored in the file f as a map of frame maps"
  [f]
  (let [frs (read-string (slurp f))]
    (reset! frames  frs)))

;; dissoc-in was once part of clojure.contrib.core, and is now part of core.incubator.

(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

;;;
;; Frame Language function
;

(defn create-frame
  "Create the frame f and, if fm is passed, initialize it with the map fm"
  ([f]
  (let [v {f {:frame {:value f}}}]
    (swap! frames #(conj % v))
    v))
  ([f fm]  (let [v (conj {f {:frame {:value f}}} fm)]
    (swap! frames #(conj % v))
            v)))

(defn fget-frame
  "If it exist return the frame f, otherwise create the frame f and returns it"
  [f]
  (let [fv (get @frames f)]
    (if fv fv (create-frame f))))

(defn fput-frame
  "Store the frame f in the frames repository"
  [f]
  (let [fn (get-in f [:frame :value]) ]
    (swap! frames #(assoc % fn f))))

(defn fget
  "Fetches information from a given frame, slot, and facet"
  [frame slot facet]
  (get-in (fget-frame frame) [slot facet]))

(defn fput
  "Places information in a given frame, slot, and facet"
  [frame slot facet v]
  (swap! frames #(assoc-in % [frame slot facet] v)))

(defn fput-p
  "Places information in a given frame, slot, and facet and activate the demons :range and :if-added"
  [frame slot facet v]
  (let [f (fget-frame frame) tf (assoc-in f [slot facet] v)]
    (if ((fget f slot :range) frame slot)
      nil
      ((fget f slot :if-added) frame slot))
    (fput-frame tf)))

(defn fremove
  "Remove information in a given frame, slot, and facet"
  [& more]
  (println "more = " more)
  (swap! frames #(dissoc-in % (vec more))))

(defn fcheck
  "Check if the information stored in a given frame, slot, and facet is equal to value"
  [frame slot facet value]
  (= (fget frame slot facet) value))

(defn fget-v-d
  "Fetches :value information from a given frame and slot or, in case there is no :value facet, fetches :default facet"
  [frame slot]
  (if-let [v (fget frame slot :value)]
    (if v v (fget frame slot :default))))

(defn fget-v-d-p
  "Fetches :value information from a given frame and slot and, in case there is no :value facet, fetches :default facet,
   otherwise activate the :if-neede demon"
  [frame slot]
  (let [result (or (fget-v-d frame slot) (fget frame slot :if-needed))]
    (if (test/function? result) (result frame slot) result)))

(defn fget-i
  [frame slot facet]
  "implement inheritance trying to fetch :value information from the :isa frames for a given frame and slot"
  (defn fget-i1[frames slot facet]
    (cond
     (nil? frames) nil
     (not (list? frames)) (fget frames slot facet)
     :else (if-let [value (fget (first frames) slot facet)]
             (do (println "value = " value) value)
             (fget-i1 (next frames) slot facet))))
  (or (fget frame slot facet) (fget-i1 (fget frame :isa :value) slot facet)))


(defn fget-ii
  [frame slot facet]
  "fgte-ii implement inheritance. If the requested slot and facet aren[t present in the frame, the function
  try to fetch slot information from the :isa frames otherways look for the slot in all the :ako frames
  of all the :isa."
  (defn fget-ii1[frames slot facet]
    (cond
     (nil? frames) nil
     (not (list? frames)) (or (fget frames slot facet) (fget-ii1  (fget frames :ako :value) slot facet))
     :else
     (if-let [value (or (fget (first frames) slot facet)
                        (fget-ii1  (fget (first frames) :ako :value) slot facet))]
       value
       (fget-ii1 (next frames) slot))))
  (or (fget frame slot facet) (fget-ii1 (fget frame :isa :value) slot facet)))

;;;
;; utility short name functions
;

(defn getv
"get the :value of the slot s of a given frame symbol f"
[f s]
  (fget f s :value))


(defn getn
"get the :value of :name of a given frame symbol f"
[f]
  (fget f :name :value))

(defn getcof
"get the :cof (component-of) frames of a given frame symbol f"
[f]
  (fget f :cof :value))

(defn getako
"get the :ako (a-kind-of) frames of a given frame symbol f"
[f]
  (fget f :ako :value))

(defn getisa
"get the :isa frames of a given frame symbol f"
[f]
  (fget f :isa :value))

(defn ako?
  [ako f]
  (= (getako f) ako))

(defn is-ako?
  [ako frames]
  (loop [fs frames result false]
    (cond
     (nil? fs) result
     (not (list? fs)) (ako? ako fs)
     :else (recur (next fs) (or result (ako? ako (first fs)))))))

(defn is-a?
  [ako f]
  (let [isas (getisa f)]
    (if (nil? isas) false (is-ako? ako isas))))

(defn showcofs[f]
  (let [cofs (getcof f) cofslist (if-not (list? cofs) (list cofs) cofs)]
    (if (nil? cofs)
      (vector f)
      (reduce #( conj %1 ((if-let [showproc (fget-ii %2 :showcofs :proc)] (eval showproc) (vector %2)) %2)) (vector f)  cofslist))))



