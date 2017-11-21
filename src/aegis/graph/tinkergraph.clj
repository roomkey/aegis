(ns aegis.graph.tinkergraph
  (:import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph))

(defn open!
  "Opens and returns a new TinkerGraph instance."
  ([]
   (TinkerGraph/open)))
