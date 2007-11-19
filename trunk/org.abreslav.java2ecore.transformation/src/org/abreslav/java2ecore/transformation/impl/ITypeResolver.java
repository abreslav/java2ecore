package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.jdt.core.dom.ITypeBinding;

public interface ITypeResolver {

	EClass getEClass(ITypeBinding type);

	EDataType getEDataType(ITypeBinding type);
	
	EGenericType resolveEGenericType(ITypeBinding binding, EClassTypeParameterIndex typeParameterIndex);

}