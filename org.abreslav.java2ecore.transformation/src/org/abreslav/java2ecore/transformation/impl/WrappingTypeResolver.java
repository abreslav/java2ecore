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

	public EClass getEClass(String fqn) {
		return myResolver.getEClass(fqn);
	}

	public EDataType getEDataType(String fqn) {
		EDataType eDataType = myResolver.getEDataType(fqn);
		if (eDataType == null) {
			eDataType = myEDataTypeMap.get(fqn);
		}
		if (eDataType == null) {
			eDataType = EcoreFactory.eINSTANCE.createEDataType();
			String name = fqn;
			int genericIndex = name.indexOf('<');
			if (genericIndex >= 0) {
				name = name.substring(0, genericIndex);
			}
			name = name.substring(name.lastIndexOf('.') + 1);
			eDataType.setName(name);
			eDataType.setInstanceTypeName(fqn);
			myEDataTypeMap.put(fqn, eDataType);
			myEDataTypes.add(eDataType);
		}
		return eDataType;
	}

	public Collection<EDataType> getWrappedTypes() {
		return myEDataTypeMap.values();
	}

	public EGenericType resolveEGenericType(ITypeBinding binding,
			EClassTypeParameterIndex typeParameterIndex) {
		return myResolver.resolveEGenericType(binding, typeParameterIndex);
	}
}
