package org.abreslav.java2ecore.transformation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.abreslav.java2ecore.transformation.IItemStorage;
import org.abreslav.java2ecore.transformation.ITypeResolver;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.WildcardType;

public class TypeResolver implements ITypeResolver {
	private IItemStorage myItemStorage;
	private IUnknownTypeHandler myUnknownTypeHandler;
	
	public TypeResolver(IItemStorage itemStorage,
			IUnknownTypeHandler unknownTypeHandler) {
		myItemStorage = itemStorage;
		myUnknownTypeHandler = unknownTypeHandler;
	}

	private EClass getEClass(ITypeBinding type) {
		return myItemStorage.getEClass(type);
	}

	private EDataType getEDataType(ITypeBinding type) {
		EDataType eDataType = myItemStorage.getEDataType(type);
		if (eDataType != null) {
			return eDataType;
		}
		return getEEnum(type);
	}

	private EEnum getEEnum(ITypeBinding type) {
		return myItemStorage.getEEnum(type);
	}
	
	public EPackage getEPackage(ITypeBinding type) {
		return myItemStorage.getEPackage(type);
	}

	public EGenericType resolveEGenericType(Type type, boolean forceEClass, TypeParameterIndex typeParameterIndex) {
		EGenericType eGenericType = EcoreFactory.eINSTANCE.createEGenericType(); 
		
		ITypeBinding binding = type.resolveBinding();
		if (binding.isTypeVariable()) {
			eGenericType.setETypeParameter(typeParameterIndex.getETypeParameter(binding.getName()));
		} if (type.isWildcardType()) { 
			Type bound = ((WildcardType) type).getBound();
			if (bound != null) {
				EGenericType eBound = resolveEGenericType(bound, false, typeParameterIndex);
				if (binding.isUpperbound()) {
					eGenericType.setEUpperBound(eBound);
				} else {
					eGenericType.setELowerBound(eBound);
				}
			}
		} else {
			EClassifier actualType = resolveEClassifier(binding, type, forceEClass);
			if (actualType == null) {
				return eGenericType;
			}
			
			eGenericType.setEClassifier(actualType);
		}
		
		if (type.isParameterizedType()) {
			@SuppressWarnings("unchecked")
			List<Type> typeArguments = (List<Type>) type.getStructuralProperty(ParameterizedType.TYPE_ARGUMENTS_PROPERTY);
			for (Type typeArgument : typeArguments) {
				eGenericType.getETypeArguments().add(resolveEGenericType(typeArgument, false, typeParameterIndex));
			}
		}
		
		return eGenericType;
	}

	public TypeParameterIndex createTypeParameters(EClassifier eClassifier,
			List<TypeParameter> typeParameters) {
		TypeParameterIndex typeParameterIndex = new TypeParameterIndex(null);
		eClassifier.getETypeParameters().addAll(createETypeParameters(typeParameterIndex, typeParameters));
		return typeParameterIndex;
	}

	public Collection<ETypeParameter> createETypeParameters(
			final TypeParameterIndex typeParameterIndex,
			List<TypeParameter> typeParameters) {
		List<Runnable> deferredCommands = new ArrayList<Runnable>();
		Collection<ETypeParameter> eTypeParameters = new ArrayList<ETypeParameter>();
		for (final TypeParameter typeParameter : typeParameters) {
			final ETypeParameter eTypeParameter = EcoreFactory.eINSTANCE.createETypeParameter();
			eTypeParameter.setName(typeParameter.getName().getIdentifier());
			typeParameterIndex.registerTypeParameter(eTypeParameter);

			deferredCommands.add(new Runnable() {
				public void run() {
					@SuppressWarnings("unchecked")
					List<Type> typeBounds = typeParameter.typeBounds();
					for (Type typeBound : typeBounds) {
						// TODO: Strange hack :)
						if (Object.class.getName().equals(typeBound.resolveBinding().getQualifiedName())) {
							continue;
						}
						EGenericType eTypeBound = resolveEGenericType(typeBound, false, typeParameterIndex);
						eTypeParameter.getEBounds().add(eTypeBound);
					}
				}
			});
			
			eTypeParameters.add(eTypeParameter);
		}
		
		for (Runnable runnable : deferredCommands) {
			runnable.run();
		}
		
		return eTypeParameters;
	}

	public EClassifier resolveEClassifier(ITypeBinding binding, ASTNode node, boolean forceEClass) {
		if (void.class.getCanonicalName().equals(binding.getQualifiedName())) {
			return null;
		}
		EClass eClass = getEClass(binding);
		if (forceEClass && eClass == null) {
			myUnknownTypeHandler.handleUnknownClass(binding.getName(), node);
			return null;
		}
		if (eClass != null) {
			return eClass;
		}
		EDataType eDataType = getEDataType(binding);
		if (eDataType == null) {
			myUnknownTypeHandler.handleUnknownType(binding.getName(), node);
		}
		return eDataType;
	}

//	private EDataType resolveEDataType(Type type) {
//		ITypeBinding typeBinding = type.resolveBinding();
//		EDataType eDataType = getEDataType(typeBinding);
//		if (eDataType == null) {
//			eDataType = EcoreFactory.eINSTANCE.createEDataType();
//			eDataType.setName(typeBinding.getErasure().getName());
//			String qualifiedName = typeBinding.getErasure().getQualifiedName();
//			eDataType.setInstanceTypeName(qualifiedName);
//
//			if (type.isParameterizedType()) {
//				@SuppressWarnings("unchecked")
//				List<Type> typeArguments = (List<Type>) type.getStructuralProperty(ParameterizedType.TYPE_ARGUMENTS_PROPERTY);
//				createTypeParameters(eDataType, typeArguments);
//			}
//			
//			myItemStorage.addEDataType(typeBinding, eDataType);
//			myUnknownTypeHandler.handleUnknownType(type, eDataType);
//		}
//		return eDataType;
//	}
}
