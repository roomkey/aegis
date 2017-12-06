(ns aegis.graph
  (:import org.apache.tinkerpop.gremlin.structure.Graph))

(defn close!
  "Closes the graph instance.
  Closing a Graph is equivalent to \"shutdown\" and
  implies that no further operations can be executed on the instance."
  [^Graph graph]
  (.close graph))

(defn configuration
  "Returns the Configuration associated with the construction of the graph"
  [^Graph graph]
  (.configuration graph))

(defn features
  "Returns the Graph.Features exposed by the underlying Graph implementation"
  [^Graph graph]
  (.features graph))

(defn traversal
  "Returns a reusable GraphTraversalSource instance"
  [^Graph graph]
  (.traversal graph))

(defn edges
  "Returns all of the edges in the graph"
  [^Graph graph]
  (->> (.edges graph (into-array Object []))
       iterator-seq
       (into #{})))

(defn vertices
  "Returns all of the vertices in the graph"
  [^Graph graph]
  (->> (.vertices graph (into-array Object []))
       iterator-seq
       (into #{})))

;; This approach is copied from clojurewerkz/archimedes and clojure.java.jdbc.
;; The ^:once metadata and use of fn* is explained by Christophe Grand in this blog post:
;; http://clj-me.cgrand.net/2013/09/11/macros-closures-and-unexpected-object-retention/
(defn with-transaction*
  [^Graph graph f]
  (try
    (let [result (f graph)]
      (.commit (.tx graph))
      result)
    (catch Throwable t
      (try (.rollback graph) (catch Exception _))
      (throw t))))

(defmacro with-transaction
  "Evaluates body in the context of a transaction on the specified graph, which must
   support transactions.  The binding provides the graph for the transaction and the
   name to which the transactional graph is bound for evaluation of the body.
   (with-transaction [tx graph]
     (vertex/create! tx)
     ...)
   Note that `commit` and `rollback` should not be called explicitly inside
   `with-transaction`. If you want to force a rollback, you must throw an
   exception or specify rollback in the `with-transaction` call:
   (with-transaction [tx graph :rollback? true]
      (vertex/create! tx)
      ...)"
  [binding & body]
  `(with-transaction*
     ~(second binding)
     (^{:once true} fn* [~(first binding)] ~@body)
     ~@(rest (rest binding))))
