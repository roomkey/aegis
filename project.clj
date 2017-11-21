(defproject com.roomkey/aegis :lein-v
  :description "A Clojure library for managing the structure of Apache Tinkerpop graphs"
  :url "https://github.com/roomkey/aegis"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[com.roomkey/lein-v "6.1.0"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.tinkerpop/tinkergraph-gremlin "3.3.0"]
                 [potemkin "0.4.4"]]
  :profiles {:dev {:dependencies [[midje "1.8.3" :exclusions [org.clojure/clojure]]]}}
  :release-tasks [["vcs" "assert-committed"]
                  ["v" "update"] ;; compute new version & tag it
                  ["vcs" "push"]
                  ["deploy"]]
  :repositories {"rk-public" {:url "http://rk-maven-public.s3-website-us-east-1.amazonaws.com/releases/"}
                 "releases" {:url "s3://rk-maven/releases/" :creds :gpg}})
