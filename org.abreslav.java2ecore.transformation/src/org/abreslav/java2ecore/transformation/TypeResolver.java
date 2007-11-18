package org.abreslav.java2ecore.transformation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

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

}
