package org.abreslav.java2ecore.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a classifier (typically described by Java interface) to be a class, 
 * not an interface.<br/>
 * <br/>
 * Use this annotation on Java interfaces when you need to establish multiple 
 * inheritance of Ecore classifiers but Java does not allow this.<br/>
 * <br/>
 * Example:
 * <pre>
 * // This denotes an EClass A
 * &#064;EClass interface A {
 * class_ {
 *     int x;
 * }
 * }
 * 
 * // This denotes abstract EClass B
 * abstract &#064;EClass interface B {
 * class_ {
 *     int y;
 * }
 * }
 * 
 * // This EClass has two superclasses: A and B
 * class C implements A, B {
 * }
 * </pre>
 *
 */
@Target(ElementType.TYPE)
public @interface EClass {
}
