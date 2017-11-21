(ns aegis.property
  (:refer-clojure :exclude [empty?])
  (:import org.apache.tinkerpop.gremlin.structure.Property))

(defn element
  "Returns the element that this property is associated with"
  [^Property property]
  (.element property))

(defn empty?
  "Returns true if the element is empty"
  [^Property property]
  (not (.isPresent property)))

(defn unbox
  "Returns the unboxed value associated with this property"
  ([^Property property]
   (unbox property nil))
  ([property not-found]
   (.orElse property nil)))

(defn remove!
  "Removes the property from the associated element"
  [^Property property]
  (.remove property))

