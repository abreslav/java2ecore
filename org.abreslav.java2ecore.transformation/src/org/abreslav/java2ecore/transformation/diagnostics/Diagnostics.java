package org.abreslav.java2ecore.transformation.diagnostics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.ASTNode;

public class Diagnostics implements IDiagnostics {
	
	private final Collection<Diagnostic> myDiagnostics = new ArrayList<Diagnostic>(); 
	private boolean myHasErrors = false;
	
	public void reportWarning(String message, ASTNode node) {
		myDiagnostics.add(new Diagnostic(node, message, Severity.WARNING));
	}
	
	public void reportError(String message, ASTNode node) {
		myDiagnostics.add(new Diagnostic(node, message, Severity.ERROR));
		myHasErrors = true;
	}
	
	public boolean hasErrors() {
		return myHasErrors;
	}

	public Iterator<Diagnostic> iterator() {
		return myDiagnostics.iterator();
	}	
}