package org.abreslav.java2ecore.transformation.diagnostics;

import org.eclipse.jdt.core.dom.ASTNode;

public class Diagnostic {
	private final ASTNode myNode;
	private final String myMessage;
	private final Severity mySeverity;
	
	public Diagnostic(ASTNode node, String message, Severity severity) {
		myNode = node;
		myMessage = message;
		mySeverity = severity;
	}
	
	public ASTNode getNode() {
		return myNode;
	}
	
	public String getMessage() {
		return myMessage;
	}
	
	public Severity getSeverity() {
		return mySeverity;
	}
}
