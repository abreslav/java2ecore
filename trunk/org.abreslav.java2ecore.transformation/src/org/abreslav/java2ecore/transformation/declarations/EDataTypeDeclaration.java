package org.abreslav.java2ecore.transformation.declarations;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class EDataTypeDeclaration extends AbstractDeclaration<TypeDeclaration, EDataType> {

	public EDataTypeDeclaration(TypeDeclaration declaration,
			EDataType declaredElement) {
		super(declaration, declaredElement);
	}

	public void accept(IDeclarationVisitor visitor) {
		visitor.visit(this);
	}
}
