package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation allows you to use classes defined in some Ecore models from which
 * some Java code was generated.<br/>
 * <br/>
 * Specify paths to *.genmodel resources containing EMF GenModels for some Ecore models.
 * <br/>
 * You can specify either relative or absolute paths, but those two mean different things:
 * <ul>
 *   <li> If you specify a relative path (like "model/my.gemodel") then it is treated as 
 *        current project relative: it is appended to the path of the project where
 *        your Java2Ecore source file is located.
 *   </li>
 *   <li> If you specify an absolute path (like "/other.project/model/other.genmodel")
 *        then it is treated as workspace-relative, so the first segment has to be
 *        a project name. 
 *   </li>
 * </ul>
 * NOTE: Paths with device specification (like "C:/somepath") are not supported.
 */
@Target({ElementType.TYPE})
public @interface ImportGenModel {
	/**
	 * An array of paths to *.genmodel resources
	 * containing EMF GenModels for some Ecore models
	 * @return
	 */
	String[] value();
}
