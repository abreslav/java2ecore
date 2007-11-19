package org.abreslav.java2ecore.transformation.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeResolver implements IWritableTypeResolver {
	private final Map<String, EClass> myEClasses = new HashMap<String, EClass>();
	private final Map<String, EDataType> myEDataTypes = new HashMap<String, EDataType>();

	public TypeResolver() {
		EList<EClassifier> classifiers = EcorePackage.eINSTANCE.getEClassifiers();
		for (EClassifier classifier : classifiers) {
			if (classifier instanceof EDataType) {
				myEDataTypes.put(classifier.getInstanceClassName(), (EDataType) classifier);
			}
		}

	}
	
	public EClass getEClass(ITypeBinding type) {
		return myEClasses.get(type.getErasure().getQualifiedName());
	}

	public EDataType getEDataType(ITypeBinding type) {
		return myEDataTypes.get(type.getErasure().getQualifiedName());
	}

	public void addEClass(ITypeBinding type, EClass eClass) {
		myEClasses.put(type.getErasure().getQualifiedName(), eClass);
	}

	public void addEDataType(ITypeBinding type, EDataType eDataType) {
		myEDataTypes.put(type.getErasure().getQualifiedName(), eDataType);
	}
	
	public EGenericType resolveEGenericType(ITypeBinding binding, EClassTypeParameterIndex typeParameterIndex) {
		return doResolveEGenericType(this, binding, typeParameterIndex);
	}

	/*package*/ static EGenericType doResolveEGenericType(ITypeResolver resolver, ITypeBinding binding,
			EClassTypeParameterIndex typeParameterIndex) {
		EGenericType eGenericType = EcoreFactory.eINSTANCE.createEGenericType(); 

		if (binding.isTypeVariable()) {
			eGenericType.setETypeParameter(typeParameterIndex.getETypeParameter(binding.getName()));
		} if (binding.isWildcardType()) { 
			ITypeBinding bound = binding.getBound();
			if (bound != null) {
				EGenericType eBound = resolver.resolveEGenericType(bound, typeParameterIndex);
				if (binding.isUpperbound()) {
					eGenericType.setEUpperBound(eBound);
				} else {
					eGenericType.setELowerBound(eBound);
				}
			}
		} else {
			processActualType(resolver, binding, eGenericType);
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		for (ITypeBinding typeArgument : typeArguments) {
			eGenericType.getETypeArguments().add(resolver.resolveEGenericType(typeArgument, typeParameterIndex));
		}
		
		return eGenericType;
	}

	private static void processActualType(ITypeResolver resolver, ITypeBinding binding,
			EGenericType eGenericType) {
		
		EClass eClass = resolver.getEClass(binding);
		if (eClass != null) {
			eGenericType.setEClassifier(eClass);
		} else {
			EDataType eDataType = resolver.getEDataType(binding);
			if (eDataType != null) {
				eGenericType.setEClassifier(eDataType);
			}
		}
	}

}
