/**
 * 
 */
package org.abreslav.java2ecore.transformation.impl.typesettings;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;

public interface IUnwrapStrategy {
	Type unwrap(Type type);
	
	IUnwrapStrategy NO_UNWRAP = new IUnwrapStrategy() {
		public Type unwrap(Type type) {
			return type;
		}
	};
	
	IUnwrapStrategy UNWRAP_ARRAY = new IUnwrapStrategy() {
		public Type unwrap(Type type) {
			return ((ArrayType) type).getElementType();
		}
	};
	
	IUnwrapStrategy UNWRAP_GENERIC = new IUnwrapStrategy() {
		public Type unwrap(Type type) {
			return (Type) ((ParameterizedType) type).typeArguments().get(0);
		}
	};
}