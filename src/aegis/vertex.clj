(ns aegis.vertex
  (:refer-clojure :exclude [get assoc! dissoc!])
  (:require [aegis.conversion :as conversion]
            [aegis.element :as element]
            [potemkin])
  (:import org.apache.tinkerpop.gremlin.structure.Graph
           org.apache.tinkerpop.gremlin.structure.Vertex))

(potemkin/import-fn element/graph)
(potemkin/import-fn element/has-id?)
(potemkin/import-fn element/id)
(potemkin/import-fn element/label)
(potemkin/import-fn element/property)
(potemkin/import-fn element/properties)
(potemkin/import-fn element/get)
(potemkin/import-fn element/assoc!)
(potemkin/import-fn element/dissoc!)
(potemkin/import-fn element/remove!)
(potemkin/import-fn element/update!)

(defn create!
  "Creates a new vertex in the graph. Properties is a map of key value pairs
  that will become properties of the vertex. Keyword keys will be converted
  into strings by calling `name`.
  To set the label of the vertex use :label as the key.
  To set the id of the vertex (if supported) use :id as a key."
  ([^Graph graph]
   (create! graph {}))
  ([graph properties]
   (.addVertex graph (conversion/map->Object-array properties))))

(defn find-by-id
  "Returns vertices from the graph by their id"
  ([^Graph graph id]
   (first (iterator-seq (.vertices graph (into-array Object [id])))))
  ([^Graph graph id & ids]
   (iterator-seq (.vertices graph (into-array Object (cons id ids))))))

(defn edges
  "Returns the edges attached to this vertex as a set.
  The value of direction is either a string, keyword or Direction.
  The value of labels is a vector of edges labels
  represented as either strings or keywords."
  [^Vertex vertex & {:keys [direction labels]
             :or {direction :both}}]

  (->> (.edges vertex
               (conversion/->edge-direction direction)
               (into-array String (map name labels)))
       iterator-seq
       (into #{})))

(defn vertices
  "Returns the vertices connected to this vertex as a set.
  The value of direction is either a string, keyword or Direction.
  The value of labels is a vector of edges labels
  represented as either strings or keywords."
  [^Vertex vertex & {:keys [direction labels]
             :or {direction :both}}]
  (->> (.vertices vertex
                  (conversion/->edge-direction direction)
                  (into-array String (map name labels)))
       iterator-seq
       (into #{})))
