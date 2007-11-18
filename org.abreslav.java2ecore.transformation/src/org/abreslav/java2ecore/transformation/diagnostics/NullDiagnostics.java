package org.abreslav.java2ecore.transformation.diagnostics;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.ASTNode;

public class NullDiagnostics implements IDiagnostics {

	public boolean hasErrors() {
		return false;
	}

	public Iterator<Diagnostic> iterator() {
		return Collections.<Diagnostic>emptyList().iterator();
	}

	public void reportError(String message, ASTNode node) {
		
	}

	public void reportWarning(String message, ASTNode node) {
		
	}

}
