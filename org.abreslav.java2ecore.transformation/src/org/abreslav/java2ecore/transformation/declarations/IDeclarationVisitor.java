package org.abreslav.java2ecore.transformation.declarations;

public interface IDeclarationVisitor {
	void visit(EClassDeclaration declaration);

	void visit(EDataTypeDeclaration declaration);

	void visit(EEnumDeclaration declaration);

	void visit(EPackageDeclaration declaration);
}
