package org.abreslav.java2ecore.transformation.declarations;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public interface IDeclarationCollector {
	void addEClass(TypeDeclaration type, EClass eClass);
	void addEEnum(EnumDeclaration type, EEnum eEnum);
	void addEDataType(TypeDeclaration type, EDataType eDataType);
	void addEPackage(TypeDeclaration type, EPackage ePackage);
	
	List<IDeclaration> getDeclarations();
}
