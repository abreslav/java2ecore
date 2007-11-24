package org.abreslav.java2ecore.transformation.diagnostics;

import org.eclipse.jdt.core.dom.ASTNode;

public interface IDiagnostics {

	public abstract void reportWarning(String message, ASTNode node);

	public abstract void reportError(String message, ASTNode node);
}