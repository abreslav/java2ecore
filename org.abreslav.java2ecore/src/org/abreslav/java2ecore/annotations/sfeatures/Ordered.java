package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes an <code>ordered</code> attribute value for a structural feature.<br/>
 * The <code>value</code> attribute corresponds to <code>ordered</code> attribute value.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Ordered {
	/**
	 * Corresponds to <code>ordered</code> attribute.
	 */
	boolean value() default true;
}
