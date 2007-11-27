package org.abreslav.java2ecore.transformation.diagnostics;

import org.eclipse.jdt.core.dom.ASTNode;

public interface IDiagnostics {

	public abstract void reportWarning(ASTNode node, IDiagnosticMessage message);
	public abstract void reportWarningFormatted(ASTNode node, IDiagnosticMessage message, Object... args);

	public abstract void reportError(ASTNode node, IDiagnosticMessage message);
	public abstract void reportErrorFormatted(ASTNode node, IDiagnosticMessage message, Object... args);
}