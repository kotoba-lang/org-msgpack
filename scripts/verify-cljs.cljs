#!/usr/bin/env nbb
;; Run the suite on the ClojureScript side.
;;
;; Not a formality. Three things here are a different operation per runtime:
;; the UTF-8 codec (`String.getBytes` vs `TextEncoder`), IEEE 754 conversion
;; (`Double/doubleToLongBits` vs `DataView`), and integer width — a JVM
;; `long` reaches 2^63-1 and a ClojureScript number does not, which is why
;; this codec refuses the difference rather than letting it show.
;;
;;   nbb --classpath "$(clojure -A:cljs -Spath)" scripts/verify-cljs.cljs
(ns verify-cljs
  (:require [clojure.test :as t]
            [msgpack.core-test]))

(defmethod t/report [:cljs.test/default :end-run-tests] [m]
  (println)
  (if (t/successful? m)
    (println "all checks passed on the ClojureScript path")
    (do (println "FAILED on the ClojureScript path")
        (js/process.exit 1))))

(t/run-tests 'msgpack.core-test)
