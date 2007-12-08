package org.abreslav.java2ecore.transformation.impl.typeresolver;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.dom.ITypeBinding;

public interface IItemStorage {
	EClass getEClass(ITypeBinding type);
	EEnum getEEnum(ITypeBinding type);	
	EDataType getEDataType(ITypeBinding type);
	EPackage getEPackage(ITypeBinding type);

	void addEClass(ITypeBinding type, EClass eClass);
	void addEEnum(ITypeBinding type, EEnum eEnum);
	void addEDataType(ITypeBinding type, EDataType eDataType);
	void addEPackage(ITypeBinding type, EPackage ePackage);
}
