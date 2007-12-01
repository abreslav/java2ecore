package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Specifies defaultValueLiteral for a structural feature.
 */
@Target(ElementType.FIELD)
public @interface DefaultValueLiteral {
	/**
	 * Corresponds to <code>defaultValueLiteral</code> attribute.
	 */
	String value();
}
