package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;

public interface IWritableTypeResolver extends ITypeResolver {

	void addEClass(String fqn, EClass eClass);

	void addEDataType(String fqn, EDataType eDataType);

}