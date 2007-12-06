package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation allows you to use classes defined in some Ecore models from which
 * some Java code was generated.<br/>
 * <br/>
 * Specify current project-relative paths to *.genmodel resources
 * containing EMF GenModels for some Ecore models
 */
@Target({ElementType.TYPE})
public @interface ImportGenModel {
	/**
	 * An array of current project relative paths to *.genmodel resources
	 * containing EMF GenModels for some Ecore models
	 * @return
	 */
	String[] value();
}
