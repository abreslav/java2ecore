package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes an <code>resolveProxies</code> attribute value for a reference.<br/>
 * Not applicable for EAttributes.<br/>
 * The <code>value</code> attribute corresponds to <code>resoveProxies</code> attribute value.
 */
@Target(ElementType.FIELD)
public @interface ResolveProxies {
	/**
     * Corresponds to <code>resoveProxies</code> attribute.
	 */
	boolean value() default true;
}
