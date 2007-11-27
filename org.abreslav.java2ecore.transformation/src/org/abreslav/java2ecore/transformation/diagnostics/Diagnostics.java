package org.abreslav.java2ecore.transformation.diagnostics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.ASTNode;

public class Diagnostics implements IDiagnostics, Iterable<Diagnostic> {
	
	private final Collection<Diagnostic> myDiagnostics = new ArrayList<Diagnostic>(); 
	private boolean myHasErrors = false;
	
	public void reportWarning(ASTNode node, IDiagnosticMessage message) {
		myDiagnostics.add(new Diagnostic(node, message.toString(), Severity.WARNING));
	}
	
	public void reportWarningFormatted(ASTNode node,
			IDiagnosticMessage message, Object... args) {
		myDiagnostics.add(new Diagnostic(node, message.format(args), Severity.WARNING));
	}	
	
	public void reportError(ASTNode node, IDiagnosticMessage message) {
		myDiagnostics.add(new Diagnostic(node, message.toString(), Severity.ERROR));
		myHasErrors = true;
	}
	
	public void reportErrorFormatted(ASTNode node, IDiagnosticMessage message,
			Object... args) {
		myDiagnostics.add(new Diagnostic(node, message.format(args), Severity.ERROR));
		myHasErrors = true;
	}
	
	public boolean hasErrors() {
		return myHasErrors;
	}

	public Iterator<Diagnostic> iterator() {
		return myDiagnostics.iterator();
	}
}