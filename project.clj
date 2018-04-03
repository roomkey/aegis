(defproject com.roomkey/aegis "3.3.1.0"
  :description "A Clojure library for managing the structure of Apache Tinkerpop graphs"
  :url "https://github.com/roomkey/aegis"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.apache.tinkerpop/gremlin-core "3.3.1"]
                 [potemkin "0.4.5"]]
  :profiles {:dev {:dependencies [[midje "1.9.1" :exclusions [org.clojure/clojure]]
                                  [org.apache.tinkerpop/tinkergraph-gremlin "3.3.1"]]}}
  :release-tasks [["vcs" "assert-committed"]
                  ["vcs" "push"]
                  ["deploy"]]
  :repositories {"rk-public" {:url "http://rk-maven-public.s3-website-us-east-1.amazonaws.com/releases/"}
                 "releases" {:url "s3p://rk-maven/releases/" :no-auth true}})
