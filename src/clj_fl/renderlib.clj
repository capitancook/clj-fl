(ns clj-fl.renderlib
  (:require [clj-fl.core :refer :all])
  (import [java.awt Color]
          [javax.imageio ImageIO]
          [javax.swing JPanel JFrame]))

(defn angle [p1 p2]
  (Math/atan (/ (- (p2 :y) (p1 :y))
                (- (p2 :x) (p1 :x)))))

(defn square [x]
  (* x x))

(defn third [s]
  (nth s 2))

(defn lenght [p1 p2]
  (Math/sqrt (+ (square (- (p2 :y) (p1 :y)))
                (square (- (p2 :x) (p1 :x))))))

(def frame (atom nil))

(def gfx (atom nil))

(def t (atom nil)) ;real world to screen window coordinate map

(defn startgraphics
  "start a graphics window of wx by xy pixels (400 by 400 if none is specified.
  The real world that is mapped in the graphic window is approx 12 meterd by 12 meters."
  ([] (startgraphics 400 400))
  ([wx wy]
   (reset! frame
           (doto (JFrame.)
             (.setVisible true)
             (.setSize wx wy)
             (.setBackground Color/WHITE)))
   (reset! gfx
           (doto
             (.getGraphics @frame)
             (.setStroke (java.awt.BasicStroke. 1.0))
             (.setColor (java.awt.Color. 0 0 255))))
   (reset! t {:tx (int(/ wx 20)) :ty (int (/ wx 20)) :dx (int (/ wx 8)) :dy (int (/ wy 8))})))


(defn transform [p]
  "Viewing Transformation - perform the mapping of real world coordinates of the point p
  to the screen window coordinates. here the real world is a rectangle on a plane of 12 by 12 meters"
  (list (+ (@t :dx) (* (first p) (@t :tx))) (+ (@t :dy) (* (second p) (@t :ty)))))

(defn transformsegment [s]
  "Viewing Transformation - perform the viewing transformation of the two end points of the segment s."
  (list (transform (first s)) (transform (second s))))

(defn setlinecolor [c]
  (.setColor @gfx (java.awt.Color. (first c) (second c) (third c))))

(defn setlinewidth [w]
  (.setStroke @gfx (java.awt.BasicStroke. w)))

(defn drawline [s]
  "Draw the line segment s"
  (let [st (transformsegment s)]
  (.drawLine @gfx (first(first st)) (second(first st)) (first(second st)) (second(second st)))))

(defn drawcolorline [c w s]
  "Draw the line segment s of color c and width w"
  (setlinecolor c)
  (setlinewidth w)
  (drawline s))

(defn drawstring [s p]
  "draw the string s starting from point p"
  (let [pt (transform p)]
  (.drawString @gfx s (first pt) (second pt))))

(defn drawcolorstring [c s p]
  "draw the string s starting from point p using the color c"
  (setlinecolor c)
  (drawstring s p))


(defn drawrectangle [x1 y1 x2 y2]
  (.drawRectangle @gfx x1 y1 x2 y2))

(defn drawfilledrectangle [x1 y1 x2 y2]
  (.fillRect @gfx x1 y1 x2 y2))


(defn drawcircle [x y r]
  (.drawOval @gfx (- x  r) (- y r ) (* 2 r) (* 2 r)))

(defn drawfilledcircle [x y r]
  (.fillOval @gfx (- x  r) (- y r ) (* 2 r) (* 2 r)))

(defn clear []
  "Clears the Graphics2D context"
  (.clearRect gfx 0 0 500 500))

(defn render2dbu [f]
  (let [ims (fget f :cof :value)]
    (drawcolorstring
     (list 0 0 255) (fget f :name :value) (fget f :sps :value))
    (dorun (map #((eval (fget-ii % :render2d :proc)) %) ims))))

(defn render2dsu [f]
  (let [ims (fget f :cof :value)]
    (drawcolorstring
     (list 0 0 255) (fget f :name :value) (fget f :sps :value))
    (dorun (map #((eval (fget-ii % :render2d :proc)) %) ims))))

(defn render2dbe [f]
  (drawcolorline (fget-i f :color :value) (fget-i f :width :value) (fget f :sps :value)))
