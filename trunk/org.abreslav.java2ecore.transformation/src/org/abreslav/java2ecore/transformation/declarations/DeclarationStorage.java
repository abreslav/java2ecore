package org.abreslav.java2ecore.transformation.declarations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class DeclarationStorage implements IDeclarationStorage {

	private List<IDeclaration> myDeclarations = new ArrayList<IDeclaration>();
	
	public void addEClass(TypeDeclaration type, EClass element) {
		myDeclarations.add(new EClassDeclaration(type, element));
	}

	public void addEDataType(TypeDeclaration type, EDataType element) {
		myDeclarations.add(new EDataTypeDeclaration(type, element));
	}

	public void addEEnum(EnumDeclaration type, EEnum element) {
		myDeclarations.add(new EEnumDeclaration(type, element));
	}

	public void addEPackage(TypeDeclaration type, EPackage element) {
		myDeclarations.add(new EPackageDeclaration(type, element));
	}

	public List<IDeclaration> getDeclarations() {
		return myDeclarations;
	}

}
