package org.abreslav.java2ecore.transformation.impl.typeresolver;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

public class ItemStorageWithStringKeys implements IItemStorageWithStringKeys {
	private final Map<String, EClass> myEClasses = new HashMap<String, EClass>();
	private final Map<String, EDataType> myEDataTypes = new HashMap<String, EDataType>();
	private final Map<String, EEnum> myEEnums = new HashMap<String, EEnum>();
	private final Map<String, EPackage> myEPackages = new HashMap<String, EPackage>();

	private final IItemStorageWithStringKeys myParent;
	
	public ItemStorageWithStringKeys() {
		this(NullStorage.INSTANCE);
	}
	
	public ItemStorageWithStringKeys(IItemStorageWithStringKeys parent) {
		myParent = parent;
	}

	public void addEClass(String type, EClass class1) {
		myEClasses.put(type, class1);
	}
	
	public void addEDataType(String type, EDataType dataType) {
		myEDataTypes.put(type, dataType);
	}

	public void addEEnum(String type, EEnum enum1) {
		myEEnums.put(type, enum1);
	}

	public void addEPackage(String type, EPackage package1) {
		myEPackages.put(type, package1);
	}

	public EClass getEClass(String type) {
		EClass result = myEClasses.get(type);
		if (result == null) {
			result = myParent.getEClass(type);
		}
		return result;
	}

	public EDataType getEDataType(String type) {
		EDataType result = myEDataTypes.get(type);
		if (result == null) {
			result = myParent.getEDataType(type);
		}
		return result;
	}

	public EEnum getEEnum(String type) {
		EEnum result = myEEnums.get(type);
		if (result == null) {
			result = myParent.getEEnum(type);
		}
		return result;
	}

	public EPackage getEPackage(String type) {
		EPackage result = myEPackages.get(type);
		if (result == null) {
			result = myParent.getEPackage(type);
		}
		return result;
	}

}
