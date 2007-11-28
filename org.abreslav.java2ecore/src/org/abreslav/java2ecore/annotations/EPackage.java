package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes a class that describes EPackage
 * @author abreslav
 *
 */
@Target({ElementType.TYPE})
public @interface EPackage {
	
	/**
	 * Corresponds to nsPrefix attribute of an EPackage  
	 */
	String nsPrefix();

	/**
	 * Corresponds to nsURI attribute of an EPackage  
	 */
	String nsURI();
}
