package org.abreslav.java2ecore.transformation.declarations;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class EClassDeclaration extends AbstractDeclaration<TypeDeclaration, EClass> {
	
	public EClassDeclaration(TypeDeclaration declaration, EClass declaredElement) {
		super(declaration, declaredElement);
	}

	public void accept(IDeclarationVisitor visitor) {
		visitor.visit(this);
	}
}
