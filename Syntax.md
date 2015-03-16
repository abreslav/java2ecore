# Java2Ecore Syntax #
Java2Ecore uses Java syntax to express Ecore constructs. If some construct is not defined in Java we use annotations. All the annotations mentioned below can be found in [org.abreslav.java2ecore.annotations](http://java2ecore.googlecode.com/svn/trunk/org.abreslav.java2ecore.docs/index.html) package and it's subpackages.

# EPackage specification #
When you describe some Ecore model in a `*`.java file you define a top-level interface (or class) to specify a root EPackage of the model, EPackages's contents (classifiers and subpackages) are denoted by nested Java types.

Use `@EPackage` annotation to denote a type that describes EPackage and set up nsPrefix and nsURI attributes.
```
@EPackage(
    nsPrefix="samples",   
    nsURI="http:///example.com/samples"
)  
public interface samples {
    @EPackage(
        nsPrefix="subpackage",
        nsURI="http:///example.com/samples/subpackage"
    )
    class subpackage {
        // subpackage contents
    }

    // other root package contents
}
```
EPackage will have the same name as this class.

# Classifiers #
Java2Ecore supports all three types of Ecore classifiers:
  * _EClass_ which is represented by Java `class` or `interface` (see below)
```
@EPackage(
    nsPrefix="samples",   
    nsURI="http:///example.com/samples"
)  
public interface samples {
    interface MyInterface {
    }
    class First {
    }
    class Sample extends First implements MyInterface { // both extends and implements are supported
    }
    abstract class Abstract { // abstract modifier sets up EClass' abstract attribute
    }
}
```
  * _EDataType_ which is represented by Java `class` with `@EDataType` annotation or just created automatically (see below)
```
@EDataType("java.awt.Point")
class Point {}
```
  * _EEnum_ which is represented by Java `enum`
```
enum Enum {
    A,
    B,
    C;
}
```

## Setting instance type (instance class) ##

To set _instanceTypeName_ attribute of EClass, supply `@InstanceTypeName` annotation:
```
@InstanceTypeName("java.util.Comparator<Integer>")
class IntComparator {
}
```

## Inheritance ##

As it is mentioned above Java-style inheritance (both `extends` and `implements`) is fully supported.
```
interface MyInterface {
}
class First {
}
class Sample extends First implements MyInterface { // both extends and implements are supported
}
abstract class Abstract { // abstract modifier sets up EClass' abstract attribute
}
```

But here we face a problem of multiple inheritance for classes being supported by Ecore and not supported by Java.
Java2Ecore provides a workaround which we call an _interface form of class_ (see below)...

### Interfaces with fields ###

In Java you cannot declare non-final fields in interfaces. But `final` sets  _changeable_ to `false`. A final field also has to be initialized which will set up _defaultValue_ unless you specify `@NoDefaultValue` annotation. And Ecore supports any structural features in interfaces.

So we provide the following: each interface (or class, to be consistent) can declare one _inner class_ (it's name does not matter), which holds all the parent's contents:
```
interface I {
class _{
    int x;
    I other;
}
}
```
In this example we created an EClass having `interface` attribute set to `true` and having one integer attribute and one reference of type `I`.

### Multiple inheritance for classes: interface form of class ###

There's no multiple inheritance for classes in Java. But there is one for interfaces.

To provide multiple inheritance for Ecore EClasses you can define them as Java interfaces supplying a `@EClass` annotation which will tell Java2Ecore not to set `interface` attribute to true.
```
@EClass interface A {
class _{
   int x;
}
}
@EClass interface B {
class _{
   A a;
}
}
@EClass interface C extends A, B {
}
```

In this sample we created three **classes**: A, B and C, where C is a descendant of both A and B.

You still can specify `abstract` attribute along with `@EClass` annotation to make your class abstract:
```
abstract @EClass interface A {
class _{
   Object o;
}
}
```

## Handling unknown types ##
When you define a type in your EPackage and then use it, there's no doubt about what happens then.

But what if you use some Java type not being declared in your package?

### Handling Ecore types ###
If this is a class from Ecore itself (e.g. `EStructuralFeature`) the corresponding EClass is chosen.

### The case of an unknown type ###
Otherwise Java2Ecore creates a new EDataType with `instanceClassName` attribute set to fully-qualified name of unknown class.

The only exception is for unknown supertypes: such a type has to be EClass, not EDataType.
So, Java2Ecore reports an error in this case.

## Non-Model classifiers ##

Eventually you may need to define some Java type which you want Java2Ecore to ignore (not to create a corresponding Ecore classifier). You can obtain this by supplying `@NonModel` annotation:
```
@NonModel
class AuxilaryType {

}
```

# Structural features #

Structural features are represented by fields.
```
class A {
    int x;
    A y;
}
```
ERerferences are distinguished from EAttributes by type: if the type is EClass then the feature will be a reference, otherwise (see _Handling unknown types_ section) it will be an attribute.

Above example is transformed to the following:

![http://content.screencast.com/media/f65373b7-9f45-481e-ab41-a17993595cfc_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png](http://content.screencast.com/media/f65373b7-9f45-481e-ab41-a17993595cfc_2d2ecdec-33f4-4541-acd2-9ed52d228863_static_0_0_image.png)


## Primitive types ##

Ecore defines a number of EDataTypes for general purposes: `EInt`, `EJavaObject` and so on.
To use those types in your model just use their Java analogs: `int` and `Object` for given example.

## Multiplicities ##

There are several ways to denote feature's multiplicity:
  * Stay satisfied by default multiplicity: 0..1
  * Use Java array type
  * Use Java collections (`java.util.Collection` and it's descendants)
  * Use MCollections (from [org.abreslav.java2ecore.multiplicities](http://java2ecore.googlecode.com/svn/trunk/org.abreslav.java2ecore.docs/index.html) package)
  * Use `@Bounds` annotation

### Java arrays ###
You can denote a 0..`*` multiplicity by using array type:
```
class A {
    int[] x; // produces an Attribute with lowerBound = 0 and upperBound = -1
}
```
Multiple feature is marked as _ordered_ and _unique_, since these are default values.

You can change values of _ordered_ and _unique_ by specifying corresponding annotations (see below).

**Note:** only one-dimension arrays are allowed and brackets must be specified after the type, not field name.

### Java collections ###
You can use `java.util.Collection<E>`, `java.util.Set<E>` and `java.util.List<E>` to denote the same 0..`*` multiplicity.

However, _ordered_ and _unique_ will differ:
```
class A {
    Collection<A> as; // 0..*, not ordered, not unique
    Set<String> strings; // 0..*, not ordered, unique
    List<Integer> ints; // 0..*, ordered, not unique (same as arrays)
}
```
`@Ordered` might be used for Collections and Sets (but not for Lists).

`@Unique` might be used for Collections and Lists (but not for Sets).

### MCollections ###
There are some auxiliary types defined in `org.abreslav.java2ecore.multiplicities` package.

Main three of them are:
  * MCollection<E, L extends ILowerBound, U extends IUpperBound>
  * MSet<E, L extends ILowerBound, U extends IUpperBound>
  * MList<E, L extends ILowerBound, U extends IUpperBound>

These classes are analogous to Java collections but they allow to specify lower and upper bounds in their **L** and **U** parameters.

Possible values are
  * `Infinity` - for infinite upper bound (-1)
    * Defined in `org.abreslav.java2ecore.multiplicities` package
  * `Unspecified` - for unspecified upper bound (-2)
    * Defined in `org.abreslav.java2ecore.multiplicities` package
  * `_NUM` where `NUM` is a non-negative integer
```
class A {
    MList<String, _1, _5> strings; // 1..5
}
```
    * **`_`0** cannot be specified as an upper bound and upper bound cannot be less than lower bound
    * Types **`_`0** through **`_`12** are defined in `org.abreslav.java2ecore.multiplicities`
    * If you need higher values you can create your own:
```
@NonModel // this annotation is described below
interface _239 extends ILowerBound, IUpperBound {
}
```

### @Bounds ###

`@Bounds` annotation can be supplied to a field to explicitly denote feature's multiplicity. The field may be of a collection or non-collection type (MCollections do not allow to override specified multiplicity):
```
class A {
   @Bounds({5, 8})
   int x; // 5..8

   @Bounds({0, 6})
   int[] x; // 0..6

   @Bounds({1, 100})
   List<Integer> x; // 1..100

   @Bounds(1)
   int x; // 1..1
}
```

## Attributes mapped from Java modifiers ##
  * `final` sets _changeable_ to `false`
  * `transient` sets _transient_ to `true`
  * `volatile` sets _volatile_ to `true`
  * All the modifiers which do not have analogs in Ecore are ignored

## Default values ##
You can specify default value by initializing a field with a **constant** or an **enum literal**:
```
class A {
    String s = "some"; // defaultValue = "some"
    E e = E.LITERAL; // defaultValue = E.LITERAL
}
```

If the field is `final` Java forces you to initialize it. In this case if you want to leave `defaultValue` unset, you can use one of the two opportunities:
  * assign `null` if it is allowed:
```
final String s = null; // no default value
```
  * if `null` is not allowed you can supply `@NoDefaultValue` annotation:
```
@NoDefaultValue
final int x = 0;
```

Sometimes you have to specify a non-trivial value for `defaultValueLiteral` attribute. This can be done by supplying `@DefaultValueLiteral` annotation:
```
@DefaultValueLiteral("some_misterious_value")
MyClass a;
```

## Opposite references ##

You can use `@Opposite` annotation to denote an opposite reference for the defined reference.
```
class Book {
    @Opposite("books")
    Writer writer;
}
class Writer {
    Set<Book> books;
}
```
In this example references `writer` and `books` are opposite to each other. All the opposites in Java2Ecore are bidirectional (`a` cannot be an opposite to `b` unless `b` is opposite to `a`).

The following teo examples are absolutely equivalent to each other and to the first example:
```
class Book {
    @Opposite("books")
    Writer writer;
}
class Writer {
    @Opposite("writer")
    Set<Book> books;
}
```

means the same as

```
class Book {
    Writer writer;
}
class Writer {
    @Opposite("writer")
    Set<Book> books;
}
```

## Other attributes of structural features ##

Other attributes are set by annotations:
  * `@Containment`
    * Denotes a containment reference
  * `@Derived`
    * Denotes a derived feature
  * `@ID`
    * Denotes an ID-attribute
  * `@Ordered(boolean value() default true)`
    * Sets _ordered_ attribute of a feature to `value()`
  * `@ResolveProxies(boolean value() default true)`
    * Sets _resolveProxies_ attribute of a feature to `value()`
  * `@Unique(boolean value() default true)`
    * Sets _unique_ attribute of a feature to `value()`
  * @Unsettable
    * Denotes an unsettable reference

# Operations #

Operations are defined just as in Java.
```
class A {
    void a(String x, int y) {
    }
}
```
Method body is always ignored.

All the multiplicity-related things mentioned above apply for operations' return values and parameter types.

To avoid writing stub code for methods returning values you can use abstract methods and to avoid making your class necessary abstract you can use _interface form of class_ (see above).

# Generics #

Almost nothing to say: generics are fully supported :)...
  * Generic classes, interfaces and data types
    * even those created automatically for unknown types
  * Type parameters for methods
  * Upper and lower bounds (<T extends Some>, <? super T>)
  * Several bounds on the same variable (<T extends A & B>)
  * Wildcards
    * with no bound (<?>)
    * with upper bound (<? extends T>)
    * with lower bound (<? super T>)

Did you ever know that Java itself supports all these things? :)

And a big sample:
```
class A<T, Q extends Comparable<? super T>> {

}
class B<E> {
abstract class _{
    A<E, Comparable<E>>;
    abstract <R, P> R doIt(Collection<? extends P & Serializable> objects) {};
}
}
class C extends B<String> {

}
```