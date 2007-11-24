package org.abreslav.java2ecore.transformation.impl;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeResolver implements ITypeResolver {
	private IItemStorage myItemStorage;
	private Collection<? super EClassifier> myWrappedEClassifiers;
	
	public TypeResolver(IItemStorage itemStorage,
			Collection<? super EClassifier> wrappedEClassifiers) {
		myItemStorage = itemStorage;
		myWrappedEClassifiers = wrappedEClassifiers;
	}

	public EClass getEClass(ITypeBinding type) {
		return myItemStorage.getEClass(type);
	}

	public EDataType getEDataType(ITypeBinding type) {
		EDataType eDataType = myItemStorage.getEDataType(type);
		if (eDataType != null) {
			return eDataType;
		}
		return getEEnum(type);
	}

	public EEnum getEEnum(ITypeBinding type) {
		return myItemStorage.getEEnum(type);
	}
	
	public EPackage getEPackage(ITypeBinding type) {
		return myItemStorage.getEPackage(type);
	}

	public EGenericType resolveEGenericType(ITypeBinding binding, boolean forceEClass, TypeParameterIndex typeParameterIndex) {
		EGenericType eGenericType = EcoreFactory.eINSTANCE.createEGenericType(); 
		
		if (binding.isTypeVariable()) {
			eGenericType.setETypeParameter(typeParameterIndex.getETypeParameter(binding.getName()));
		} if (binding.isWildcardType()) { 
			ITypeBinding bound = binding.getBound();
			if (bound != null) {
				EGenericType eBound = resolveEGenericType(bound, false, typeParameterIndex);
				if (binding.isUpperbound()) {
					eGenericType.setEUpperBound(eBound);
				} else {
					eGenericType.setELowerBound(eBound);
				}
			}
		} else {
			processActualType(binding.getTypeDeclaration(), eGenericType, forceEClass);
			if (eGenericType.getEClassifier() == null) {
				return null;
			}
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		for (ITypeBinding typeArgument : typeArguments) {
			eGenericType.getETypeArguments().add(resolveEGenericType(typeArgument, false, typeParameterIndex));
		}
		
		return eGenericType;
	}

	private void processActualType(ITypeBinding binding,
			EGenericType eGenericType, boolean forceEClass) {
		
		EClass eClass = getEClass(binding);
		if (forceEClass && eClass == null) {
			eClass = wrapUnknownClass(binding);
		}
		if (eClass != null) {
			eGenericType.setEClassifier(eClass);
		} else {
			EDataType eDataType = resolveEDataType(binding);
			if (eDataType != null) {
				eGenericType.setEClassifier(eDataType);
			}
		}
	}

	private EClass wrapUnknownClass(ITypeBinding binding) {
		EClass eClass;
		eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(binding.getErasure().getName());
		eClass.setAbstract((binding.getModifiers() & Modifier.ABSTRACT) != 0);
		eClass.setInstanceClassName(binding.getErasure().getQualifiedName());
		eClass.setInterface(binding.isInterface());
		createTypeParameters(eClass, binding);
		myItemStorage.addEClass(binding, eClass);
		myWrappedEClassifiers.add(eClass);
		return eClass;
	}
	
	public TypeParameterIndex createTypeParameters(EClassifier eClassifier,
			ITypeBinding binding) {
		TypeParameterIndex typeParameterIndex = new TypeParameterIndex(null);
		if (binding.isGenericType()) {
			ITypeBinding[] typeParameters = binding.getTypeParameters();
			eClassifier.getETypeParameters().addAll(createETypeParameters(
					typeParameterIndex, Arrays.asList(typeParameters)));
		}
		return typeParameterIndex;
	}

	public Collection<ETypeParameter> createETypeParameters(
			TypeParameterIndex typeParameterIndex,
			List<ITypeBinding> typeParameters) {
		Collection<ETypeParameter> eTypeParameters = new ArrayList<ETypeParameter>();
		for (ITypeBinding typeParameter : typeParameters) {
			ETypeParameter eTypeParameter = EcoreFactory.eINSTANCE.createETypeParameter();
			eTypeParameter.setName(typeParameter.getName());
			typeParameterIndex.registerTypeParameter(eTypeParameter);
			
			ITypeBinding[] typeBounds = typeParameter.getTypeBounds();
			for (ITypeBinding typeBound : typeBounds) {
				// TODO: Strange hack :)
				if (Object.class.getName().equals(typeBound.getQualifiedName())) {
					continue;
				}
				EGenericType eTypeBound = resolveEGenericType(typeBound, false, typeParameterIndex);
				eTypeParameter.getEBounds().add(eTypeBound);
			}
			eTypeParameters.add(eTypeParameter);
		}
		return eTypeParameters;
	}

	public EDataType resolveEDataType(ITypeBinding type) {
		EDataType eDataType = getEDataType(type);
		if (void.class.getCanonicalName().equals(type.getQualifiedName())) {
			return null;
		}
		if (eDataType == null) {
			eDataType = EcoreFactory.eINSTANCE.createEDataType();
			eDataType.setName(type.getErasure().getName());
			String qualifiedName = type.getErasure().getQualifiedName();
			eDataType.setInstanceTypeName(qualifiedName);

			createTypeParameters(eDataType, type.getTypeDeclaration());
			
			myItemStorage.addEDataType(type, eDataType);
			myWrappedEClassifiers.add(eDataType);
		}
		return eDataType;
	}
}
