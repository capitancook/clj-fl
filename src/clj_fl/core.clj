(ns clj-fl.core
  (:require [clojure.test :as test]))

(def frames (atom {}))

;;
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

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn forceseq
  "Force passed parameter  to be a seq"
  [x]
  (if-not (seq? x)
    (list x)
    x))

(defn abs
  [n]
  (if (neg? n) (- n) n))

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
  (if-let [fv (get @frames f)]
    fv
    (create-frame f)))

(defn fput-frame
  "Store the frame f in the frames repository"
  [f]
  (let [fn (get-in f [:frame :value]) ]
    (swap! frames #(assoc % fn f)))
  nil)

(defn fget
  "Fetches information from a given frame, slot, and facet"
  [frame slot facet]
  (get-in @frames [frame slot facet]))

(defn fput
  "Places information in a given frame, slot, and facet"
  [frame slot facet value]
  (let [v (fget frame slot facet) vu (if-not (nil? v) (conj (forceseq v) value) value)]
  (swap! frames #(assoc-in % [frame slot facet] vu)))
  nil)

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
  (swap! frames #(dissoc-in % (vec more)))
  nil)

(defn fcheck
  "Check if the information stored in a given frame, slot, and facet is equal to or contains value"
  [frame slot facet value]
  (in? (forceseq (fget frame slot facet)) value))

(defn fdeleteinfo
  "Delete the value info from the frame.slot.facet . If, after the removal,
  the facet is empty or contains nil the facet is removed too"
  [frame slot facet info]
  (if (fcheck frame slot facet info)
    (let [l (fget frame slot facet) sl (forceseq l)
          slu (remove #{info} sl) c (count slu)]
      (fremove frame slot facet)
      (if-not (or (empty? slu) (nil? (first slu)))
        (if (= c 1)
          (fput frame slot facet (first slu))
          (fput frame slot facet slu)
          )))))

(defn removeframelinks
  "Remove the relations links  between  the frames fs and the frame f"
  [f fs links]
  (dorun (map #(fdeleteinfo % links :value f) (forceseq fs))))

(defn fget-v-d
  "Fetches :value information from a given frame and slot or, in case there is no :value facet, fetches :default facet"
  [frame slot]
  (if-let [v (fget frame slot :value)]
    (if v v (fget frame slot :default))))

(defn fget-v-d-p
  "Fetches :value information from a given frame and slot and, in case there is no :value facet, fetches :default facet,
   otherwise activate the :if-neede demon"
  [frame slot]
  (let [result (or (fget-v-d frame slot) (fget-ii frame slot :if-needed))]
;;     (println result)
    (if (test/function? result) ((eval result) frame slot) result)))

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
  "get the facet :value of the slot s of a given frame symbol f"
  [f s]
  (fget f s :value))


(defn getn
  "get the facet :value of the slot :name of a given frame f"
  [f]
  (fget f :name :value))

(defn getcof
  "get the  facet :value of the slot :cof (component-of) of a given frame f"
  [f]
  (fget f :cof :value))

(defn getisi
  "get the  facet :value of the slot :isi (is-in) of a given frame f"
  [f]
  (fget f :isi :value))

(defn getako
  "get the  facet :value of the slot :ako (a-kind-of) of a given frame f"
  [f]
  (fget f :ako :value))

(defn getisa
  "get the  facet :value of the slot :isa  of a given frame f"
  [f]
  (fget f :isa :value))

(defn getproc
  "get the  facet :value of the slot :cof (component-of) of a given frame f"
  [frame slot]
  (eval (fget-ii frame slot :proc)))


(defn is-a?
  "Return true if the if the frame f is a-kind-of ako. Multiple akos are possible"
  [ako f]
  (let [akos (forceseq (fget-ii f :ako :value))]
    (in? akos ako)))

;;;
;; demons
;



