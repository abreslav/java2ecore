package org.abreslav.java2ecore.transformation;

import org.eclipse.jdt.core.dom.ASTNode;

public class Diagnostic {
	private final ASTNode myNode;
	private final String myMessage;
	private final boolean myError;
	
	public Diagnostic(ASTNode node, String message) {
		this(node, message, true);
	}
	
	public Diagnostic(ASTNode node, String message, boolean isError) {
		myNode = node;
		myMessage = message;
		myError = isError;
	}
	
	public ASTNode getNode() {
		return myNode;
	}
	
	public String getMessage() {
		return myMessage;
	}
	
	public boolean isError() {
		return myError;
	}
}
