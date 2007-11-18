package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface EDataType {
	/**
	 * Means this EDataType's instanceTypeName property value
	 */
	String value();
}