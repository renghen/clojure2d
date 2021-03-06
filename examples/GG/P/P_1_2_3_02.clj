(ns examples.GG.P.P-1-2-3-02
  (:require [clojure2d.core :refer :all]
            [clojure2d.math.random :as r]
            [clojure2d.math.vector :as v]
            [clojure2d.math :as m]
            [clojure2d.color :as c]))

(defn make-parts
  "Create fragments"
  [^long part-count]
  (loop [ii (int 0)
         mx part-count
         coll []] 
    (if (< ii mx)
      (if (r/brand 0.075)
        (let [fragments (r/irand 2 20)]
          (recur (inc ii) (+ mx fragments) (vec (concat coll (repeatedly fragments #(r/drand 2))))))
        (recur (inc ii) mx (conj coll (r/drand 2 20))))
      coll)))

(defn draw
  "Draw stripes"
  [canvas]
  (let [iter (make-counter)
        colors (mapv #(c/from-HSB (if (even? %)
                                    (v/vec4 (r/drand 256) 255 (r/drand 255) 255)
                                    (v/vec4 138 (r/drand 255) 255 255))) (range 20))
        row-count (r/irand 5 40) 
        row-height (/ (height canvas) (double row-count))
        rows (map #(vector % (make-parts (inc %))) (range row-count))]
    (doseq [[i parts] rows]
      (let [sum-parts-total (reduce + parts)
            cumulative (reductions + 0 parts)]
        (mapv (fn [w p]
                (set-color canvas (colors (mod (iter) 20)))
                (rect canvas (m/norm w 0 sum-parts-total 0 (width canvas))
                      (* i row-height)
                      (m/norm p 0 sum-parts-total 0 (width canvas)) row-height)) cumulative parts)))))

(def canvas (make-canvas 800 800 :highest))
(def window (show-window canvas "P_1_2_3_02"))

(defmethod mouse-event [(:window-name window) :mouse-released] [_ _]
  (with-canvas-> canvas
    draw))

(with-canvas-> canvas
  draw)
