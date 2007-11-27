package org.abreslav.java2ecore.transformation.diagnostics;

import org.eclipse.jdt.core.dom.ASTNode;

public class NullDiagnostics implements IDiagnostics {

	public void reportWarning(ASTNode node, IDiagnosticMessage message) {
		
	}

	public void reportError(ASTNode node, IDiagnosticMessage message) {
		
	}

	public void reportErrorFormatted(ASTNode node, IDiagnosticMessage message,
			Object... args) {
		
	}

	public void reportWarningFormatted(ASTNode node,
			IDiagnosticMessage message, Object... args) {
		
	}

}
