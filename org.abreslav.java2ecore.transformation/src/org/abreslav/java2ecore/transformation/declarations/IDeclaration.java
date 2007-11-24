package org.abreslav.java2ecore.transformation.declarations;

public interface IDeclaration {
	void accept(IDeclarationVisitor visitor);
}
