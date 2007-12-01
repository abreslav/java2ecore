package org.abreslav.java2ecore.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Allows to set EClass' <code>instanceTypeName</code> property.
 * <br/>
 * Not applicable for EDataTypes. 
 *
 */
@Target(ElementType.TYPE)
public @interface InstanceTypeName {
	/**
	 * Corresponds to instanceTypeName attribute
	 */
	String value();
}
