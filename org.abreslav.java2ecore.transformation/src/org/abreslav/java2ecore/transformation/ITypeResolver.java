package org.abreslav.java2ecore.transformation;

import java.util.Collection;
import java.util.List;

import org.abreslav.java2ecore.transformation.impl.TypeParameterIndex;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;

public interface ITypeResolver {

	EPackage getEPackage(ITypeBinding type);

	EGenericType resolveEGenericType(Type type, boolean forceEClass, TypeParameterIndex typeParameterIndex);
	EClassifier resolveEClassifier(ITypeBinding binding, ASTNode node, boolean forceEClass);
	
	TypeParameterIndex createTypeParameters(EClassifier eClassifier,
			List<TypeParameter> typeParameters);
	
	Collection<ETypeParameter> createETypeParameters(
			TypeParameterIndex typeParameterIndex,
			List<TypeParameter> typeParameters);

}