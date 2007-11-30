package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation allows you to use classes defined in some Ecore from which
 * some Java code was generated.<br/>
 *<br/>
 * Not implemented yet.
 */
@Target({ElementType.TYPE})
public @interface ImportGenModel {
	String value();
}
