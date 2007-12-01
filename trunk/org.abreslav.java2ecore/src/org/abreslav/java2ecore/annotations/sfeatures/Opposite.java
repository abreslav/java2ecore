package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * Denotes an opposite for a reference.<br/>
 * Not applicable for EAttributes.<br/>
 * The <code>value</code> attribute corresponds to an opposite feature name.
 * Opposites are bidirectional. This means that if <code>a</code> is opposite 
 * to <code>b</code>, then <code>b</code> is opposite to <code>a</code>.
 * So you do not need to specify this annotation twice for a pair of opposite 
 * references.<br/>
 * Example:
 * <pre>
 * class A {
 *   &#064;Opposite("a")
 *   B b;
 * }
 * class B {
 *   A a;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
public @interface Opposite {
	/**
	 * Corresponds to an <code>eOpposite</code> reference name
	 */
	String value();
}
