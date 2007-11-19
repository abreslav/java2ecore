package org.abreslav.java2ecore.transformation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.abreslav.java2ecore.transformation.utils.NullSet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.jdt.core.dom.ITypeBinding;

public class WrappingTypeResolver implements ITypeResolver {

	private final ITypeResolver myResolver;
	private final Map<String, EDataType> myEDataTypeMap = new HashMap<String, EDataType>();
	private Collection<? super EDataType> myEDataTypes;
	
	public WrappingTypeResolver(ITypeResolver resolver, Collection<? super EDataType> eDataTypes) {
		myResolver = resolver;
		if (eDataTypes == null) {
			myEDataTypes = new NullSet<Object>();
		} else {
			myEDataTypes = eDataTypes;
		}
	}

	public EClass getEClass(ITypeBinding type) {
		return myResolver.getEClass(type);
	}

	public EDataType getEDataType(ITypeBinding type) {
		EDataType eDataType = myResolver.getEDataType(type);
		if (eDataType == null) {
			eDataType = myEDataTypeMap.get(type);
		}
		if (eDataType == null) {
			eDataType = EcoreFactory.eINSTANCE.createEDataType();
			eDataType.setName(type.getName());
			String qualifiedName = type.getErasure().getQualifiedName();
			eDataType.setInstanceTypeName(qualifiedName);

			// TODO: Use generic arguments
			
			myEDataTypeMap.put(qualifiedName, eDataType);
			myEDataTypes.add(eDataType);
		}
		return eDataType;
	}

	public Collection<EDataType> getWrappedTypes() {
		return myEDataTypeMap.values();
	}

	public EGenericType resolveEGenericType(ITypeBinding binding,
			EClassTypeParameterIndex typeParameterIndex) {
		return TypeResolver.doResolveEGenericType(this, binding, typeParameterIndex);
	}
}
