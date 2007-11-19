package org.abreslav.java2ecore.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Class {
	public static final boolean ABSTRACT = true;
	
	boolean value() default false;
}
