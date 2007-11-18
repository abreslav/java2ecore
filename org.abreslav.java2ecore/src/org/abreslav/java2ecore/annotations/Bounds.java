package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Bounds {
	int[] value() default {0, 1};
}
