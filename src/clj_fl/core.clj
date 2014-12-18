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

;-----Basic functions------- START

(defn fget-frame
  "If it exist return the frame f, otherwise nil"
  [f]
  (if-let [fv (get @frames f)]
    fv
    nil))

(defn fput-frame
  "Store the frame f in the frames repository"
  [f]
  (let [fn (get-in f [:frame :value]) ]
    (swap! frames #(assoc % fn f)))
  nil)

(defn fcreate-frame
  "Create the frame f and, if fm is passed, initialize it with the map fm"
  ([f]
   (let [v {f {:frame {:value f}}}]
     (swap! frames #(conj % v))
     v))
  ([f fm]
   (let [v (conj {f {:frame {:value f}}} fm)]
     (swap! frames #(conj % v))
     v)))

(defn fremove
  "Remove information in a given frame, slot, and facet"
  [& more]
  (swap! frames #(dissoc-in % (vec more)))
  nil)

;-----Basic functions------- END

(defn fget
  "Fetches information from a given frame, slot, and facet"
  [frame slot facet]
  (let [f (fget-frame frame)]
    (get-in f [slot facet])))

(defn fget-i
  [frame slot facet]
  "implement inheritance trying to fetch :value information from the :isa frames for a given frame and slot"
  (defn fget-i1[frames slot facet]
    (let [sframes (forceseq frames)]
      (if (nil? frames)
        nil
        (if-let [value (fget (first sframes) slot facet)]
          value
          (fget-i1 (next sframes) slot facet)))))
  (or (fget frame slot facet) (fget-i1 (fget frame :isa :value) slot facet)))


(defn fget-ii
  [frame slot facet]
  "fgte-ii implement inheritance. If the requested slot and facet aren't present in the frame, the function
  try to fetch slot information from the :isa frames, for each isa frames if doesn't find the requested
  slot.facet search in the ako frames."
  (defn fget-ii1
    [frames slot facet]
    (let [sframes (forceseq frames)]
      (if (nil? frames)
        nil
        (if-let [value (or (fget (first sframes) slot facet)
                           (fget-ii1  (fget (first sframes) :ako :value) slot facet))]
          value
          (fget-ii1 (next sframes) slot facet)))))

  (or (fget frame slot facet) (fget-ii1 (fget frame :isa :value) slot facet)))


(defn fput
  "Places information in a given frame, slot, and facet"
  [frame slot facet value]
  (let [v  (fget frame slot facet)
        vu (if-not (nil? v) (conj (forceseq v) value) value)
        f  (fget-frame frame)
        fp (assoc-in f [slot facet] vu)]
    (fput-frame fp))
  value)

(defn fput-p
  "Places information in a given frame, slot, and facet and activate
  the demons :range and :if-added"
  [frame slot facet v]
  (println (fput frame slot facet v))
  (let [frange (fget-ii frame slot :range)
        fif-added (fget-ii frame slot :if-added)]
    (cond
     (test/function? frange) ((resolve frange) frame slot facet v)
     (test/function? fif-added) ((resolve fif-added) frame slot facet v)))
  v)

(defn fput!
  "Overwrite the contentent of frame.slot.facet with v"
  [frame  slot facet v]
  (let [f (fget-frame frame)
        fp (assoc-in f [slot facet] v)]
    (fput-frame fp))
  v)

(defn fcopyslotvalue
  "Copies the facet :value of the slot slot of frame fa in frame fb"
  [fa fb slot]
  (if-let [v (fget fa slot :value)]
    (fput fb slot :value v)))

(defn fmergeslotvalue
  "merges the slot slot of frame fa in frame fb overwriting the slot slot of frame fb"
  [fa fb slot]
  (let [va (set (fget fa slot :value))
        vb (set (fget fb slot :value))
        vm (seq (clojure.set/union va vb))]
    (fput! fa slot :value vm)
    (fput! fb slot :value vm)))

(defn clamper
  "clamper is a demon stored in the :if-added facet of clamped frames
  by the fclamp function."
  [frame slot facet v]
  (let [framedest (fget frame :clamp :value)]
    (println "frame dest = " framedest)
    (fput framedest slot facet v)))

(defn fclamp
  "Its purpose is to tie together the two frames so that anything that goes into
  the given slot by way of either frame goes automatically into the other."
  [f1 f2 slot]
  (println (fput f1 :clamp :value f2))
  (println (fput f2 :clamp :value f1))
  (println (fput f1 slot :if-added 'clamper))
  (println (fput f2 slot :if-added 'clamper)))

(defn fcheck
  "Check if the information stored in a given frame, slot, and facet is equal to or contains value"
  [frame slot facet value]
  (in? (forceseq (fget frame slot facet)) value))

(defn fdeletefacetinfo
  "Delete the value info from the frame.slot.facet. If, after the removal,
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
          ))))
  info)

(defn fremovelinks
  "Remove the relations links  between  the frames fs and the frame f"
  [f fs links]
  (dorun (map #(fdeletefacetinfo % links :value f) (forceseq fs))))

(defn fdetachframe
  "Detach the frame from the links generated by  slot1 and slot2.
   For example, if f is linked to f1 and f2 by means of slot1 and
   slot2

   f1---slot1-->    ---slot1-->f2
    |           |  |           |
    |<--slot2----f <---slot2---|

  (fdetachframe 'f1' link1 link) leave f, f1 and f2 unconnected."

  [f slot1 slot2]
  (let [list1 (fget f slot1 :value) list2 (fget f slot2 :value)]
    (fremovelinks f list1 slot2)
    (fremovelinks f list2 slot1))
  (fremove f slot1)
  (fremove f slot2))

(defn fmoveframe
  "Given 3 frames f, f1 and f2, with f1 linked to f by means of slot1
  and f1 linked to f by means of slot2, and f2 unrelated to f, delete
  the links slot1 and slot2 between f and f1 and create new links
  slot1 and slot2 between f and f2

  f1---slot1-->                f2
  |           |
  |<--slot2----f

  executing:

  (fmoveframe f f1 f2 slot1 slot2)

  we obtain:

  f1              <--slot1---f2
                  |           |
                f ----slot2-->|"

  [f f1 f2 slot1 slot2]
  (fdeletefacetinfo f1 slot1 :value f)
  (fdeletefacetinfo f slot2 :value f1)
  (fput f2 slot1 :value f)
  (fput f slot2 :value f2))

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
    (if (test/function? result) ((resolve result) frame slot) result)))


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
  (resolve (fget-ii frame slot :proc)))

(defn is-a?
  "Return true if the if the frame f is a-kind-of ako. Multiple akos are possible"
  [ako f]
  (let [akos (forceseq (fget-ii f :ako :value))]
    (in? akos ako)))

;;;
;; demons
;



