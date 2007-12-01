package org.abreslav.java2ecore.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes a type which must be skipped by Java2Ecore.
 * No Ecore classifier will be created from such a class.
 * <br/>
 * This annotation is not applicable to classes declared outside a package class. 
 *
 */
@Target(ElementType.TYPE)
public @interface NonModel {

}
