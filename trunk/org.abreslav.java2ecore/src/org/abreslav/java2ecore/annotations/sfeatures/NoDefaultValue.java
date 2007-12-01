package org.abreslav.java2ecore.annotations.sfeatures;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * Denotes that a feature doe not have a default value.<br/>
 * Cannot be specified together with {@link DefaultValueLiteral @DefaultValueLiteral}.<br/>
 * This annotation is convenient for final fields for which an initializer is mandatory.
 * For object types one can specify <code>null</code> value to denote no default value.
 * But for primitive types this is impossible, so &#064;NoDefaultValue is used instead.
 * <br/>
 * Example:
 * <pre>
 * class A {
 *   &#064;NoDefaultValue
 *   &#064;Derived transient volatile final int x = 0;
 * }
 * </pre> 
 */
@Target(ElementType.FIELD)
public @interface NoDefaultValue {
}
