(ns aegis.unit.graph
  (:require [aegis
             [edge :as edge]
             [graph :as graph]
             [vertex :as vertex]]
            [midje.sweet :refer :all])
  (:import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph))

(def ^:dynamic *graph*)

(defmacro with-test-graph
  "Creates an empty TinkerGraph and binds it to the *graph* var.
  Closes the graph after the body is executed."
  [& body]
  `(let [graph# (TinkerGraph/open)]
     (binding [*graph* graph#]
       (try
         ~@body
         (finally
           (graph/close! graph#))))))

(facts "About graph functionality"
       (fact "Can retrieve all edges"
             (graph/edges *graph*) => (just [e1] :in-any-order))

       (fact "Can retrieve all vertices"
             (graph/vertices *graph*) => (just [v1 v2] :in-any-order))

       (against-background
        (around :contents
                (with-test-graph
                  (let [v1 (vertex/create! *graph*)
                        v2 (vertex/create! *graph*)
                        e1 (edge/create! v1 :edge v2)]
                    ?form)))))
