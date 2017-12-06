(ns aegis.unit.edge
  (:require [aegis
             [edge :as edge]
             [graph :as graph]
             [vertex :as vertex]]
             [aegis.unit.graph
              :refer [with-test-graph
                      *graph*]]
             [midje.sweet :refer :all]))

(facts "about edge functionality"
       (facts "about creating an edge"
              (fact "can create an edge with no properties"
                    (edge/properties e1) => {:weight 10}
                    (edge/get e1 :weight) => 10
                    (edge/label e1) => "edge-1"
                    (edge/label e1 true) => :edge-1)
              (fact "can create and edge with properties"
                    (edge/properties e2) => {}
                    (edge/label e2) => "edge-2"))

       (facts "about modifying a edge"
              (fact "can add a property to a edge"
                    (edge/assoc! e2 :name "Sam")
                    (edge/get e2 :name) => "Sam"
                    (edge/assoc! e2 :name "Bob")
                    (edge/get e2 :name) => "Bob")

              (fact "Can add multiple properties to a edge"
                    (edge/assoc! e2 :name "Sam" :age 27 :occupation "Developer")
                    (edge/properties e2) => {:name "Sam" :age 27 :occupation "Developer"})

              (fact "Can update a property of a edge"
                    (edge/update! e2 :age + 1)
                    (edge/get e2 :age) => 28)

              (fact "Can remove a property from a edge"
                    (edge/dissoc! e2 :name)
                    (edge/get e2 :name) => nil
                    ;; No error should be thrown if removing a non existent property
                    (edge/dissoc! e2 :name))

              (fact "Can remove multiple properties from a edge"
                    (edge/dissoc! e2 :age :occupation)
                    (edge/properties e2) => {}))

       (facts "about finding and edge by id"
              (fact "can find one edge by id"
                    (edge/find-by-id *graph* (edge/id e1)) => e1)
              (fact "can find multiple edges by id"
                    (edge/find-by-id *graph* (edge/id e1) (edge/id e2))
                    => (just [e1 e2] :in-any-order)))

       (fact "can access in and out vertices"
             (edge/out-vertex e1) => v1
             (edge/in-vertex e1) => v2)

       (against-background
        (around :contents
                (with-test-graph
                  (let [v1 (vertex/create! *graph*)
                        v2 (vertex/create! *graph*)
                        e1 (edge/create! v1 "edge-1" v2 {:weight 10})
                        e2 (edge/create! v2 :edge-2 v1)]
                    ?form)))))

