package org.abreslav.java2ecore.transformation.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.jdt.core.dom.ITypeBinding;

public interface ITypeResolver {

	EClass getEClass(ITypeBinding type);
	EEnum getEEnum(ITypeBinding type);
	EDataType getEDataType(ITypeBinding type);

	void addEClass(ITypeBinding type, EClass eClass);
	void addEEnum(ITypeBinding type, EEnum eEnum);
	void addEDataType(ITypeBinding type, EDataType eDataType);

	EDataType resolveEDataType(ITypeBinding type);	
	EGenericType resolveEGenericType(ITypeBinding binding, boolean forceEClass, TypeParameterIndex typeParameterIndex);
	
	TypeParameterIndex createTypeParameters(EClassifier eClassifier,
			ITypeBinding binding);
	
	Collection<ETypeParameter> createETypeParameters(
			TypeParameterIndex typeParameterIndex,
			List<ITypeBinding> typeParameters);

}