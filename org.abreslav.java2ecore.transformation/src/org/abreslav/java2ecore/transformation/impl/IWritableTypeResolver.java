package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.jdt.core.dom.ITypeBinding;

public interface IWritableTypeResolver extends ITypeResolver {

	void addEClass(ITypeBinding type, EClass eClass);

	void addEDataType(ITypeBinding type, EDataType eDataType);

}