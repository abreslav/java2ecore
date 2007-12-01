package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes an unsettable reference.<br/>
 * Not applicable for EAttributes.
 *
 */
@Target(ElementType.FIELD)
public @interface Unsettable {

}
