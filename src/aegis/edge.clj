(ns aegis.edge
  (:refer-clojure :exclude [get assoc! dissoc!])
  (:require [aegis.conversion :as conversion]
            [aegis.element :as element]
            [potemkin])
  (:import org.apache.tinkerpop.gremlin.structure.Edge
           org.apache.tinkerpop.gremlin.structure.Graph
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

(defn in-vertex
  "Returns the incoming vertex associated with this edge"
  [^Edge edge]
  (.inVertex edge))

(defn out-vertex
  "Returns the outgoing vertex associated with this edge"
  [^Edge edge]
  (.outVertex edge))

(defn create!
  "Creates a new edge in the graph between out-vertex and in-vertex with
  `label` as either a string or a keyword.
  If passed, properties is a map of key value pairs that will become
  properties of the edge. Keyword keys will be converted into strings
  by calling `name`.
  To set the id of the edge (if supported) use :id as a key."
  ([^Vertex out-vertex label ^Vertex in-vertex]
   (create! out-vertex (name label) in-vertex {}))
  ([^Vertex out-vertex label ^Vertex in-vertex properties]
   (.addEdge out-vertex (name label) in-vertex (conversion/map->Object-array properties))))

(defn find-by-id
  "Returns edges from the graph by their id"
  ([^Graph graph id]
   (first (iterator-seq (.edges graph (into-array Object [id])))))
  ([^Graph graph id & ids]
   (iterator-seq (.edges graph (into-array Object (cons id ids))))))
