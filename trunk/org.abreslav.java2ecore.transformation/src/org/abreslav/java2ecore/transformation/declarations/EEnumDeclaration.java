package org.abreslav.java2ecore.transformation.declarations;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.jdt.core.dom.EnumDeclaration;

public class EEnumDeclaration extends AbstractDeclaration<EnumDeclaration, EEnum> {

	public EEnumDeclaration(EnumDeclaration declaration, EEnum declaredElement) {
		super(declaration, declaredElement);
	}

	public void accept(IDeclarationVisitor visitor) {
		visitor.visit(this);
	}
}
