(ns aegis.unit.vertex
  (:require [aegis
             [vertex :as vertex]
             [edge :as edge]]
            [aegis.unit.graph
             :refer [with-test-graph
                     *graph*]]
            [midje.sweet :refer :all]))

(facts "about vertex functionality"
       (facts "about creating a vertex"
              (fact "can create a vertex with no properties"
                    (vertex/properties v1) => {}
                    (vertex/property v1 "name") => nil
                    (vertex/label v1) => "vertex")

              (fact "can create a vertex with properties"
                    (vertex/properties v2) => {:name "John" :age 30}
                    (vertex/get v2 :name) => "John"
                    (vertex/label v2) => "vertex")

              (fact "can create a vertex with a label an no other properties"
                    (vertex/properties v3) => {}
                    (vertex/label v3) => "person")

              (fact "can create a vertex with a label and properties"
                    (vertex/properties v4) => {:name "Jim" :age 20}
                    (vertex/label v4) => "person"))

       (facts "about modifying a vertex"
              (fact "can add a property to a vertex"
                    (vertex/assoc! v1 :name "Sam")
                    (vertex/get v1 :name) => "Sam"
                    (vertex/assoc! v1 :name "Bob")
                    (vertex/get v1 :name) => "Bob")

              (fact "Can add multiple properties to a vertex"
                    (vertex/assoc! v1 :name "Sam" :age 27 :occupation "Developer")
                    (vertex/properties v1) => {:name "Sam" :age 27 :occupation "Developer"})

              (fact "Can update a property of a vertex"
                    (vertex/update! v1 :age + 1)
                    (vertex/get v1 :age) => 28)

              (fact "Can remove a property from a vertex"
                    (vertex/dissoc! v1 :name)
                    (vertex/get v1 :name) => nil
                    ;; No error should be thrown if removing a non existent property
                    (vertex/dissoc! v1 :name))

              (fact "Can remove multiple properties from a vertex"
                    (vertex/dissoc! v1 :age :occupation)
                    (vertex/properties v1) => {}))

       (facts "about finding a vertex by id"
              (fact "can find one vertex by id"
                    (vertex/find-by-id *graph* (vertex/id v1)) => v1)

              (fact "can find multiple vertices by id"
                    (vertex/find-by-id *graph* (vertex/id v1) (vertex/id v2))
                    => (just [v1 v2] :in-any-order)))

       (facts "about attached edges and connected vertices"
              (fact "can find attached edges"
                    (vertex/edges v1) => (just [e1 e2] :in-any-order)
                    (vertex/edges v1 :direction :out) => (just [e1])
                    (vertex/edges v1 :direction :in) => (just [e2])
                    (vertex/edges v1 :labels ["edge-1"]) => (just [e1])
                    (vertex/edges v1 :direction :out :labels ["edge-1"]) => (just [e1])
                    (vertex/edges v1 :direction :in :labels ["edge-1"]) => #{})

              (fact "can find connected vertices"
                    (vertex/vertices v1) => (just [v2 v3] :in-any-order)
                    (vertex/vertices v1 :direction :out) => (just [v2])
                    (vertex/vertices v1 :direction :in) => (just [v3])
                    (vertex/vertices v1 :labels ["edge-1"]) => (just [v2])
                    (vertex/vertices v1 :direction :out :labels ["edge-1"]) => (just [v2])
                    (vertex/vertices v1 :direction :in :labels ["edge-1"]) => #{}))

       (against-background
        (around :contents
                (with-test-graph
                  (let [v1 (vertex/create! *graph*)
                        v2 (vertex/create! *graph* {:name "John" :age 30})
                        v3 (vertex/create! *graph* {:label "person"})
                        v4 (vertex/create! *graph* {:label "person" :name "Jim" :age 20})
                        e1 (edge/create! v1 "edge-1" v2)
                        e2 (edge/create! v3 "edge-2" v1)]
                    ?form)))))
