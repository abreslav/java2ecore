package org.abreslav.java2ecore.transformation.impl.typeresolver;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

public interface IItemStorageWithStringKeys {

	EClass getEClass(String type);

	EDataType getEDataType(String type);

	EEnum getEEnum(String type);

	EPackage getEPackage(String type);

}