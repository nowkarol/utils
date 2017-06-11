# Utilities - useful | interesting
## NullTraversal
Useful when you have to extract/copy data from deep data structures that can have null values all over. Especially and commonly if those data structures don't support Optional types when empty values can apper.

This simple utility was designed to extract data using especially but not exclusively method references:
```java
NullTraversal.nullSafeTraverse(structure, Structure::getDeeperStructure, DeeperStructure::getEvenDeeperStructure)
```
Currently only type safe method for traversal for depth 1 and 2 are defined, but adding more is a matter of usage of predefined NullTraversalWithUnlimitedDepth class. This class was not exposed for direct usage because of it's not type safe nature.
