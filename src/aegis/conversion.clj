(ns aegis.conversion
  (:import org.apache.tinkerpop.gremlin.structure.T
           org.apache.tinkerpop.gremlin.structure.Direction))

(defprotocol DirectionConversion
  (->edge-direction [input] "Converts input to a Tinkerpop Direction"))

(extend-protocol DirectionConversion
  clojure.lang.Named
  (->edge-direction [input]
    (Direction/valueOf (clojure.string/upper-case (name input))))

  String
  (->edge-direction [input]
    (Direction/valueOf (clojure.string/upper-case input)))

  Direction
  (->edge-direction [input]
    input))

(defn- stringify-key
  "Converts keywords to keys and translates the meaning of reserved keywords
  :label and :id to the underlying Tinkerpop token"
  [[k v]]
  (condp = k
    :label [T/label v]
    :id [T/id v]
    [(name k) v]))

(defn map->Object-array
  "Flattens a Clojure map into a Java varargs compatible array"
  [m]
  (->> m
       (map stringify-key)
       flatten
       (into-array Object)))

