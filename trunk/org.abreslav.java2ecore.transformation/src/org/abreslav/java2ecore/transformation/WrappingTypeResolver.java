package org.abreslav.java2ecore.transformation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcoreFactory;

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
			eDataType.setName(fqn.substring(fqn.lastIndexOf('.') + 1));
			eDataType.setInstanceTypeName(fqn);
			myEDataTypeMap.put(fqn, eDataType);
			myEDataTypes.add(eDataType);
		}
		return eDataType;
	}

	public Collection<EDataType> getWrappedTypes() {
		return myEDataTypeMap.values();
	}
}
