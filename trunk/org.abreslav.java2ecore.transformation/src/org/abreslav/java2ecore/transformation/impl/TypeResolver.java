package org.abreslav.java2ecore.transformation.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class TypeResolver implements IWritableTypeResolver {
	private final Map<String, EClass> myEClasses = new HashMap<String, EClass>();
	private final Map<String, EDataType> myEDataTypes = new HashMap<String, EDataType>();

	public EClass getEClass(String fqn) {
		return myEClasses.get(fqn);
	}

	public EDataType getEDataType(String fqn) {
		return myEDataTypes.get(fqn);
	}

	public void addEClass(String fqn, EClass eClass) {
		myEClasses.put(fqn, eClass);
	}

	public void addEDataType(String fqn, EDataType eDataType) {
		myEDataTypes.put(fqn, eDataType);
	}
	
	public EGenericType resolveEGenericType(ITypeBinding binding, EClassTypeParameterIndex typeParameterIndex) {
		EGenericType eGenericType = EcoreFactory.eINSTANCE.createEGenericType(); 

		if (binding.isTypeVariable()) {
			eGenericType.setETypeParameter(typeParameterIndex.getETypeParameter(binding.getName()));
		} if (binding.isWildcardType()) { 
			ITypeBinding bound = binding.getBound();
			EGenericType eBound = resolveEGenericType(bound, typeParameterIndex);
			if (binding.isUpperbound()) {
				eGenericType.setEUpperBound(eBound);
			} else {
				eGenericType.setELowerBound(eBound);
			}
		} else {
			processActualType(binding, eGenericType);
		}
		
		ITypeBinding[] typeArguments = binding.getTypeArguments();
		for (ITypeBinding typeArgument : typeArguments) {
			eGenericType.getETypeArguments().add(resolveEGenericType(typeArgument, typeParameterIndex));
		}
		
		return eGenericType;
	}

	private void processActualType(ITypeBinding binding,
			EGenericType eGenericType) {
		String fqn = binding.getErasure().getQualifiedName();
		
		EClass eClass = getEClass(fqn);
		if (eClass != null) {
			eGenericType.setEClassifier(eClass);
		} else {
			EDataType eDataType = getEDataType(fqn);
			if (eDataType != null) {
				eGenericType.setEClassifier(eDataType);
			}
		}
	}

}
