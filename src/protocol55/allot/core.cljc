(ns protocol55.allot.core)

(defn allot-1
 ([m k1]
  (list m))
 ([m k1 k2]
  (list (dissoc m k2)
        (dissoc m k1)))
 ([m k1 k2 & ks]
  (let [ks (set (concat [k1 k2] ks))]
    (map #(apply dissoc m (disj ks %)) ks))))

(defn dispatch-fn
  "Given a k-pred function that expects a single keyword argument returns a
  function suitable for multi method dispatching."
  [k-pred]
  (fn [m]
    (when (map? m)
      (->> m keys (some #(when (k-pred %) %))))))

(defn allot-fn
  "Given a k-pred function that expects a single keyword argument returns a
  function suitable for use by allot-by."
 [k-pred]
 (fn [m]
   (when (map? m)
     (->> m keys (filter #(k-pred %))))))

(defn allot-by
  "Divides a map m into a map of maps each with only a single occurance of any
  key produced by (ks-f m). Returns nil if no such keys exist in m."
 [m ks-f]
 (when-some [ks (seq (ks-f m))]
   (zipmap ks (apply allot-1 m ks))))

(defn key-name-pred
  "Returns a predicate function for use with allot-fn or dispatch-fn that
  returns true when k has name n."
  [n]
  (let [n (name n)]
    (fn [k]
      (= n (name k)))))

(defn key-namespace-pred
  "Returns a predicate function for use with allot-fn or dispatch-fn that
  returns true when k has namespace n."
  [n]
  (let [n (name n)]
    (fn [k]
      (= n (namespace k)))))

(defn allot-name
  "Divides a map m into a map of maps each with only a single occurance of any
  key with name n. Returns nil if no such keys exist in m."
  [m n]
  (allot-by m (allot-fn (key-name-pred n))))

(defn allot-namespace
  "Divides a map m into a map of maps each with only a single occurance of any
  key with namespace n. Returns nil if no such keys exist in m."
  [m n]
  (allot-by m (allot-fn (key-namespace-pred n))))
