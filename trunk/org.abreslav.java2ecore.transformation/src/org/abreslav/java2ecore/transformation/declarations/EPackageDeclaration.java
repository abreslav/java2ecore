package org.abreslav.java2ecore.transformation.declarations;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class EPackageDeclaration extends AbstractDeclaration<TypeDeclaration, EPackage> {

	public EPackageDeclaration(TypeDeclaration declaration, EPackage declaredElement) {
		super(declaration, declaredElement);
	}

	public void accept(IDeclarationVisitor visitor) {
		visitor.visit(this);
	}
}
