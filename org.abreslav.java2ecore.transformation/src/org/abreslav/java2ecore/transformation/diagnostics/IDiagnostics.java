package org.abreslav.java2ecore.transformation.diagnostics;

import java.util.Iterator;

import org.eclipse.jdt.core.dom.ASTNode;

public interface IDiagnostics extends Iterable<Diagnostic> {

	public abstract void reportWarning(String message, ASTNode node);

	public abstract void reportError(String message, ASTNode node);

	public abstract boolean hasErrors();

	public abstract Iterator<Diagnostic> iterator();

}