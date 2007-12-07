package org.abreslav.java2ecore.transformation.impl;

import org.eclipse.jdt.core.dom.ASTNode;

public interface IUnknownTypeHandler {

	void handleUnknownType(String typeName, ASTNode node);
	void handleUnknownClass(String typeName, ASTNode node);
}
