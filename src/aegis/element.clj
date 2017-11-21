(ns aegis.element
  (:refer-clojure :exclude [get assoc! dissoc!])
  (:require [aegis.property :as property])
  (:import org.apache.tinkerpop.gremlin.structure.Element))

(defn graph
  "Returns the graph that this entity is within."
  [^Element element]
  (.graph element))

(defn has-id?
  "Returns true if this entity has a unique identifier."
  [^Element element]
  (.hasId element))

(defn id
  "Returns the unique identifier of this entity."
  [^Element element]
  (.id element))

(defn label
  "Returns the label associated with this element. If kw? is true then
  the string label will be kewordized before being returned"
  ([^Element element]
   (.label element))
  ([^Element element kw?]
   (if kw?
     (keyword (label element))
     (label element))))

(defn property
  "Returns a Property for the entity given its key"
  [^Element element key]
  (let [property (.property element (name key))]
    (when-not (property/empty? property)
      property)))

(defn properties
  "Returns all of the property keys and values as a Clojure map with keyword keys"
  [^Element element]
  (as-> element x
      (.properties x (into-array String []))
      (iterator-seq x)
      (map #(vector (keyword (.key %)) (.value %)) x)
      (into {} x)))

(defn get
  "Returns the value of the property at key, not-found or nil if key not present"
  ([^Element element key not-found]
   (if-let [property (property element (name key))]
     (.orElse property not-found)
     not-found))
  ([^Element element key]
   (get element key nil)))

(defn assoc!
  "Sets properties of the element returning the modified element"
  ([^Element element key value]
   (.property element (name key) value)
   element)
  ([^Element element key value & kvs]
   (assoc! element key value)
   (doseq [[key value] (partition 2 kvs)]
     (assoc! element key value))
   element))

(defn dissoc!
  "Removes the properties found at keys returning the modified element"
  [^Element element & keys]
  (doseq [key keys]
    (when-some [p (property element key)]
      (property/remove! p)))
  element)

(defn update!
  "Updates the value of the property of an element where k is the key of
  the property and f is a function that will take the old value
  and any supplied args and return the new value, and returns the updated element.
  If the a property does not exist with key k, nil will be passed as the old value."
  ([^Element element k f]
   (assoc! element k (f (get element k))))
  ([^Element element k f x]
   (assoc! element k (f (get element k) x)))
  ([^Element element k f x y]
   (assoc! element k (f (get element k) x y)))
  ([^Element element k f x y z]
   (assoc! element k (f (get element k) x y z)))
  ([^Element element k f x y z & more]
   (assoc! element k (apply f (get element k) x y z more))))

(defn remove!
  "Removes an element from the graph"
  [^Element element]
  (.remove element))

