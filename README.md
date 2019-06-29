# allot

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.protocol55/allot.svg)](https://clojars.org/org.clojars.protocol55/allot)

## First, an example

```shell
user=> (require '[protocol55.allot.core :as a])
nil
user=> (a/allot-name {:bar/bar 1 :foo/bar 2 :foo/baz 3 :x 4 :y 5 :z 6} :bar)
{:bar/bar {:bar/bar 1, :foo/baz 3, :x 4, :y 5, :z 6}, :foo/bar {:foo/bar 2, :foo/baz 3, :x 4, :y 5, :z 6}}
user=> (a/allot-namespace {:bar/bar 1 :foo/bar 2 :foo/baz 3 :x 4 :y 5 :z 6} :foo)
#:foo{:bar {:bar/bar 1, :foo/bar 2, :x 4, :y 5, :z 6}, :baz {:bar/bar 1, :foo/baz 3, :x 4, :y 5, :z 6}}
```

## What's going on?

When we call `allot-name` giving it a map and a keyword we get back a map where
each key is a key in the original map that has that name and whose value is the
original map minus the set of all other keys with that name. 

```clojure
{:bar/bar {:bar/bar 1, :foo/baz 3, :x 4, :y 5, :z 6}, :foo/bar {:foo/bar 2, :foo/baz 3, :x 4, :y 5, :z 6}}
```

You'll notice that that `:bar/bar` contains `:bar/bar` but not `:foo/bar`, and
likewise `:foo/bar` does not contain `:bar/bar`.

The same thing happens with `allot-namespace`, but now only one key that has the
specified namespace will appear in each resulting map.

## Use with multimethods

```clojure
(defmulti bar-multi (a/dispatch-fn (a/key-name-pred :bar)))
```

## Making your own variations

The libary provides functions for constructing your own allot variations:

```
protocol55.allot.core/allot-by
([m ks-f])
  Divides a map m into a map of maps each with only a single occurance of any
  key produced by (ks-f m). Returns nil if no such keys exist in m.

protocol55.allot.core/allot-fn
([k-pred])
  Given a k-pred function that expects a single keyword argument returns a
  function suitable for use by allot-by.
```
