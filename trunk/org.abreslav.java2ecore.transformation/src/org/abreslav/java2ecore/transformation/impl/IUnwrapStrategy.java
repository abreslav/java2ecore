/**
 * 
 */
package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.jdt.core.dom.ITypeBinding;

interface IUnwrapStrategy {
	ITypeBinding unwrap(ITypeBinding binding);
	
	IUnwrapStrategy NO_UNWRAP = new IUnwrapStrategy() {
		public ITypeBinding unwrap(ITypeBinding binding) {
			return binding;
		}
	};
	
	IUnwrapStrategy UNWRAP_ARRAY = new IUnwrapStrategy() {
		public ITypeBinding unwrap(ITypeBinding binding) {
			return binding.getElementType();
		}
	};
	
	IUnwrapStrategy UNWRAP_GENERIC = new IUnwrapStrategy() {
		public ITypeBinding unwrap(ITypeBinding binding) {
			return binding.getTypeArguments()[0];
		}
	};
}