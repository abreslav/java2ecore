package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.jdt.core.dom.ITypeBinding;

public interface ITypeResolver {

	EClass getEClass(String fqn);

	EDataType getEDataType(String fqn);
	
	EGenericType resolveEGenericType(ITypeBinding binding, EClassTypeParameterIndex typeParameterIndex);

}