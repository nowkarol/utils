# Utilities - useful | interesting
## NullSafeTraversal
Useful when you have to extract/copy data from deep data structures that can have null values all over. Especially and commonly if those data structures don't support Optional types when empty values can appear.

This simple utility was designed to extract data using especially but not exclusively method references:
```java
NullSafeTraversal.nullSafeTraverse(structure, Structure::getDeeperStructure, DeeperStructure::getEvenDeeperStructure)
```
Currently only type safe method for traversal for depth 1 and 2 are defined, but adding more is a matter of usage of predefined NullSafeTraversalWithUnlimitedDepth class. This class was not exposed for direct usage because of it's not type safe nature.

## NullSafeInitializer
Useful when you have to initialize deep data structures in a fluent way.

Because I don't know any better solution in Java, you need to provide both accessor (getter and setter) methods for NullSafeInitializer to work properly:
```java
NullSafeInitializer<A> initializer = NullSafeInitializer.of(a);
B bAfterInitialization = initializer.initIfNullAndGet(A::b, A::b, B::new).get();
```
Two versions of initialization methods exist:
* initIfNull(getter, setter, valueSupplier) => initializes a property using given accessors and valueSupplier and returns current object initializer for further fluent api initialization
* initIfNullAndGet(getter, setter, valueSupplier) => same, but at the end returns initializer for just initialized property for further fluent api initialization