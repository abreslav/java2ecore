package org.abreslav.java2ecore.transformation.diagnostics;

public interface IDiagnosticMessage {
	String toString();
	String format(Object... args);
}
