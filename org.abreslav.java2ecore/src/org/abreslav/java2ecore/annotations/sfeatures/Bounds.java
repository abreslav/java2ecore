package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Allows to specify ETypedElement's bounds (lower and upper).<br/>
 * <br/>
 * Applicable for attributes, references, operations and parameters.<br/>
 * One or two numbers have to be specified. If two numbers are specified (in 
 * the form <code>&#064;Bounds({1, 5})</code>) then the first one is a lower bound 
 * and the second is an upper bound. If only one number is specified 
 * (<code>&#064;Bounds(1)</code>), then this number is both lower and upper bound.
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Bounds {
	/**
	 * Corresponds to <code>lowerBound</code> and <code>upperBound</code> attributes.
	 * 
	 * @see Bounds
	 */
	int[] value() default {0, 1};
}
