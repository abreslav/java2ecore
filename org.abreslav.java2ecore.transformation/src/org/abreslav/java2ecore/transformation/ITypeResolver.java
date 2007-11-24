package org.abreslav.java2ecore.transformation;

import java.util.Collection;
import java.util.List;

import org.abreslav.java2ecore.transformation.impl.TypeParameterIndex;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.jdt.core.dom.ITypeBinding;

public interface ITypeResolver {

	EPackage getEPackage(ITypeBinding type);
	EClass getEClass(ITypeBinding type);
	EEnum getEEnum(ITypeBinding type);
	EDataType getEDataType(ITypeBinding type);

	EGenericType resolveEGenericType(ITypeBinding binding, boolean forceEClass, TypeParameterIndex typeParameterIndex);
	
	TypeParameterIndex createTypeParameters(EClassifier eClassifier,
			ITypeBinding binding);
	
	Collection<ETypeParameter> createETypeParameters(
			TypeParameterIndex typeParameterIndex,
			List<ITypeBinding> typeParameters);

}