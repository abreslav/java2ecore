package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation allows you to use classes defined in another Java2Ecore
 * source file.<br/>
 *<br/>
 * Not implemented yet.
 */
@Target({ElementType.TYPE})
public @interface Import {
	Class<?> value();
}
