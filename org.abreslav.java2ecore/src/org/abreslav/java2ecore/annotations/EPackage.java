package org.abreslav.java2ecore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Denotes a class (or interface) that describes EPackage.
 * <br/>
 * A class which is marked with this annotation is considered to be an EPackage 
 * description. A class' name becomes EPackage's <code>name</code>, 
 * <code>nsPrefix</code> and <code>nsURI</code> attributes are set through corresponding 
 * annotation attributes. <br/>
 * <br/>
 * EPackage contents (i.e. classifiers and subpackages) are declared as nested classes. <br/>
 * <br/>
 * Example:
 * <pre>
 * &#064;EPackage(
 *     nsPrefix = "example",
 *     nsURI = "http:///example.com/example"
 * )
 * public interface example {
 *     class ExampleClass {
 *     
 *     }
 *     
 *     &#064;EPackage(
 *         nsPrefix = "sub",
 *         nsURI = "http:///example.com/example/sub"
 *     )
 *     class sub {
 *         class ClassInsideSubpackage {
 *         }
 *     }
 * }
 * </pre>
 * 
 * This annotation is mandatory for any top-level class in <code>ecores</code> 
 * source folder.
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
