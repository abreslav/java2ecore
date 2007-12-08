package org.abreslav.java2ecore.transformation.impl.typeresolver;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

public class NullStorage implements IItemStorageWithStringKeys {

	public static final IItemStorageWithStringKeys INSTANCE = new NullStorage(); 
	
	private NullStorage() {
		
	}
	
	public EClass getEClass(String type) {
		return null;
	}

	public EDataType getEDataType(String type) {
		return null;
	}

	public EEnum getEEnum(String type) {
		return null;
	}

	public EPackage getEPackage(String type) {
		return null;
	}

}
