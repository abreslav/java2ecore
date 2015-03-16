**See also**
  * Our [bug-tracker](http://code.google.com/p/java2ecore/issues/list)

# Not yet implemented #
  * Ecore annotations
  * Import of existing Ecore models
  * Ecore2Java transformations

# Problems and known issues #
  * Fields in generic classes represented by interfaces (see [Syntax](Syntax.md) section)
```
@Class interface A<T> {
class _{
    T field; // an error here: type parameter T is not visible from static inner class
}
}
```
    * Workaround (ugly, but working):
```
@Class interface A<T> {
class _<T> { // Define the same type parameters as enclosing type does
    T field; 
}
}
```

# Unsupported things #
  * Generic exceptions are not supported in Java, so Java2Ecore does not support them