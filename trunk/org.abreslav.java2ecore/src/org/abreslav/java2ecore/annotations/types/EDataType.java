package org.abreslav.java2ecore.annotations.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes an EDataType with a specified instance type.<br/>
 * <br/>
 * Use for classes which represent EDataTypes. 
 * Such classes cannot have any members or supertypes.
 * <br/>
 * Example:
 * <pre>
 * &#064;EDataType("javax.swing.JComponent")
 * class SwingComponent { 
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
public @interface EDataType {
	/**
	 * Corresponds to EDataType's instanceTypeName property value
	 */
	String value();
}
